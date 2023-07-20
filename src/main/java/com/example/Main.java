package com.example;

import com.example.domain.Website;
import com.example.repository.CsvRepository;
import com.example.repository.ElasticRepo;
import com.example.repository.IFileRepository;
import com.example.repository.IRepository;
import com.example.service.IScrapperService;
import com.example.service.ScrapperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        IFileRepository<Website> readRepository= new CsvRepository("src/main/resources/sample-websites.csv");
        IFileRepository<Website> writeRepository= new CsvRepository("src/main/resources/websites-company-names.csv");
        IRepository<Website> elasticRepository= new ElasticRepo();
        IScrapperService service = new ScrapperService(readRepository,writeRepository);
        String url;
        int count=0;
        //Set<Website> websites= writeRepository.readAll();
        while ((url= readRepository.readLine())!=null&&count<20){
            Set<String> phones= new HashSet<>();
            Set<String> socialLinks= new HashSet<>();
            service.setPage("http://"+url+"/contact");
            phones.addAll(service.parseTelephoneNr());
            socialLinks.addAll(service.parseMediaLinks());
            service.setPage("http://"+url+"/contact-us");
            phones.addAll(service.parseTelephoneNr());
            socialLinks.addAll(service.parseMediaLinks());
            service.setPage("http://"+url);
            phones.addAll(service.parseTelephoneNr());
            socialLinks.addAll(service.parseMediaLinks());

            Website website= new Website.Builder()
                    .setDomain(url)
                    .setTelephones(phones.stream().toList())
                    .setMediaLinks(socialLinks.stream().toList()).build();
            //useful if we would to save the data to the csv
//            if(websites.contains(website)){
//                Website w= websites.stream().filter(website1 -> website1.getDomain().equals(website.getDomain())).findFirst().get();
//                website.setAvailableNames(w.getAvailableNames());
//                website.setLegalName(w.getLegalName());
//                website.setName(w.getName());
//                websites.remove(website);
//                websites.add(website);
//            }
            elasticRepository.update(website);
            count++;
        }
        System.out.println("ok");
       //writeRepository.writeAll(websites);


    }
}
