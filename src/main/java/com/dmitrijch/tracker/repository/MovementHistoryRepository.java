package com.dmitrijch.tracker.repository;

import com.dmitrijch.tracker.entity.MovementHistory;
import com.dmitrijch.tracker.entity.MailItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementHistoryRepository extends JpaRepository<MovementHistory, Long> {
    List<MovementHistory> findByMailItemOrderByTimestampDesc(MailItem mailItem);
}