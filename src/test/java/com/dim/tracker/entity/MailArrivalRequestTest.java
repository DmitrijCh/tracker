package com.dim.tracker.entity;

import com.dim.tracker.request.MailArrivalRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailArrivalRequestTest {

    @Test
    public void testGetSetMailItemId() {
        MailArrivalRequest request = new MailArrivalRequest();
        request.setMailItemId(1L);
        assertEquals(1L, request.getMailItemId());
    }

    @Test
    public void testGetSetPostOfficeId() {
        MailArrivalRequest request = new MailArrivalRequest();
        request.setPostOfficeId(2L);
        assertEquals(2L, request.getPostOfficeId());
    }
}


