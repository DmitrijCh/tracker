package com.dmitrijch.tracker.controller;

import com.dmitrijch.tracker.entity.MailItem;
import com.dmitrijch.tracker.entity.MovementHistory;
import com.dmitrijch.tracker.request.HistoryRequest;
import com.dmitrijch.tracker.request.MailArrivalRequest;
import com.dmitrijch.tracker.request.MailDepartureRequest;
import com.dmitrijch.tracker.request.MailItemIdWrapperRequest;
import com.dmitrijch.tracker.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControllerTest {

    private Controller mailController;
    private MailService mailService;

    @BeforeEach
    public void setUp() {
        mailService = mock(MailService.class);
        mailController = new Controller(mailService);
    }

    @Test
    public void testRegisterMail() {
        MailItem mailItem = new MailItem();
        mailItem.setType("письмо");
        mailItem.setRecipientIndex("Индекс получателя");
        mailItem.setRecipientAddress("Адрес получателя");
        mailItem.setRecipientName("Имя получателя");

        MailItem registeredMailItem = new MailItem();
        registeredMailItem.setId(1L);

        when(mailService.registerMailItem(mailItem)).thenReturn(registeredMailItem);

        ResponseEntity<?> response = mailController.registerMail(mailItem);

        assertEquals(HttpStatus.OK, response.getStatusCode());


        verify(mailService, times(1)).registerMailItem(mailItem);
    }

    @Test
    public void testUpdateMailArrival() {
        MailArrivalRequest request = new MailArrivalRequest();
        request.setMailItemId(1L);
        request.setPostOfficeId(2L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.updateCurrentPostOffice(1L, 2L)).thenReturn(mailItem);

        ResponseEntity<?> response = mailController.updateMailArrival(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mailService, times(1)).updateCurrentPostOffice(1L, 2L);
    }

    @Test
    public void testUpdateMailDeparture() {
        MailDepartureRequest request = new MailDepartureRequest();
        request.setMailItemId(1L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.updateDepartureFromPostOffice(1L)).thenReturn(mailItem);

        ResponseEntity<?> response = mailController.updateMailDeparture(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mailService, times(1)).updateDepartureFromPostOffice(1L);
    }

    @Test
    public void testReceiveMail() {
        MailItemIdWrapperRequest wrapper = new MailItemIdWrapperRequest();
        wrapper.setMailItemId(1L);

        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        when(mailService.updateReceivedStatus(1L)).thenReturn(mailItem);

        ResponseEntity<?> response = mailController.receiveMail(wrapper);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mailService, times(1)).updateReceivedStatus(1L);
    }

    @Test
    public void testGetFullHistory() {
        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setMailItemId(1L);

        MovementHistory historyEntry = new MovementHistory();
        historyEntry.setTimestamp(LocalDateTime.now());
        historyEntry.setLocation("Локация");
        historyEntry.setStatus("Статус");
        List<MovementHistory> history = new ArrayList<>();
        history.add(historyEntry);

        when(mailService.findMailItemById(anyLong())).thenReturn(history);

        ResponseEntity<?> response = mailController.getFullHistory(historyRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mailService, times(1)).findMailItemById(1L);
    }
}