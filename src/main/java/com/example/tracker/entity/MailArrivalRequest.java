package com.example.tracker.entity;

public class MailArrivalRequest {
    private Long mailItemId;
    private Long postOfficeId;

    public MailArrivalRequest() {
    }

    public Long getMailItemId() {
        return mailItemId;
    }

    public void setMailItemId(Long mailItemId) {
        this.mailItemId = mailItemId;
    }

    public Long getPostOfficeId() {
        return postOfficeId;
    }

    public void setPostOfficeId(Long postOfficeId) {
        this.postOfficeId = postOfficeId;
    }
}
