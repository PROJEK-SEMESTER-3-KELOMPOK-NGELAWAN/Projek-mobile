package com.majelismdpl.majelis_mdpl.models;

import java.util.List;

public class MeetingPointResponse {
    private boolean success;
    private String message;
    private List<MeetingPoint> data;

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<MeetingPoint> getData() {
        return data;
    }
    public void setData(List<MeetingPoint> data) {
        this.data = data;
    }
}
