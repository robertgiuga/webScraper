package com.example.service;

import com.example.domain.Website;
import com.example.repository.IFileRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrapperService implements IScrapperService {
    IFileRepository<Website> readRepository;
    IFileRepository<Website> writeRepository;
    WebDriver driver;

    public ScrapperService(IFileRepository<Website> readRepository, IFileRepository<Website> writeRepository) {
        this.readRepository = readRepository;
        this.writeRepository= writeRepository;
        //should not be here
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Robert\\chromedriver.exe");
        driver= new ChromeDriver(options);
    }

    @Override
    public void setPage(String url) {
        try {
         driver.get(url);
        }
        catch (WebDriverException e){
//            System.out.println("Site is down");
        }
    }



    @Override
    public List<String> parseTelephoneNr() {
        Set<String> phones= new HashSet<>();
        Document doc =Jsoup.parse(driver.getPageSource());
        String regexPattern= Regex.PHONE.toString();
        Pattern pattern= Pattern.compile(regexPattern);

        Elements elms= doc.getElementsMatchingOwnText(pattern);
        if(!elms.isEmpty()){
            for (Element e: elms) {
                phones.add(e.text());
            }
        }
        return phones.stream().toList();
    }

    @Override
    public List<String> parseMediaLinks() {
        Set<String> socialLinks= new HashSet<>();
        String regexPattern= Regex.MEDIALINK.toString();
        Pattern pattern= Pattern.compile(regexPattern);
        Matcher matcher;
        List<WebElement> links =driver.findElements(By.tagName("a"));
        for (WebElement elm: links) {
            try {
                String link = elm.getAttribute("href");
                if (link != null) {
                    matcher = pattern.matcher(link);
                    if (matcher.matches()) {
                        socialLinks.add(elm.getAttribute("href"));
                    }
                }
            }catch (org.openqa.selenium.StaleElementReferenceException e){
                //TODO
            }
        }
        return socialLinks.stream().toList();
    }

    @Override
    public List<String> parseAddress(Document doc) {
        return null;
    }

}
