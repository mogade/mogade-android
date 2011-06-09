package com.mogade;

public interface Response<T> {
    public boolean wasSuccessful();

    public T getData();

    public ErrorMessage getError();
}
