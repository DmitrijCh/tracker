package com.example.tracker.repository;

import com.example.tracker.entity.MailItem;
import com.example.tracker.entity.MovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementHistoryRepository extends JpaRepository<MovementHistory, Long> {
    List<MovementHistory> findByMailItemOrderByTimestampDesc(MailItem mailItem);
}

