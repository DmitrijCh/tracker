package com.example.tracker.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.tracker.entity.*;
import com.example.tracker.repository.MovementHistoryRepository;
import com.example.tracker.service.MailService;
import com.example.tracker.service.MovementHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;


@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailService mailService;

    @MockBean
    private MovementHistoryService movementHistoryService;

    @MockBean
    private MovementHistoryRepository movementHistoryRepository;

    @Test
    public void testRegisterMail() throws Exception {
        MailItem mailItem = new MailItem();
        mailItem.setType("Тип почты");
        mailItem.setRecipientIndex("Индекс получателя");
        mailItem.setRecipientAddress("Адрес получателя");
        mailItem.setRecipientName("Имя получателя");

        when(mailService.registerMailItem(mailItem)).thenReturn(mailItem);

        mockMvc.perform(post("/api/mail/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mailItem)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMailArrival() throws Exception {
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        MailArrivalRequest arrivalRequest = new MailArrivalRequest();
        arrivalRequest.setMailItemId(mailItem.getId());
        arrivalRequest.setPostOfficeId(2L);

        when(mailService.updateCurrentPostOffice(mailItem.getId(), arrivalRequest.getPostOfficeId()))
                .thenReturn(mailItem);

        mockMvc.perform(post("/api/mail/arrival")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(arrivalRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMailDeparture() throws Exception {
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        MailDepartureRequest departureRequest = new MailDepartureRequest();
        departureRequest.setMailItemId(mailItem.getId());

        when(mailService.updateDepartureFromPostOffice(mailItem.getId()))
                .thenReturn(mailItem);

        mockMvc.perform(post("/api/mail/departure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(departureRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testReceiveMail() throws Exception {
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);

        MailItemIdWrapper itemIdWrapper = new MailItemIdWrapper();
        itemIdWrapper.setMailItemId(mailItem.getId());

        when(mailService.updateReceivedStatus(mailItem.getId()))
                .thenReturn(mailItem);

        mockMvc.perform(post("/api/mail/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemIdWrapper)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFullHistory() throws Exception {
        Long mailItemId = 1L; // Здесь используйте реальный ID
        MailItem mailItem = new MailItem();
        mailItem.setId(mailItemId);

        MovementHistory historyEntry = new MovementHistory();
        historyEntry.setTimestamp(LocalDateTime.parse("2023-08-28T16:33:45.699657"));
        historyEntry.setLocation("Post Office A");
        historyEntry.setStatus("In Transit");

        List<MovementHistory> historyList = new ArrayList<>();
        historyList.add(historyEntry);

        when(mailService.findMailItemById(mailItemId)).thenReturn(mailItem);
        when(movementHistoryService.getFullHistory(mailItem)).thenReturn(historyList);

        Map<String, Long> request = new HashMap<>();
        request.put("mailItemId", mailItemId);

        mockMvc.perform(post("/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timestamp").value("2023-08-29 14:30:00"))
                .andExpect(jsonPath("$[0].location").value("Post Office A"))
                .andExpect(jsonPath("$[0].status").value("In Transit"));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

