package com.example.tracker.controller;

import com.example.tracker.entity.*;
import com.example.tracker.repository.MovementHistoryRepository;
import com.example.tracker.service.MailService;
import com.example.tracker.service.MovementHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class Controller {
    private final MailService mailService;
    private final MovementHistoryService movementHistoryService;
    private final MovementHistoryRepository movementHistoryRepository;

    @Autowired
    public Controller(MailService mailService, MovementHistoryService movementHistoryService, MovementHistoryRepository movementHistoryRepository) {
        this.mailService = mailService;
        this.movementHistoryService = movementHistoryService;
        this.movementHistoryRepository = movementHistoryRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<MailItem> registerMail(@RequestBody MailItem mailItem) {
        System.out.println("Поступил запрос на регистрацию почты: ");
        System.out.println(" ");

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

        System.out.println("Получен запрос на обновление прибытия почты.");
        System.out.println("Идентификатор почтового отправления: " + mailItemId);
        System.out.println("Идентификатор почтового отделения: " + postOfficeId);

        MailItem updatedMailItem = mailService.updateCurrentPostOffice(mailItemId, postOfficeId);

        if (updatedMailItem != null) {
            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(updatedMailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Промежуточное отделение");
            historyEntry.setStatus("Прибыло в отделение");
            movementHistoryRepository.save(historyEntry);

            System.out.println("Почтовый элемент успешно обновлен.");
            System.out.println(" ");
            return ResponseEntity.ok(updatedMailItem);
        } else {
            System.out.println("Почтовый отправление или почтовое отделение не найдено.");
            System.out.println(" ");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/departure")
    public ResponseEntity<MailItem> updateMailDeparture(@RequestBody MailDepartureRequest request) {
        Long mailItemId = request.getMailItemId();

        System.out.println("Получен запрос на убытие из почтового отделения.");
        System.out.println("Идентификатор почтового отправления: " + mailItemId);

        MailItem updatedMailItem = mailService.updateDepartureFromPostOffice(mailItemId);

        if (updatedMailItem != null) {
            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(updatedMailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Покинуло отделение");
            historyEntry.setStatus("Отправлено из отделения");
            movementHistoryRepository.save(historyEntry);

            System.out.println("Почтовый элемент успешно обновлен.");
            System.out.println(" ");
            return ResponseEntity.ok(updatedMailItem);
        } else {
            System.out.println("Почтовое отправление не найдено.");
            System.out.println(" ");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/receive")
    public ResponseEntity<MailItem> receiveMail(@RequestBody MailItemIdWrapper wrapper) {
        Long mailItemId = wrapper.getMailItemId();

        System.out.println("Получен запрос на получение почты.");
        System.out.println("Идентификатор почтового отправления: " + mailItemId);

        MailItem receivedMailItem = mailService.updateReceivedStatus(mailItemId);

        if (receivedMailItem != null) {
            MovementHistory historyEntry = new MovementHistory();
            historyEntry.setMailItem(receivedMailItem);
            historyEntry.setTimestamp(LocalDateTime.now());
            historyEntry.setLocation("Адресат получил отправление");
            historyEntry.setStatus("Доставлено адресату");
            movementHistoryRepository.save(historyEntry);

            System.out.println("Почтовый элемент успешно получен.");
            System.out.println(" ");
            return ResponseEntity.ok(receivedMailItem);
        } else {
            System.out.println("Почтовое отправление не найдено.");
            System.out.println(" ");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/history")
    public ResponseEntity<List<MovementHistory>> getFullHistory(@RequestBody Map<String, Long> request) {
        Long mailItemId = request.get("mailItemId");

        MailItem mailItem = mailService.findMailItemById(mailItemId);

        if (mailItem == null) {
            System.out.println("Почтовый элемент не найден");
            System.out.println(" ");
            return ResponseEntity.notFound().build();
        }

        List<MovementHistory> history = movementHistoryService.getFullHistory(mailItem);

        if (history.isEmpty()) {
            System.out.println("История пуста");
            System.out.println(" ");
        } else {
            System.out.println("История включает: " + history.size() + " запись(и)");
            for (MovementHistory entry : history) {
                System.out.println("Время и дата: " + entry.getTimestamp() +
                        ", Уведомление: " + entry.getLocation() +
                        ", Статус: " + entry.getStatus());
            }
            System.out.println(" ");
        }

        return ResponseEntity.ok(history);
    }
}
