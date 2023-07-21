package com.example.repository;

import com.example.domain.Website;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ElasticRepo implements IRepository<Website> {
    private OkHttpClient client;
    public ElasticRepo() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
        newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
        newBuilder.hostnameVerifier((hostname, session) -> true);

        client = newBuilder.build();
    }

    @Override
    public void update(Website w) {
        MediaType mediaType = MediaType.parse("application/json");
        StringBuilder bodyValue= new StringBuilder("{\n").append("\"doc\" :\n");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            bodyValue.append(objectMapper.writeValueAsString(w)).append("\n}");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, bodyValue.toString());
        Request request = new Request.Builder()
                .url("https://localhost:9200/website/_update/"+w.getDomain())
                .method("POST", body)
                .addHeader("Authorization", "Basic ZWxhc3RpYzp0U0hEZmRmTTlzeUdTcDVrMGlHcA==")
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
