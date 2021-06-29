package com.sber.bookcatalog.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

//не используется, писалось для старого варианта с JSON-каталогом
//@ConfigurationProperties(prefix = "book-catalog")
public class BookCatalogProperties {
    private String directory;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
