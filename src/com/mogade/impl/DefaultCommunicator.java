package com.mogade.impl;

import android.net.Uri;
import com.mogade.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class DefaultCommunicator {
    public <T> Response<T> get(String endpoint, Map<String, Object> parameters, final ResponseConverter<T> converter) {
        Uri.Builder builder = MogadeConfiguration.getUriBuilder();
        builder.appendPath(endpoint);
        addQuerystringParameters(parameters, builder);

        HttpGet get = new HttpGet(builder.toString());

        return executeRequest(get, converter);
    }

    public <T> Response<T> get(String endpoint, Map<String, Object> parameters, ArrayResponseConverter<T> converter) {
        Uri.Builder builder = MogadeConfiguration.getUriBuilder();
        builder.appendPath(endpoint);
        addQuerystringParameters(parameters, builder);

        HttpGet get = new HttpGet(builder.toString());

        return executeRequest(get, converter);
    }

    public <T> Response<T> post(String endpoint, Map<String, Object> parameters, ResponseConverter<T> converter) {
        return null;
    }

    private void addQuerystringParameters(Map<String, Object> parameters, Uri.Builder builder) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String key = parameter.getKey();
            Object val = parameter.getValue();

            if (val == null) continue;
            if (val instanceof Boolean) {
                builder.appendQueryParameter(key, Boolean.toString((Boolean) val));
            } else if (val instanceof List) {
                List<String> items = (List<String>) val;
                for (String item : items) {
                    builder.appendQueryParameter(key, item);
                }
            } else {
                builder.appendQueryParameter(key, val.toString());
            }
        }
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
