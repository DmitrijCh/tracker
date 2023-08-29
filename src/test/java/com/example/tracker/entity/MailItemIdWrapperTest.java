package com.example.tracker.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailItemIdWrapperTest {

    @Test
    public void testGetSetMailItemId() {
        MailItemIdWrapper wrapper = new MailItemIdWrapper();
        wrapper.setMailItemId(1L);
        assertEquals(1L, wrapper.getMailItemId());
    }
}

