package com.dmitrijch.tracker.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryRequestTest {

    @Test
    public void testGetSetMailItemId() {
        HistoryRequest request = new HistoryRequest();
        request.setMailItemId(1L);
        assertEquals(1L, request.getMailItemId());
    }
}