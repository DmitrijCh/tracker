package com.example.tracker.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.tracker.entity.MailItem;
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

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

