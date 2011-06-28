package com.mogade.android;

import com.mogade.Response;

public interface ResponseCallback<T> {
    public void onComplete(Response<T> response);
}
