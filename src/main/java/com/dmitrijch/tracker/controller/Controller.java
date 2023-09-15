package com.dmitrijch.tracker.controller;

import com.dmitrijch.tracker.entity.MovementHistory;
import com.dmitrijch.tracker.request.MailArrivalRequest;
import com.dmitrijch.tracker.request.MailDepartureRequest;
import com.dmitrijch.tracker.request.MailItemIdWrapperRequest;
import com.dmitrijch.tracker.response.ErrorResponse;
import com.dmitrijch.tracker.service.MailService;
import com.dmitrijch.tracker.entity.MailItem;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public ResponseEntity<?> registerMail(@RequestBody MailItem mailItem) {
        if (mailItem == null ||
                (mailItem.getType() == null || mailItem.getType().isEmpty()) ||
                (mailItem.getRecipientIndex() == null || mailItem.getRecipientIndex().isEmpty()) ||
                (mailItem.getRecipientAddress() == null || mailItem.getRecipientAddress().isEmpty()) ||
                (mailItem.getRecipientName() == null || mailItem.getRecipientName().isEmpty())) {

            String errorMessage = "Неверные данные для регистрации почты. Убедитесь, что все обязательные поля заполнены.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        String type = mailItem.getType().toLowerCase();
        if (!type.equals("письмо") && !type.equals("посылка") && !type.equals("бандероль") && !type.equals("открытка")) {
            String errorMessage = "Неверное значение поля 'тип'. Поддерживаемые значения: письмо, посылка, бандероль, открытка.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        logger.info("Поступил запрос на регистрацию почты:");

        MailItem registeredMailItem = mailService.registerMailItem(mailItem);
        return ResponseEntity.ok(registeredMailItem);
    }

    @PostMapping("/arrival")
    public ResponseEntity<?> updateMailArrival(@RequestBody MailArrivalRequest request) {
        if (request == null || request.getMailItemId() == null ||
                Objects.isNull(request.getMailItemId()) ||
                Objects.isNull(request.getPostOfficeId())) {
            String errorMessage = "Неверные данные для обновления информации о прибытии почтового отправления." +
                    " Убедитесь, что все обязательные поля заполнены.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

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
    public ResponseEntity<?> updateMailDeparture(@RequestBody MailDepartureRequest request) {
        if (request == null || request.getMailItemId() == null ||
                Objects.isNull(request.getMailItemId())) {
            String errorMessage = "Неверные данные для обновления информации о отправке почтового отправления. " +
                    "Убедитесь, что все обязательные поля заполнены.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

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
    public ResponseEntity<?> receiveMail(@RequestBody MailItemIdWrapperRequest wrapper) {
        if (wrapper == null || wrapper.getMailItemId() == null ||
                Objects.isNull(wrapper.getMailItemId())) {
            String errorMessage = "Неверные данные для обновления статуса получения почтового отправления. " +
                    "Убедитесь, что все обязательные поля заполнены.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

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
    public ResponseEntity<?> getFullHistory(@RequestBody(required = false) Map<String, Long> request) {
        if (request == null || request.get("mailItemId") == null || Objects.isNull(request.get("mailItemId"))) {
            String errorMessage = "Неверные данные для запроса истории перемещения почтового отправления. " +
                    "Убедитесь, что все обязательные поля заполнены.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long mailItemId = request.get("mailItemId");
        List<MovementHistory> history = mailService.findMailItemById(mailItemId);

        if (history == null) {
            String errorMessage = "Почтовый элемент не найден";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (history.isEmpty()) {
            String errorMessage = ("История пуста");
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