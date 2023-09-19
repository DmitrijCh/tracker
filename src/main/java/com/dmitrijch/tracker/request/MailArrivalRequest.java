package com.dmitrijch.tracker.request;

import jakarta.validation.constraints.NotNull;

public class MailArrivalRequest {

    @NotNull(message = "Идентификатор почтового элемента не может быть пустым")
    private Long mailItemId;

    @NotNull(message = "Идентификатор почтового отделения не может быть пустым")
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