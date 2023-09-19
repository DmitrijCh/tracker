package com.dmitrijch.tracker.request;

import jakarta.validation.constraints.NotNull;

public class HistoryRequest {
    @NotNull(message = "Идентификатор почтового элемента не может быть пустым")
    private Long mailItemId;

    public HistoryRequest() {
    }

    public HistoryRequest(Long mailItemId) {
        this.mailItemId = mailItemId;
    }

    public Long getMailItemId() {
        return mailItemId;
    }

    public void setMailItemId(Long mailItemId) {
        this.mailItemId = mailItemId;
    }
}