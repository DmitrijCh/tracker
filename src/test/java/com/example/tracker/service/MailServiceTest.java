package com.example.tracker.service;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.PostOffice;
import com.example.tracker.repository.MailItemRepository;
import com.example.tracker.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MailServiceTest {

    private MailItemRepository mailItemRepository;
    private PostOfficeRepository postOfficeRepository;
    private MailService mailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mailItemRepository = mock(MailItemRepository.class);
        postOfficeRepository = mock(PostOfficeRepository.class);
        mailService = new MailService(mailItemRepository, postOfficeRepository);
    }

    @Test
    public void testRegisterMailItem() {
        MailItem mailItem = new MailItem();
        when(mailItemRepository.save(mailItem)).thenReturn(mailItem);

        MailItem savedMailItem = mailService.registerMailItem(mailItem);

        assertNotNull(savedMailItem);
        verify(mailItemRepository, times(1)).save(mailItem);
    }

    @Test
    public void testUpdateCurrentPostOffice() {
        Long mailItemId = 1L;
        Long postOfficeId = 2L;

        MailItem mailItem = new MailItem();
        mailItem.setId(mailItemId);

        PostOffice postOffice = new PostOffice();
        postOffice.setId(postOfficeId);

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.ofNullable(mailItem));
        when(postOfficeRepository.findById(postOfficeId)).thenReturn(java.util.Optional.ofNullable(postOffice));
        when(mailItemRepository.save(mailItem)).thenReturn(mailItem);

        MailItem updatedMailItem = mailService.updateCurrentPostOffice(mailItemId, postOfficeId);

        assertNotNull(updatedMailItem);
        assertEquals(postOffice, updatedMailItem.getCurrentPostOffice());
        verify(mailItemRepository, times(1)).save(mailItem);
    }

    @Test
    public void testUpdateDepartureFromPostOffice() {
        Long mailItemId = 1L;

        MailItem mailItem = new MailItem();
        mailItem.setId(mailItemId);

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.ofNullable(mailItem));
        when(mailItemRepository.save(mailItem)).thenReturn(mailItem);

        MailItem updatedMailItem = mailService.updateDepartureFromPostOffice(mailItemId);

        assertNotNull(updatedMailItem);
        assertNull(updatedMailItem.getCurrentPostOffice());
        verify(mailItemRepository, times(1)).save(mailItem);
    }

    @Test
    public void testUpdateReceivedStatus() {
        Long mailItemId = 1L;

        MailItem mailItem = new MailItem();
        mailItem.setId(mailItemId);

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.ofNullable(mailItem));
        when(mailItemRepository.save(mailItem)).thenReturn(mailItem);

        MailItem updatedMailItem = mailService.updateReceivedStatus(mailItemId);

        assertNotNull(updatedMailItem);
        assertTrue(updatedMailItem.isReceived());
        verify(mailItemRepository, times(1)).save(mailItem);
    }

    @Test
    public void testFindMailItemById() {
        Long mailItemId = 1L;
        MailItem mailItem = new MailItem();
        mailItem.setId(mailItemId);

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.ofNullable(mailItem));

        MailItem foundMailItem = mailService.findMailItemById(mailItemId);

        assertNotNull(foundMailItem);
        assertEquals(mailItemId, foundMailItem.getId());
    }
}

