package com.example.tracker.service;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.MovementHistory;
import com.example.tracker.repository.MovementHistoryRepository;
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

