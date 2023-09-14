package com.dim.tracker.repository;

import com.dim.tracker.entity.MovementHistory;
import com.dim.tracker.entity.MailItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementHistoryRepository extends JpaRepository<MovementHistory, Long> {
    List<MovementHistory> findByMailItemOrderByTimestampDesc(MailItem mailItem);
}

