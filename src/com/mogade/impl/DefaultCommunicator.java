package com.mogade.impl;

import android.net.Uri;
import android.util.Log;

import com.mogade.*;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultCommunicator {
    public <T> Response<T> get(String endpoint, Map<String, Object> parameters, final ResponseConverter<T> converter) {
        Uri.Builder builder = MogadeConfiguration.getUriBuilder();
        builder.appendPath(endpoint);
        addQuerystringParameters(parameters, builder);

        HttpGet get = new HttpGet(builder.toString());
        get.addHeader("Accept", "*/*");

        return executeRequest(get, converter);
    }

    public <T> Response<T> get(String endpoint, Map<String, Object> parameters, ArrayResponseConverter<T> converter) {
        Uri.Builder builder = MogadeConfiguration.getUriBuilder();
        builder.appendPath(endpoint);
        addQuerystringParameters(parameters, builder);

        HttpGet get = new HttpGet(builder.toString());
        get.addHeader("Accept", "*/*");

        return executeRequest(get, converter);
    }

    public <T> Response<T> post(String endpoint, Map<String, Object> parameters, ResponseConverter<T> converter) {
        Uri.Builder builer = MogadeConfiguration.getUriBuilder();
        builer.appendPath(endpoint);

        HttpPost post = new HttpPost(builer.toString());

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(convertToNameValuePairs(parameters), "UTF-8");
            entity.setContentType("application/x-www-form-urlencoded");
            post.setEntity(entity);

            return executeRequest(post, converter);
        } catch (Exception ex) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage(ex.getMessage());
            error.setInnerException(ex);

            return BasicResponse.failure(error);
        }
    }

    private List<NameValuePair> convertToNameValuePairs(Map<String, Object> parameters) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>(parameters.size());
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String key = parameter.getKey();
            Object val = parameter.getValue();

            if (val == null) continue;
            if (val instanceof Boolean) {
                pairs.add(new BasicNameValuePair(key, Boolean.toString((Boolean) val)));
            } else if (val instanceof List) {
                List<String> items = (List<String>) val;
                for (String item : items) {
                    pairs.add(new BasicNameValuePair(key, item));
                }
            } else {
                pairs.add(new BasicNameValuePair(key, val.toString()));
            }
        }

        return pairs;
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

    private static HttpClient getClientInstance() {
    	
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, "mogade-android");
        HttpProtocolParams.setVersion(params, new ProtocolVersion("HTTP", 1, 1));

        return new DefaultHttpClient(params);
    }

    private <T> Response<T> executeRequest(HttpUriRequest request, ArrayResponseConverter<T> converter) {
        BasicResponse<T> result = new BasicResponse<T>();
        HttpClient client = getClientInstance();

        try {
            String responseText = client.execute(request, new BasicResponseHandler());
            Log.v("mogade", responseText);
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
        HttpClient client = getClientInstance();

        try {
            String responseText = client.execute(request, new BasicResponseHandler());

            Log.v("mogade", responseText);
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
