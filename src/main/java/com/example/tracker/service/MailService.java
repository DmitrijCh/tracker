package com.example.tracker.service;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.PostOffice;
import com.example.tracker.repository.MailItemRepository;
import com.example.tracker.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final MailItemRepository mailItemRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Autowired
    public MailService(MailItemRepository mailItemRepository, PostOfficeRepository postOfficeRepository) {
        this.mailItemRepository = mailItemRepository;
        this.postOfficeRepository = postOfficeRepository;
    }

    public MailItem registerMailItem(MailItem mailItem) {
        return mailItemRepository.save(mailItem);
    }

    public MailItem updateCurrentPostOffice(Long mailItemId, Long postOfficeId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);
        PostOffice postOffice = postOfficeRepository.findById(postOfficeId).orElse(null);

        if (mailItem != null && postOffice != null) {
            mailItem.setCurrentPostOffice(postOffice);
            return mailItemRepository.save(mailItem);
        }
        return null;
    }

    public MailItem updateDepartureFromPostOffice(Long mailItemId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);

        if (mailItem != null) {
            mailItem.setCurrentPostOffice(null);
            return mailItemRepository.save(mailItem);
        } else {
            return null;
        }
    }

    public MailItem updateReceivedStatus(Long mailItemId) {
        MailItem mailItem = mailItemRepository.findById(mailItemId).orElse(null);

        if (mailItem != null) {
            mailItem.setReceived(true);
            return mailItemRepository.save(mailItem);
        }

        return null;
    }


    public MailItem findMailItemById(Long mailItemId) {
        return mailItemRepository.findById(mailItemId).orElse(null);
    }
}
