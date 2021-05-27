package com.sber.bookcatalog.repository;

import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sber.bookcatalog.configuration.BookCatalogProperties;
import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class BookCatalogRepositoryImpl implements BookCatalogRepository {

    private final String bookCatalogDir;

    @Autowired
    public BookCatalogRepositoryImpl(BookCatalogProperties properties) {
        this.bookCatalogDir = properties.getDirectory();
    }

    @Override
    public boolean createAuthor(AuthorDto author) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            boolean flagFind = false;
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if (bookElement.hasNonNull("id")) {
                        if (bookElement.get("id").numberValue().equals(author.getId())) {
                            flagFind = true;
                            break;
                        }
                    }
                }
            }
            if (!flagFind) {
                ((ArrayNode) catalog).addPOJO(author);
                mapper.writeValue(file, rootNode);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> readAllAuthors() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            return rootNode.get("catalog").findValuesAsText("author");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        String json;
//        try {
//            json = new String(Files.readAllBytes(Paths.get(bookCatalogDir + "\\BookCatalog.json")));
//            return JsonPath.read(json, "$.catalog[*].author");
//        } catch (IOException | InvalidPathException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public AuthorDto readAuthorById(int id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if (bookElement.hasNonNull("id")) {
                        if (bookElement.get("id").numberValue().equals(id)) {
                            return mapper.convertValue(bookElement, AuthorDto.class);
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateAuthor(AuthorDto author) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if (bookElement.hasNonNull("id")) {
                        if (bookElement.get("id").numberValue().equals(author.getId())) {
                            ((ArrayNode) catalog).remove(i);
                            ((ArrayNode) catalog).addPOJO(author);
                            mapper.writeValue(file, rootNode);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAuthor(int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if (bookElement.hasNonNull("id")) {
                        if (bookElement.get("id").numberValue().equals(id)) {
                            ((ArrayNode) catalog).remove(i);
                            mapper.writeValue(file, rootNode);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BookDto readBookByAuthorAndTitle(String author, String title) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            JsonNode titleBook;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if ((bookElement.hasNonNull("author")) && (bookElement.hasNonNull("bookList"))) {
                        if (bookElement.get("author").textValue().equalsIgnoreCase(author)) {
                            for (int j = 0; j < bookElement.get("bookList").size(); j++) {
                                titleBook = bookElement.get("bookList").get(j);
                                if (titleBook.hasNonNull("title")) {
                                    if (titleBook.get("title").textValue().equalsIgnoreCase(title)) {
                                        return mapper.convertValue(titleBook, BookDto.class);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        String json;
//        try {
//            json = new String(Files.readAllBytes(Paths.get(bookCatalogDir + "\\BookCatalog.json")));
//            return JsonPath.parse(json).read("$.catalog[?(@.author == '" + author +
//                    "')].bookList[?(@.title == '" + title + "')]", BookDto.class);
//        } catch (IOException | InvalidPathException e) {
//            e.printStackTrace();
//            return null;
//        }
    }
}