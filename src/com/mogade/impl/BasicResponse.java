package com.mogade.impl;

import com.mogade.ErrorMessage;
import com.mogade.Response;

public class BasicResponse<T> implements Response<T> {
    private boolean successful = true;
    private T data = null;
    private ErrorMessage error = null;

    public boolean wasSuccessful() {
        return successful;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
        successful = false;
    }

    public static <T> Response<T> failure(ErrorMessage error) {
        BasicResponse<T> response = new BasicResponse<T>();
        response.setError(error);

        return response;
    }
}
