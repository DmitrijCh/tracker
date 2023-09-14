package com.dim.tracker.controller;

import com.dim.tracker.entity.MovementHistory;
import com.dim.tracker.request.MailArrivalRequest;
import com.dim.tracker.request.MailDepartureRequest;
import com.dim.tracker.request.MailItemIdWrapperRequest;
import com.dim.tracker.response.ErrorResponse;
import com.dim.tracker.service.MailService;
import com.dim.tracker.entity.MailItem;
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

        logger.info("");
        logger.info("Поступил запрос на регистрацию почты:");
        logger.info("");

        MailItem registeredMailItem = mailService.registerMailItem(mailItem);
        return ResponseEntity.ok(registeredMailItem);
    }

    @PostMapping("/arrival")
    public ResponseEntity<?> updateMailArrival(@RequestBody MailArrivalRequest request) {
        if (request == null || request.getMailItemId() == null ||
                Objects.isNull(request.getMailItemId()) ||
                Objects.isNull(request.getPostOfficeId())) {
            String errorMessage = "Неверные данные для регистрации почты.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long mailItemId = request.getMailItemId();
        Long postOfficeId = request.getPostOfficeId();

        logger.info("Получен запрос на обновление прибытия почты.");
        logger.info("Идентификатор почтового отправления: {}", mailItemId);
        logger.info("Идентификатор почтового отделения: {}", postOfficeId);

        MailItem updatedMailItem = mailService.updateCurrentPostOffice(mailItemId, postOfficeId);

        if (updatedMailItem != null) {
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
    public ResponseEntity<?> updateMailDeparture(@RequestBody MailDepartureRequest request) {
        if (request == null || request.getMailItemId() == null ||
                Objects.isNull(request.getMailItemId())) {
            String errorMessage = "Неверные данные для регистрации почты.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long mailItemId = request.getMailItemId();

        logger.info("Получен запрос на убытие из почтового отделения.");
        logger.info("Идентификатор почтового отправления: {}", mailItemId);

        MailItem updatedMailItem = mailService.updateDepartureFromPostOffice(mailItemId);

        if (updatedMailItem != null) {
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
    public ResponseEntity<?> receiveMail(@RequestBody MailItemIdWrapperRequest wrapper) {
        if (wrapper == null || wrapper.getMailItemId() == null ||
                Objects.isNull(wrapper.getMailItemId())) {
            String errorMessage = "Неверные данные для регистрации почты.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long mailItemId = wrapper.getMailItemId();

        logger.info("Получен запрос на получение почты.");
        logger.info("Идентификатор почтового отправления: {}", mailItemId);

        MailItem receivedMailItem = mailService.updateReceivedStatus(mailItemId);

        if (receivedMailItem != null) {
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
    public ResponseEntity<?> getFullHistory(@RequestBody(required = false) Map<String, Long> request) {
        if (request == null || request.get("mailItemId") == null || Objects.isNull(request.get("mailItemId"))) {
            String errorMessage = "Неверные данные для регистрации почты.";
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long mailItemId = request.get("mailItemId");

        List<MovementHistory> history = mailService.findMailItemById(mailItemId);

        if (history == null) {
            logger.info("Почтовый элемент не найден");
            logger.info(" ");
            return ResponseEntity.notFound().build();
        }

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
