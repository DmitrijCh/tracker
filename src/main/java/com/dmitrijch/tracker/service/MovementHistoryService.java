package com.dmitrijch.tracker.service;

import com.dmitrijch.tracker.entity.MovementHistory;
import com.dmitrijch.tracker.repository.MovementHistoryRepository;
import com.dmitrijch.tracker.entity.MailItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovementHistoryService {
    private final MovementHistoryRepository movementHistoryRepository;

    @Autowired
    public MovementHistoryService(MovementHistoryRepository movementHistoryRepository) {
        this.movementHistoryRepository = movementHistoryRepository;
    }

    public List<MovementHistory> getFullHistory(MailItem mailItem) {
        return movementHistoryRepository.findByMailItemOrderByTimestampDesc(mailItem);
    }
}