package com.dmitrijch.tracker.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @Test
    public void testGetMessage() {
        String expectedMessage = "This is an error message";
        ErrorResponse errorResponse = new ErrorResponse(expectedMessage);
        String actualMessage = errorResponse.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testSetMessage() {
        ErrorResponse errorResponse = new ErrorResponse("");
        String newMessage = "New error message";
        errorResponse.setMessage(newMessage);
        String actualMessage = errorResponse.getMessage();
        assertEquals(newMessage, actualMessage);
    }
}