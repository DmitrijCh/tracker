package com.dmitrijch.tracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class MovementHistoryTest {

    @Test
    public void testGetAndSetId() {
        MovementHistory history = new MovementHistory();
        history.setId(1L);
        assertEquals(1L, history.getId());
    }

    @Test
    public void testGetAndSetMailItem() {
        MovementHistory history = new MovementHistory();
        MailItem mailItem = new MailItem();
        history.setMailItem(mailItem);
        assertEquals(mailItem, history.getMailItem());
    }

    @Test
    public void testGetAndSetTimestamp() {
        MovementHistory history = new MovementHistory();
        LocalDateTime timestamp = LocalDateTime.now();
        history.setTimestamp(timestamp);
        assertEquals(timestamp, history.getTimestamp());
    }

    @Test
    public void testGetAndSetLocation() {
        MovementHistory history = new MovementHistory();
        history.setLocation("Location A");
        assertEquals("Location A", history.getLocation());
    }

    @Test
    public void testGetAndSetStatus() {
        MovementHistory history = new MovementHistory();
        history.setStatus("Status XYZ");
        assertEquals("Status XYZ", history.getStatus());
    }
}

