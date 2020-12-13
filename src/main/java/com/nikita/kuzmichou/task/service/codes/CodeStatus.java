package com.nikita.kuzmichou.task.service.codes;

import com.google.gson.annotations.SerializedName;

public enum CodeStatus {
    @SerializedName("0")
    OK (0),
    @SerializedName("453")
    FIELD_UNDEFINED (453),
    @SerializedName("452")
    ALREADY_STORED (452),
    @SerializedName("404")
    NOT_FOUND (404);

    @SerializedName("code")
    private int code;

    CodeStatus(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return Integer.toString(this.code);
    }
}
