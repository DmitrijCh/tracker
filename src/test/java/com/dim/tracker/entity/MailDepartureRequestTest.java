package com.dim.tracker.entity;

import com.dim.tracker.request.MailDepartureRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailDepartureRequestTest {

    @Test
    public void testGetSetMailItemId() {
        MailDepartureRequest request = new MailDepartureRequest();
        request.setMailItemId(1L);
        assertEquals(1L, request.getMailItemId());
    }
}

