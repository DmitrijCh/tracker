package com.example.tracker.service;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.PostOffice;
import com.example.tracker.repository.MailItemRepository;
import com.example.tracker.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MailServiceTest {
    @Mock
    private MailItemRepository mailItemRepository;

    @Mock
    private PostOfficeRepository postOfficeRepository;

    private MailService mailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mailService = new MailService(mailItemRepository, postOfficeRepository);
    }

    @Test
    public void testRegisterMailItem() {
        MailItem mailItem = new MailItem();
        when(mailItemRepository.save(mailItem)).thenReturn(mailItem);

        MailItem result = mailService.registerMailItem(mailItem);

        assertEquals(mailItem, result);
        verify(mailItemRepository, times(1)).save(mailItem);
    }

    @Test
    public void testUpdateCurrentPostOffice() {
        MailItem mailItem = new MailItem();
        PostOffice postOffice = new PostOffice();

        when(mailItemRepository.findById(1L)).thenReturn(java.util.Optional.of(mailItem));
        when(postOfficeRepository.findById(2L)).thenReturn(java.util.Optional.of(postOffice));
        when(mailItemRepository.save(mailItem)).thenReturn(mailItem);

        MailItem result = mailService.updateCurrentPostOffice(1L, 2L);

        assertEquals(mailItem, result);
        assertEquals(postOffice, mailItem.getCurrentPostOffice());
        verify(mailItemRepository, times(1)).save(mailItem);
    }

    // Добавьте аналогичные тесты для остальных методов

    @Test
    public void testFindMailItemById() {
        MailItem mailItem = new MailItem();

        when(mailItemRepository.findById(1L)).thenReturn(java.util.Optional.of(mailItem));

        MailItem result = mailService.findMailItemById(1L);

        assertEquals(mailItem, result);
    }
}

