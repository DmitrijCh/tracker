package com.example.tracker.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostOfficeTest {

    @Test
    public void testGetSetId() {
        PostOffice postOffice = new PostOffice();
        postOffice.setId(1L);
        assertEquals(1L, postOffice.getId());
    }

    @Test
    public void testGetSetIndex() {
        PostOffice postOffice = new PostOffice();
        postOffice.setIndex("12345");
        assertEquals("12345", postOffice.getIndex());
    }

    @Test
    public void testGetSetName() {
        PostOffice postOffice = new PostOffice();
        postOffice.setName("Main Office");
        assertEquals("Main Office", postOffice.getName());
    }

    @Test
    public void testGetSetAddress() {
        PostOffice postOffice = new PostOffice();
        postOffice.setAddress("456 Street");
        assertEquals("456 Street", postOffice.getAddress());
    }
}

