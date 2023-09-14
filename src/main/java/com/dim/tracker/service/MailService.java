package com.dim.tracker.service;

import com.dim.tracker.entity.MovementHistory;
import com.dim.tracker.entity.PostOffice;
import com.dim.tracker.repository.MailItemRepository;
import com.dim.tracker.entity.MailItem;
import com.dim.tracker.repository.MovementHistoryRepository;
import com.dim.tracker.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MailService {
    private final MailItemRepository mailItemRepository;
    private final PostOfficeRepository postOfficeRepository;
    private final MovementHistoryRepository movementHistoryRepository;
    private final MovementHistoryService movementHistoryService;

    @Autowired
    public MailService(MailItemRepository mailItemRepository, PostOfficeRepository postOfficeRepository,
                       MovementHistoryRepository movementHistoryRepository, MovementHistoryService movementHistoryService) {
        this.mailItemRepository = mailItemRepository;
        this.postOfficeRepository = postOfficeRepository;
        this.movementHistoryRepository = movementHistoryRepository;
        this.movementHistoryService =movementHistoryService;
    }

    public MailItem registerMailItem(MailItem mailItem) {
        MailItem registeredMailItem = mailItemRepository.save(mailItem);

        MovementHistory historyEntry = new MovementHistory();
        historyEntry.setMailItem(registeredMailItem);
        historyEntry.setTimestamp(LocalDateTime.now());
        historyEntry.setLocation("Регистрация отправления");
        historyEntry.setStatus("Зарегистрировано");
        movementHistoryRepository.save(historyEntry);
        return registeredMailItem;
    }

    public MailItem updateCurrentPostOffice(Long mailItemId, Long postOfficeId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);
        PostOffice postOffice = postOfficeRepository.findById(postOfficeId).orElse(null);

        if (mailItem != null && postOffice != null) {
            mailItem.setCurrentPostOffice(postOffice);
            mailItem = mailItemRepository.save(mailItem);

            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(mailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Промежуточное отделение");
            historyEntry.setStatus("Прибыло в отделение");
            movementHistoryRepository.save(historyEntry);
            return mailItem;
        }
        return null;
    }

    public MailItem updateDepartureFromPostOffice(Long mailItemId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);

        if (mailItem != null) {
            mailItem.setCurrentPostOffice(null);
            mailItem = mailItemRepository.save(mailItem);

            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(mailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Покинуло отделение");
            historyEntry.setStatus("Отправлено из отделения");
            movementHistoryRepository.save(historyEntry);
            return mailItem;
        } else {
            return null;
        }
    }

    public MailItem updateReceivedStatus(Long mailItemId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);

        if (mailItem != null) {
            mailItem.setReceived(true);
            mailItem = mailItemRepository.save(mailItem);

            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(mailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Адресат получил отправление");
            historyEntry.setStatus("Доставлено адресату");
            movementHistoryRepository.save(historyEntry);
            return mailItem;
        }
        return null;
    }

    public List<MovementHistory> findMailItemById(Long mailItemId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);

        if (mailItem != null) {
            return movementHistoryService.getFullHistory(mailItem);
        }
        return null;
    }
}
