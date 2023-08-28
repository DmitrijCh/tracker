package com.example.tracker.entity;

import jakarta.persistence.*;

@Entity
public class MailItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String recipientIndex;
    private String recipientAddress;
    private String recipientName;

    @ManyToOne
    @JoinColumn(name = "current_post_office_id")
    private PostOffice currentPostOffice;

    @Column(name = "received")
    private boolean received;

    public MailItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipientIndex() {
        return recipientIndex;
    }

    public void setRecipientIndex(String recipientIndex) {
        this.recipientIndex = recipientIndex;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public PostOffice getCurrentPostOffice() {
        return currentPostOffice;
    }

    public void setCurrentPostOffice(PostOffice currentPostOffice) {
        this.currentPostOffice = currentPostOffice;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
