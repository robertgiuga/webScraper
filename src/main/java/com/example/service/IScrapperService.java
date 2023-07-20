package com.example.service;

import com.example.domain.Website;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Set;

public interface IScrapperService {

    void setPage(String url);

    List<String> parseTelephoneNr();

    List<String> parseMediaLinks();

    List<String> parseAddress(Document doc);

}
