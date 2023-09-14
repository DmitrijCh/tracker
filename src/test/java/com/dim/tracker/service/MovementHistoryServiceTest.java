package com.dim.tracker.service;

import com.dim.tracker.entity.MovementHistory;
import com.dim.tracker.repository.MovementHistoryRepository;
import com.dim.tracker.entity.MailItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MovementHistoryServiceTest {

    private MovementHistoryRepository movementHistoryRepository;
    private MovementHistoryService movementHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        movementHistoryRepository = mock(MovementHistoryRepository.class);
        movementHistoryService = new MovementHistoryService(movementHistoryRepository);
    }

    @Test
    public void testGetFullHistory() {
        MailItem mailItem = new MailItem();
        MovementHistory history1 = new MovementHistory(mailItem, "Location A");
        MovementHistory history2 = new MovementHistory(mailItem, "Location B");

        List<MovementHistory> historyList = new ArrayList<>();
        historyList.add(history1);
        historyList.add(history2);

        when(movementHistoryRepository.findByMailItemOrderByTimestampDesc(mailItem))
                .thenReturn(historyList);

        List<MovementHistory> result = movementHistoryService.getFullHistory(mailItem);

        assertEquals(history2.getId(), result.get(0).getId());
        assertEquals(history2.getMailItem(), result.get(0).getMailItem());
    }
}


