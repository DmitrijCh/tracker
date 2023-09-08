package com.example.tracker.controller;

import com.example.tracker.entity.*;
import com.example.tracker.repository.MovementHistoryRepository;
import com.example.tracker.service.MailService;
import com.example.tracker.service.MovementHistoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class Controller {
    private final MailService mailService;
    private final MovementHistoryService movementHistoryService;
    private final MovementHistoryRepository movementHistoryRepository;
    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    public Controller(MailService mailService, MovementHistoryService movementHistoryService, MovementHistoryRepository movementHistoryRepository) {
        this.mailService = mailService;
        this.movementHistoryService = movementHistoryService;
        this.movementHistoryRepository = movementHistoryRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<MailItem> registerMail(@RequestBody MailItem mailItem) {
        logger.info("Поступил запрос на регистрацию почты:");
        logger.info("");

        MailItem registeredMailItem = mailService.registerMailItem(mailItem);

        MovementHistory historyEntry = new MovementHistory();
        historyEntry.setMailItem(registeredMailItem);
        historyEntry.setTimestamp(LocalDateTime.now());
        historyEntry.setLocation("Регистрация отправления");
        historyEntry.setStatus("Зарегистрировано");
        movementHistoryRepository.save(historyEntry);
        return ResponseEntity.ok(registeredMailItem);
    }

    @PostMapping("/arrival")
    public ResponseEntity<MailItem> updateMailArrival(@RequestBody MailArrivalRequest request) {
        Long mailItemId = request.getMailItemId();
        Long postOfficeId = request.getPostOfficeId();

        logger.info("Получен запрос на обновление прибытия почты.");
        logger.info("Идентификатор почтового отправления: {}", mailItemId);
        logger.info("Идентификатор почтового отделения: {}", postOfficeId);

        MailItem updatedMailItem = mailService.updateCurrentPostOffice(mailItemId, postOfficeId);

        if (updatedMailItem != null) {
            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(updatedMailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Промежуточное отделение");
            historyEntry.setStatus("Прибыло в отделение");
            movementHistoryRepository.save(historyEntry);

            logger.info("Почтовый элемент успешно обновлен.");
            logger.info(" ");
            return ResponseEntity.ok(updatedMailItem);
        } else {
            logger.info("Почтовый отправление или почтовое отделение не найдено.");
            logger.info(" ");

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/departure")
    public ResponseEntity<MailItem> updateMailDeparture(@RequestBody MailDepartureRequest request) {
        Long mailItemId = request.getMailItemId();

        logger.info("Получен запрос на убытие из почтового отделения.");
        logger.info("Идентификатор почтового отправления: {}", mailItemId);

        MailItem updatedMailItem = mailService.updateDepartureFromPostOffice(mailItemId);

        if (updatedMailItem != null) {
            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(updatedMailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Покинуло отделение");
            historyEntry.setStatus("Отправлено из отделения");
            movementHistoryRepository.save(historyEntry);

            logger.info("Почтовый элемент успешно обновлен.");
            logger.info(" ");
            return ResponseEntity.ok(updatedMailItem);
        } else {
            logger.info("Почтовое отправление не найдено.");
            logger.info(" ");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/receive")
    public ResponseEntity<MailItem> receiveMail(@RequestBody MailItemIdWrapper wrapper) {
        Long mailItemId = wrapper.getMailItemId();

        logger.info("Получен запрос на получение почты.");
        logger.info("Идентификатор почтового отправления: {}", mailItemId);

        MailItem receivedMailItem = mailService.updateReceivedStatus(mailItemId);

        if (receivedMailItem != null) {
            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(receivedMailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Адресат получил отправление");
            historyEntry.setStatus("Доставлено адресату");
            movementHistoryRepository.save(historyEntry);

            logger.info("Почтовый элемент успешно получен.");
            logger.info(" ");
            return ResponseEntity.ok(receivedMailItem);
        } else {
            logger.info("Почтовое отправление не найдено.");
            logger.info(" ");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/history")
    public ResponseEntity<List<MovementHistory>> getFullHistory(@RequestBody Map<String, Long> request) {
        Long mailItemId = request.get("mailItemId");

        MailItem mailItem = mailService.findMailItemById(mailItemId);

        if (mailItem == null) {
            logger.info("Почтовый элемент не найден");
            logger.info(" ");
            return ResponseEntity.notFound().build();
        }

        List<MovementHistory> history = movementHistoryService.getFullHistory(mailItem);

        if (history.isEmpty()) {
            logger.info("История пуста");
            logger.info(" ");
        } else {
            logger.info("История включает: {} запись(и)", history.size());
            for (MovementHistory entry : history) {
                logger.info("Время и дата: {}, Уведомление: {}, Статус: {}",
                        entry.getTimestamp(), entry.getLocation(), entry.getStatus());
            }
            logger.info(" ");
        }
        return ResponseEntity.ok(history);
    }
}
