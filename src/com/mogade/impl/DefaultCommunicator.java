package com.mogade.impl;

import com.mogade.ArrayResponseConverter;
import com.mogade.ErrorMessage;
import com.mogade.Response;
import com.mogade.ResponseConverter;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class DefaultCommunicator {
    public <T> Response<T> get(String endpoint, Map<String, Object> parameters, ResponseConverter<T> converter) {

        return null;
    }

    public <T> Response<T> get(String endpoint, Map<String, Object> parameters, ArrayResponseConverter<T> converter) {
        return null;
    }

    public <T> Response<T> post(String endpoint, Map<String, Object> parameters, ResponseConverter<T> converter) {
        return null;
    }

    private <T> Response<T> executeRequest(HttpUriRequest request, ArrayResponseConverter<T> converter) {
        BasicResponse<T> result = new BasicResponse<T>();
        DefaultHttpClient client = new DefaultHttpClient();

        try {
            String responseText = client.execute(request, new BasicResponseHandler());

            JSONArray response = new JSONArray(responseText);
            result.setData(converter.convert(response));
        } catch (Exception ex) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage(ex.getMessage());
            error.setInnerException(ex);

            result.setError(error);
        }

        return result;
    }

    private <T> Response<T> executeRequest(HttpUriRequest request, ResponseConverter<T> converter) {
        BasicResponse<T> result = new BasicResponse<T>();
        DefaultHttpClient client = new DefaultHttpClient();

        try {
            String responseText = client.execute(request, new BasicResponseHandler());

            JSONObject response = new JSONObject(responseText);
            if (response.has("error")) {
                ErrorMessage error = new ErrorMessage();
                error.setMessage(response.getString("error"));
                result.setError(error);
            } else {
                result.setData(converter.convert(response));
            }

        } catch (Exception ex) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage(ex.getMessage());
            error.setInnerException(ex);

            result.setError(error);
        }

        return result;
    }
}
