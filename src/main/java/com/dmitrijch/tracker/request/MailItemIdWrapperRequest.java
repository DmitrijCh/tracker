package com.dmitrijch.tracker.request;

import jakarta.validation.constraints.NotNull;

public class MailItemIdWrapperRequest {

    @NotNull(message = "Идентификатор почтового элемента не может быть пустым")
    private Long mailItemId;

    public MailItemIdWrapperRequest() {
    }

    public Long getMailItemId() {
        return mailItemId;
    }

    public void setMailItemId(Long mailItemId) {
        this.mailItemId = mailItemId;
    }
}