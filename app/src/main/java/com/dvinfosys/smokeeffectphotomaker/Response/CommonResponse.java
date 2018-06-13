package com.dvinfosys.smokeeffectphotomaker.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaksys-1 on 3/7/17.
 */

public class CommonResponse {

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

}
