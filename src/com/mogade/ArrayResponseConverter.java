package com.mogade;

import org.json.JSONArray;

public interface ArrayResponseConverter<T> {
    /**
     * Converts a json array into an array of the proper type
     *
     * @param source The json array
     * @return The parsed array
     * @throws Exception An exception that was thrown during the conversion
     */
    public T convert(JSONArray source) throws Exception;
}
