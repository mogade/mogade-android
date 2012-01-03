package com.mogade;

import org.json.JSONArray;

public interface RawResponseConverter<T> {
    /**
     * Converts a response received from a server into the desired result type
     * @param body The body to convert
     * @return     The converted value
     * @throws Exception An exception thrown during the conversion
     */
    public T convert(String body) throws Exception;
}
