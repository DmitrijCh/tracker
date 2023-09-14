package com.dim.tracker.service;

import com.dim.tracker.entity.MovementHistory;
import com.dim.tracker.entity.PostOffice;
import com.dim.tracker.repository.MailItemRepository;
import com.dim.tracker.entity.MailItem;
import com.dim.tracker.repository.MovementHistoryRepository;
import com.dim.tracker.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MailServiceTest {
    private MailService mailService;
    private MailItemRepository mailItemRepository;
    private PostOfficeRepository postOfficeRepository;
    private MovementHistoryRepository movementHistoryRepository;
    private MovementHistoryService movementHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mailItemRepository = mock(MailItemRepository.class);
        postOfficeRepository = mock(PostOfficeRepository.class);
        movementHistoryRepository = mock(MovementHistoryRepository.class);
        movementHistoryService = mock(MovementHistoryService.class);
        mailService = new MailService (mailItemRepository, postOfficeRepository, movementHistoryRepository, movementHistoryService);
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

        when(mailItemRepository.findById(mailItemId)).thenReturn(java.util.Optional.of(mailItem));

        List<MovementHistory> foundHistory = mailService.findMailItemById(mailItemId);

        assertNotNull(foundHistory);
    }
}
