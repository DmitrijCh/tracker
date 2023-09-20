package com.dmitrijch.tracker.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailItemIdWrapperRequestTest {

    @Test
    public void testGetSetMailItemId() {
        MailItemIdWrapperRequest wrapper = new MailItemIdWrapperRequest();
        wrapper.setMailItemId(1L);
        assertEquals(1L, wrapper.getMailItemId());
    }
}