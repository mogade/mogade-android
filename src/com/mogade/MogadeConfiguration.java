package com.mogade;

import android.net.Uri;

public class MogadeConfiguration {
    private static String url = "api2.mogade.com";
    private static String version = "gamma";
    private static Uri baseUri;

    static {
        // initialize our base uri
        CreateBaseUri();
    }

    public static void setUrl(String value) {
        Guard.NotNullOrEmpty(value, "A valid Api URL is required.");

        url = value;
        CreateBaseUri();
    }

    public static String getUrl() {
        return url;
    }

    public static void setVersion(String value) {
        Guard.NotNullOrEmpty(value, "A valid version is required.");

        version = value;
        CreateBaseUri();
    }

    public static String getVersion() {
        return version;
    }

    public static Uri.Builder getUriBuilder() {
        return baseUri.buildUpon();
    }

    private static void CreateBaseUri() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority(url);
        builder.appendPath("api");
        builder.appendPath(version);

        baseUri = builder.build();
    }
}
