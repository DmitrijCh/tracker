package com.dmitrijch.tracker.request;

public class MailArrivalRequest {
    private Long mailItemId;
    private Long postOfficeId;

    public MailArrivalRequest() {
    }

    public MailArrivalRequest(Long mailItemId, Long postOfficeId) {
        this.mailItemId = mailItemId;
        this.postOfficeId = postOfficeId;
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