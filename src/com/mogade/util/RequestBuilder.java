package com.mogade.util;

import android.net.Uri;
import com.mogade.Guard;
import com.mogade.MogadeConfiguration;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;

import java.util.Map;

public class RequestBuilder
{
    public static HttpGet get(String endpoint, Map<String,String> parameters) {
        Uri.Builder builder = MogadeConfiguration.getUriBuilder();
        builder.appendPath(endpoint);
        for (Map.Entry<String,String> parameter : parameters.entrySet()) {
            builder.appendQueryParameter(parameter.getKey(), parameter.getValue());
        }

        return new HttpGet(builder.build().toString());
    }
}
