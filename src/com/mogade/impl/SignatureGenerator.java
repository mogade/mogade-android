package com.mogade.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;

public class SignatureGenerator {
    public static String generate(SortedMap<String, String> parameters, String secret) {
        StringBuilder signature = new StringBuilder();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            signature.append(parameter.getKey());
            signature.append("|");
            signature.append(parameter.getValue());
            signature.append("|");
        }

        signature.append(secret);

        return sha1(signature.toString());
    }

    public static String sha1(String value) {
        try {

            byte[] hashed = MessageDigest.getInstance("SHA-1").digest(value.getBytes("UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < hashed.length; i++) {
                builder.append(String.format("%1$x", hashed[i]));
            }

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}