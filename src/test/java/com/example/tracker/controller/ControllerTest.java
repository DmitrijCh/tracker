package com.example.tracker.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.tracker.entity.*;
import com.example.tracker.repository.MovementHistoryRepository;
import com.example.tracker.service.MailService;
import com.example.tracker.service.MovementHistoryService;
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
    private MovementHistoryRepository movementHistoryRepository;
    private MovementHistoryService movementHistoryService;

    @BeforeEach
    public void setUp() {
        mailService = mock(MailService.class);
        movementHistoryRepository = mock(MovementHistoryRepository.class);
        movementHistoryService = mock(MovementHistoryService.class);
        mailController = new Controller(mailService, movementHistoryService, movementHistoryRepository);
    }

    @Test
    public void testRegisterMail() {
        MailItem mailItem = new MailItem();
        mailItem.setType("Тип почты");
        mailItem.setRecipientIndex("Индекс получателя");
        mailItem.setRecipientAddress("Адрес получателя");
        mailItem.setRecipientName("Имя получателя");

        MailItem registeredMailItem = new MailItem();
        registeredMailItem.setId(1L);

        when(mailService.registerMailItem(mailItem)).thenReturn(registeredMailItem);

        ResponseEntity<MailItem> response = mailController.registerMail(mailItem);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(movementHistoryRepository, times(1)).save(any(MovementHistory.class));
    }

    @Test
    public void testUpdateMailArrival() {
        MailArrivalRequest request = new MailArrivalRequest();
        request.setMailItemId(1L);
        request.setPostOfficeId(2L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.updateCurrentPostOffice(1L, 2L)).thenReturn(mailItem);

        ResponseEntity<MailItem> response = mailController.updateMailArrival(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(movementHistoryRepository, times(1)).save(any(MovementHistory.class));
    }

    @Test
    public void testUpdateMailDeparture() {
        MailDepartureRequest request = new MailDepartureRequest();
        request.setMailItemId(1L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.updateDepartureFromPostOffice(1L)).thenReturn(mailItem);

        ResponseEntity<MailItem> response = mailController.updateMailDeparture(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(movementHistoryRepository, times(1)).save(any(MovementHistory.class));
    }

    @Test
    public void testReceiveMail() {
        MailItemIdWrapper wrapper = new MailItemIdWrapper();
        wrapper.setMailItemId(1L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.updateReceivedStatus(1L)).thenReturn(mailItem);

        ResponseEntity<MailItem> response = mailController.receiveMail(wrapper);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(movementHistoryRepository, times(1)).save(any(MovementHistory.class));
    }

    @Test
    public void testGetFullHistory() {
        Map<String, Long> request = Collections.singletonMap("mailItemId", 1L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        MovementHistory historyEntry = new MovementHistory();
        historyEntry.setTimestamp(LocalDateTime.now());
        historyEntry.setLocation("Локация");
        historyEntry.setStatus("Статус");
        List<MovementHistory> history = new ArrayList<>();
        history.add(historyEntry);

        when(mailService.findMailItemById(1L)).thenReturn(mailItem);
        when(movementHistoryService.getFullHistory(mailItem)).thenReturn(history);

        ResponseEntity<List<MovementHistory>> response = mailController.getFullHistory(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mailService, times(1)).findMailItemById(1L);
        verify(movementHistoryService, times(1)).getFullHistory(mailItem);
    }

    @Test
    public void testGetFullHistoryNotFound() {
        Map<String, Long> request = Collections.singletonMap("mailItemId", 1L);

        when(mailService.findMailItemById(1L)).thenReturn(null);

        ResponseEntity<List<MovementHistory>> response = mailController.getFullHistory(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFullHistoryEmptyHistory() {
        Map<String, Long> request = Collections.singletonMap("mailItemId", 1L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.findMailItemById(1L)).thenReturn(mailItem);
        when(movementHistoryService.getFullHistory(mailItem)).thenReturn(Collections.emptyList());

        ResponseEntity<List<MovementHistory>> response = mailController.getFullHistory(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }
}


