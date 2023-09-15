package com.dmitrijch.tracker.repository;

import com.dmitrijch.tracker.entity.MailItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailItemRepository extends JpaRepository<MailItem, Long> {
}