package com.mogade.impl;

import com.mogade.ErrorMessage;
import com.mogade.Response;
import com.mogade.ResponseConverter;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.Map;

public class DefaultCommunicator {
    public <T> Response<T> Get(String endpoint, Map<String, String> parameters, ResponseConverter<T> converter) {

        return null;
    }

    private <T> Response<T> ExecuteRequest(HttpUriRequest request, ResponseConverter<T> converter) {
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
