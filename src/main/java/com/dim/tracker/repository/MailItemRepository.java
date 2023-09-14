package com.dim.tracker.repository;

import com.dim.tracker.entity.MailItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailItemRepository extends JpaRepository<MailItem, Long> {
}

