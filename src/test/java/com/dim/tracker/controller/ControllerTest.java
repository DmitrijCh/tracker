package com.dim.tracker.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dim.tracker.entity.MovementHistory;
import com.dim.tracker.request.MailArrivalRequest;
import com.dim.tracker.request.MailDepartureRequest;
import com.dim.tracker.request.MailItemIdWrapperRequest;
import com.dim.tracker.service.MailService;
import com.dim.tracker.service.MovementHistoryService;
import com.dim.tracker.entity.MailItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;;

public class ControllerTest {

    private Controller mailController;
    private MailService mailService;
    private MovementHistoryService movementHistoryService;

    @BeforeEach
    public void setUp() {
        // Создаем мок типа MailService, MovementHistoryService
        mailService = mock(MailService.class);
        movementHistoryService = mock(MovementHistoryService.class);
        // Создаем объект Controller, передавая ему созданные моки в качестве зависимостей.
        mailController = new Controller(mailService);
    }

    @Test
    public void testRegisterMail() {
        // Создаем объект MailItem, представляющий почтовый элемент.
        MailItem mailItem = new MailItem();
        mailItem.setType("Тип почты");
        mailItem.setRecipientIndex("Индекс получателя");
        mailItem.setRecipientAddress("Адрес получателя");
        mailItem.setRecipientName("Имя получателя");

        // Создаем фиктивный зарегистрированный MailItem с установленным идентификатором.
        MailItem registeredMailItem = new MailItem();
        registeredMailItem.setId(1L);

        // Устанавливаем поведение мок-объекта mailService: при вызове registerMailItem(mailItem)
        // должен быть возвращен зарегистрированный MailItem.
        when(mailService.registerMailItem(mailItem)).thenReturn(registeredMailItem);

        // Вызываем метод registerMail контроллера и сохраняем результат в объект response.
        ResponseEntity<?> response = mailController.registerMail(mailItem);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что метод registerMailItem у mailService был вызван ровно один раз
        // с mailItem в качестве аргумента.
        verify(mailService, times(1)).registerMailItem(mailItem);
    }

    @Test
    public void testUpdateMailArrival() {
        // Создаем объект MailArrivalRequest, представляющий запрос на обновление прибытия почты.
        MailArrivalRequest request = new MailArrivalRequest();
        request.setMailItemId(1L); // Устанавливаем идентификатор почтового элемента.
        request.setPostOfficeId(2L); // Устанавливаем идентификатор почтового отделения.

        // Создаем объект MailItem с установленным идентификатором.
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        // Устанавливаем поведение мок-объекта mailService: при вызове updateCurrentPostOffice(1L, 2L)
        // должен быть возвращен объект mailItem.
        when(mailService.updateCurrentPostOffice(1L, 2L)).thenReturn(mailItem);

        // Вызываем метод updateMailArrival контроллера и сохраняем результат в объект response.
        ResponseEntity<?> response = mailController.updateMailArrival(request);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что метод updateCurrentPostOffice у mailService был вызван ровно один раз
        // с аргументами 1L и 2L.
        verify(mailService, times(1)).updateCurrentPostOffice(1L, 2L);
    }

    @Test
    public void testUpdateMailDeparture() {
        // Создаем объект MailDepartureRequest, представляющий запрос на обновление отправки почты.
        MailDepartureRequest request = new MailDepartureRequest();
        request.setMailItemId(1L); // Устанавливаем идентификатор почтового элемента.

        // Создаем объект MailItem с установленным идентификатором.
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        // Устанавливаем поведение мок-объекта mailService: при вызове updateDepartureFromPostOffice(1L)
        // должен быть возвращен объект mailItem.
        when(mailService.updateDepartureFromPostOffice(1L)).thenReturn(mailItem);

        // Вызываем метод updateMailDeparture контроллера и сохраняем результат в объект response.
        ResponseEntity<?> response = mailController.updateMailDeparture(request);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что метод updateDepartureFromPostOffice у mailService был вызван ровно один раз
        // с аргументом 1L.
        verify(mailService, times(1)).updateDepartureFromPostOffice(1L);
    }

    @Test
    public void testReceiveMail() {
        // Создаем объект MailItemIdWrapper, содержащий идентификатор почтового элемента.
        MailItemIdWrapperRequest wrapper = new MailItemIdWrapperRequest();
        wrapper.setMailItemId(1L); // Устанавливаем идентификатор почтового элемента.

        // Создаем объект MailItem с установленным идентификатором.
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        // Устанавливаем поведение мок-объекта mailService: при вызове updateReceivedStatus(1L)
        // должен быть возвращен объект mailItem.
        when(mailService.updateReceivedStatus(1L)).thenReturn(mailItem);

        // Вызываем метод receiveMail контроллера и сохраняем результат в объект response.
        ResponseEntity<MailItem> response = (ResponseEntity<MailItem>) mailController.receiveMail(wrapper);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что метод updateReceivedStatus у mailService был вызван ровно один раз
        // с аргументом 1L.
        verify(mailService, times(1)).updateReceivedStatus(1L);
    }

    @Test
    public void testGetFullHistory() {
        // Создаем запрос в виде Map, содержащий идентификатор почтового элемента.
        Map<String, Long> request = Collections.singletonMap("mailItemId", 1L);

        // Создаем объект MailItem с установленным идентификатором.
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        // Создаем объект MovementHistory, представляющий историю перемещения.
        MovementHistory historyEntry = new MovementHistory();
        historyEntry.setTimestamp(LocalDateTime.now());
        historyEntry.setLocation("Локация");
        historyEntry.setStatus("Статус");
        List<MovementHistory> history = new ArrayList<>();
        history.add(historyEntry);

        // Устанавливаем поведение мок-объектов:
        // При вызове findMailItemById(1L) должен быть возвращен объект mailItem.
        // При вызове getFullHistory(mailItem) должен быть возвращен список history.
        when(mailService.findMailItemById(anyLong())).thenReturn(history);

        // Вызываем метод getFullHistory контроллера, передавая запрос, и сохраняем результат в объект response.
        ResponseEntity<List<MovementHistory>> response = (ResponseEntity<List<MovementHistory>>) mailController.getFullHistory(request);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что метод findMailItemById был вызван ровно один раз с аргументом 1L.
        // Проверяем, что метод getFullHistory был вызван ровно один раз с аргументом mailItem.
        verify(mailService, times(1)).findMailItemById(1L);
    }

    @Test
    public void testGetFullHistoryNotFound() {
        // Создаем запрос в виде Map, содержащий идентификатор почтового элемента.
        Map<String, Long> request = Collections.singletonMap("mailItemId", 1L);

        // Устанавливаем поведение мок-объекта:
        // При вызове findMailItemById(1L) должен быть возвращен null (почтовый элемент не найден).
        when(mailService.findMailItemById(1L)).thenReturn(null);

        // Вызываем метод getFullHistory контроллера, передавая запрос, и сохраняем результат в объект response.
        ResponseEntity<List<MovementHistory>> response = (ResponseEntity<List<MovementHistory>>) mailController.getFullHistory(request);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.NOT_FOUND (почтовый элемент не найден).
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFullHistoryEmptyHistory() {
        // Создаем запрос в виде Map, содержащий идентификатор почтового элемента.
        Map<String, Long> request = Collections.singletonMap("mailItemId", 1L);

        // Создаем объект MailItem с установленным идентификатором.
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        // Устанавливаем поведение мок-объектов:
        // При вызове findMailItemById(1L) должен быть возвращен объект mailItem.
        // При вызове getFullHistory(mailItem) должен быть возвращен пустой список (история пуста).
        when(mailService.findMailItemById(1L)).thenReturn(Collections.emptyList());
        when(movementHistoryService.getFullHistory(mailItem)).thenReturn(Collections.emptyList());

        // Вызываем метод getFullHistory контроллера, передавая запрос, и сохраняем результат в объект response.
        ResponseEntity<List<MovementHistory>> response = (ResponseEntity<List<MovementHistory>>) mailController.getFullHistory(request);

        // Проверяем, что HTTP-статус ответа равен HttpStatus.OK.
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем, что результат содержит пустой список (история пуста).
        assertEquals(Collections.emptyList(), response.getBody());
    }
}


