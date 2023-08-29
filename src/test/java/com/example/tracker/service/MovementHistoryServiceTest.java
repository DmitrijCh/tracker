package com.example.tracker.service;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.MovementHistory;
import com.example.tracker.repository.MovementHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MovementHistoryServiceTest {
    @Mock
    private MovementHistoryRepository movementHistoryRepository;

    private MovementHistoryService movementHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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

        assertEquals(2, result.size());
        assertEquals(history2, result.get(0));
        assertEquals(history1, result.get(1));
        verify(movementHistoryRepository, times(1)).findByMailItemOrderByTimestampDesc(mailItem);
    }
}

