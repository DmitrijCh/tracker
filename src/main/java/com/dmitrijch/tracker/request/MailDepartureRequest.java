package com.dmitrijch.tracker.request;

public class MailDepartureRequest {
    private Long mailItemId;

    public MailDepartureRequest() {
    }

    public Long getMailItemId() {
        return mailItemId;
    }

    public void setMailItemId(Long mailItemId) {
        this.mailItemId = mailItemId;
    }
}