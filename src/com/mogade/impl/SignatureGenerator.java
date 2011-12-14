package com.mogade.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.SortedMap;

public class SignatureGenerator {
    public static String generate(SortedMap<String, Object> parameters, String secret) {
        StringBuilder signature = new StringBuilder();
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
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
            byte[] bytes = MessageDigest.getInstance("SHA-1").digest(value.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(bytes.length * 2);

            Formatter formatter = new Formatter(sb);
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
