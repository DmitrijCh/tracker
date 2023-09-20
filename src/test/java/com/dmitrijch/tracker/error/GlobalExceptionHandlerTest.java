package com.dmitrijch.tracker.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleValidationException() {
        // Создаем объект MethodArgumentNotValidException и BindingResult для тестирования
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        // Создаем список FieldError для BindingResult
        List<FieldError> fieldErrors = List.of(
                new FieldError("field1", "field1", "Error 1"),
                new FieldError("field2", "field2", "Error 2")
        );

        // Устанавливаем поведение mock объектов
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Вызываем обработчик и получаем результат
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleValidationException(ex);

        // Проверяем, что статус ответа соответствует ожидаемому (HttpStatus.BAD_REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        // Проверяем, что ответ содержит ожидаемые ошибки в виде Map
        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertEquals(2, responseBody.size());
        assertEquals("Error 1", responseBody.get("field1"));
        assertEquals("Error 2", responseBody.get("field2"));
    }
}