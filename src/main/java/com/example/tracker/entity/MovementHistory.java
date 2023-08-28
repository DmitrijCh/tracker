package com.example.tracker.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MovementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_item_id")
    private MailItem mailItem;

    private LocalDateTime timestamp;
    private String location;
    private String status;

    public MovementHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailItem getMailItem() {
        return mailItem;
    }

    public void setMailItem(MailItem mailItem) {
        this.mailItem = mailItem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

