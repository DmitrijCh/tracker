package com.dmitrijch.tracker.service;

import com.dmitrijch.tracker.entity.MailItem;
import com.dmitrijch.tracker.entity.MovementHistory;
import com.dmitrijch.tracker.entity.PostOffice;
import com.dmitrijch.tracker.repository.MailItemRepository;
import com.dmitrijch.tracker.repository.MovementHistoryRepository;
import com.dmitrijch.tracker.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MailServiceTest {
    private MailService mailService;
    private MailItemRepository mailItemRepository;
    private PostOfficeRepository postOfficeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mailItemRepository = mock(MailItemRepository.class);
        postOfficeRepository = mock(PostOfficeRepository.class);
        MovementHistoryRepository movementHistoryRepository = mock(MovementHistoryRepository.class);
        MovementHistoryService movementHistoryService = Mockito.mock(MovementHistoryService.class);
        mailService = new MailService(mailItemRepository, postOfficeRepository, movementHistoryRepository, movementHistoryService);
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

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.of(mailItem));
        when(postOfficeRepository.findById(postOfficeId)).thenReturn(java.util.Optional.of(postOffice));
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

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.of(mailItem));
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

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.of(mailItem));
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

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.of(mailItem));

        List<MovementHistory> foundHistory = mailService.findMailItemById(mailItemId);

        assertNotNull(foundHistory);
    }
}