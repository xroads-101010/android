package com.li.xroads.helper;

import android.os.Parcelable;

import com.li.xroads.util.Constant;

import java.io.Serializable;
import java.net.HttpURLConnection;

/**
 * Created by nagnath_p on 3/4/2016.
 */
public class HTTPResponse implements Serializable {
    /**
     * Numeric status code, 1000: Exception
     */
    public static final int HTTP_Exception = 1000;


    private int status;
    private String response;
    private String error;

    public HTTPResponse() {
        this.status = HTTP_Exception;
    }

    public HTTPResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response == null ? Constant.EMPTY_JSON : response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
