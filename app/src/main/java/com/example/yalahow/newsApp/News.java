package com.example.yalahow.newsApp;

/**
 * Created by Adnan Yalahow on 7/8/2018.
 */

public class News {

    private String webTitle;
    private String webUrl;
    private String sectionName;
    private String publishedDate;

    public News( String webTitle, String webUrl, String sectionName, String publishedDate){

        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.sectionName = sectionName;
        this.publishedDate = publishedDate;
    }



    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}
