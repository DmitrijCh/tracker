package com.example.tracker.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MailItemTest {

    @Test
    public void testGetSetId() {
        MailItem mailItem = new MailItem();
        mailItem.setId(1L);
        assertEquals(1L, mailItem.getId());
    }

    @Test
    public void testGetSetType() {
        MailItem mailItem = new MailItem();
        mailItem.setType("Package");
        assertEquals("Package", mailItem.getType());
    }

    @Test
    public void testGetSetRecipientIndex() {
        MailItem mailItem = new MailItem();
        mailItem.setRecipientIndex("12345");
        assertEquals("12345", mailItem.getRecipientIndex());
    }

    @Test
    public void testGetSetRecipientAddress() {
        MailItem mailItem = new MailItem();
        mailItem.setRecipientAddress("123 Main St");
        assertEquals("123 Main St", mailItem.getRecipientAddress());
    }

    @Test
    public void testGetSetRecipientName() {
        MailItem mailItem = new MailItem();
        mailItem.setRecipientName("John Doe");
        assertEquals("John Doe", mailItem.getRecipientName());
    }

    @Test
    public void testGetSetCurrentPostOffice() {
        MailItem mailItem = new MailItem();
        PostOffice postOffice = new PostOffice();
        mailItem.setCurrentPostOffice(postOffice);
        assertEquals(postOffice, mailItem.getCurrentPostOffice());
    }

    @Test
    public void testIsSetReceived() {
        MailItem mailItem = new MailItem();
        assertFalse(mailItem.isReceived());

        mailItem.setReceived(true);
        assertTrue(mailItem.isReceived());
    }
}

