package com.example.tracker.repository;

import com.example.tracker.entity.MailItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailItemRepository extends JpaRepository<MailItem, Long> {
}

