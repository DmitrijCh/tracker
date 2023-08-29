package com.example.tracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class MovementHistoryTest {

    @Test
    public void testGetSetId() {
        MovementHistory history = new MovementHistory();
        history.setId(1L);
        assertEquals(1L, history.getId());
    }

    @Test
    public void testGetSetMailItem() {
        MovementHistory history = new MovementHistory();
        MailItem mailItem = mock(MailItem.class); // Мокирование MailItem для теста
        history.setMailItem(mailItem);
        assertEquals(mailItem, history.getMailItem());
    }

    @Test
    public void testGetSetTimestamp() {
        MovementHistory history = new MovementHistory();
        LocalDateTime timestamp = LocalDateTime.now();
        history.setTimestamp(timestamp);
        assertEquals(timestamp, history.getTimestamp());
    }

    @Test
    public void testGetSetLocation() {
        MovementHistory history = new MovementHistory();
        history.setLocation("Location A");
        assertEquals("Location A", history.getLocation());
    }

    @Test
    public void testGetSetStatus() {
        MovementHistory history = new MovementHistory();
        history.setStatus("Status XYZ");
        assertEquals("Status XYZ", history.getStatus());
    }
}

