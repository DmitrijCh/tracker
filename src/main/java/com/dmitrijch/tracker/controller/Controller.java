package com.dmitrijch.tracker.controller;

import com.dmitrijch.tracker.entity.MailItem;
import com.dmitrijch.tracker.entity.MovementHistory;
import com.dmitrijch.tracker.request.HistoryRequest;
import com.dmitrijch.tracker.request.MailArrivalRequest;
import com.dmitrijch.tracker.request.MailDepartureRequest;
import com.dmitrijch.tracker.request.MailItemIdWrapperRequest;
import com.dmitrijch.tracker.response.ErrorResponse;
import com.dmitrijch.tracker.service.MailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mail")
public class Controller {
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    public Controller(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMail(@RequestBody @Valid MailItem mailItem) {
        logger.info("Поступил запрос на регистрацию почты:");
        MailItem registeredMailItem = mailService.registerMailItem(mailItem);
        return ResponseEntity.ok(registeredMailItem);
    }

    @PostMapping("/arrival")
    public ResponseEntity<?> updateMailArrival(@RequestBody @Valid MailArrivalRequest request) {
        Long mailItemId = request.getMailItemId();
        Long postOfficeId = request.getPostOfficeId();
        MailItem updatedMailItem = mailService.updateCurrentPostOffice(mailItemId, postOfficeId);

        if (updatedMailItem != null) {
            logger.info("Почтовый элемент успешно обновлен.");
            return ResponseEntity.ok(updatedMailItem);
        } else {
            String errorMessage = "Почтовый отправление или почтовое отделение не найдено.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/departure")
    public ResponseEntity<?> updateMailDeparture(@RequestBody @Valid MailDepartureRequest request) {
        Long mailItemId = request.getMailItemId();
        MailItem updatedMailItem = mailService.updateDepartureFromPostOffice(mailItemId);

        if (updatedMailItem != null) {
            logger.info("Почтовый элемент успешно обновлен.");
            return ResponseEntity.ok(updatedMailItem);
        } else {
            String errorMessage = "Почтовое отправление не найдено.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receiveMail(@RequestBody @Valid MailItemIdWrapperRequest wrapper) {
        Long mailItemId = wrapper.getMailItemId();
        MailItem receivedMailItem = mailService.updateReceivedStatus(mailItemId);

        if (receivedMailItem != null) {
            logger.info("Почтовый элемент успешно получен.");
            return ResponseEntity.ok(receivedMailItem);
        } else {
            String errorMessage = "Почтовое отправление не найдено.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/history")
    public ResponseEntity<?> getFullHistory(@RequestBody @Valid HistoryRequest request) {
        Long mailItemId = request.getMailItemId();
        List<MovementHistory> history = mailService.findMailItemById(mailItemId);

        if (history == null) {
            String errorMessage = "Почтовый элемент не найден";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (history.isEmpty()) {
            String errorMessage = "История пуста";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            logger.info("История включает: {} запись(и)", history.size());
            for (MovementHistory entry : history) {
                logger.info("Время и дата: {}, Уведомление: {}, Статус: {}",
                        entry.getTimestamp(), entry.getLocation(), entry.getStatus());
            }
        }
        return ResponseEntity.ok(history);
    }
}