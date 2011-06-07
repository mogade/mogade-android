package com.mogade;

import org.json.JSONObject;

public interface ResponseConverter<T> {
    /**
     * Converts a json object into the proper type
     *
     * @param source The json object
     * @return The parsed instance
     * @throws Exception An exception that was thrown during the conversion
     */
    public T convert(JSONObject source) throws Exception;
}
