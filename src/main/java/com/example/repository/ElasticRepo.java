package com.example.repository;

import com.example.domain.Website;
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
        StringBuilder bodyValue= new StringBuilder("{\n").append("\"doc\" : {\n");
        if(w.getDomain()!=null) bodyValue.append("\"domain\" :\""+w.getDomain()+"\",\n");
        if(w.getName()!=null)  bodyValue.append("\"company_commercial_name\" :\""+w.getDomain()+"\",\n");
        if(w.getLegalName()!=null) bodyValue.append("\"company_legal_name\" :\""+w.getDomain()+"\",\n");
        if(w.getAvailableNames()!=null)  bodyValue.append("\"company_all_available_names\" :\""+String.join("|", w.getAvailableNames())+"\",\n");
        if(w.getTelephones()!=null)  bodyValue.append("\"telephone\" :\""+String.join("|", w.getTelephones())+"\",\n");
        if(w.getMediaLinks()!=null) bodyValue.append("\"socialLinks\" :\""+String.join("|", w.getMediaLinks())+"\",\n");
        bodyValue.deleteCharAt(bodyValue.length()-2);
        bodyValue.append("}\n").append("}");

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
