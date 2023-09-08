package com.example.tracker.service;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.MovementHistory;
import com.example.tracker.repository.MovementHistoryRepository;
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
        MockitoAnnotations.initMocks(this);
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


