package com.sber.bookcatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogServiceImpl implements CatalogService {

    // Хранилище авторов
    private static final Map<Integer, AuthorDto> AUTHOR_REPOSITORY_MAP = new HashMap<>();

    @Value("${book-catalog.directory}")
    private String bookCatalogDir;

    @Override
    public boolean createAuthor(AuthorDto author) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //String jsonString = mapper.writeValueAsString(author);
            boolean flagFind = false;
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if (bookElement.hasNonNull("id")) {
                        if (bookElement.get("id").textValue().equalsIgnoreCase(Integer.toString(author.getId()))) {
                            flagFind = true;
                            break;
                        }
                    }
                }
            }
            if (!flagFind) {
                ((ArrayNode) catalog).addPOJO(author);
                ((ObjectNode) rootNode).putArray("catalog").add(catalog);
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
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get(bookCatalogDir + "\\BookCatalog.json")));
            return JsonPath.read(json, "$.catalog[*].author");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public AuthorDto readAuthorById(int id) {
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get(bookCatalogDir + "\\BookCatalog.json")));
            return JsonPath.parse(json).read("$.catalog[?(@.id == " + id + ")]", AuthorDto.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean updateAuthor(AuthorDto author, int id) {
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
                        if (bookElement.get("id").textValue().equalsIgnoreCase(Integer.toString(id))) {
                            ((ArrayNode) catalog).remove(i);
                            ((ArrayNode) catalog).addPOJO(author);
                            ((ObjectNode) rootNode).putArray("catalog").add(catalog);
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
        try {
            File file = new File(bookCatalogDir + "\\BookCatalog.json");
            JsonNode rootNode = mapper.readTree(file);
            JsonNode catalog = rootNode.get("catalog");
            JsonNode bookElement;
            if (catalog.isArray()) {
                for (int i = 0; i < catalog.size(); i++) {
                    bookElement = catalog.get(i);
                    if (bookElement.hasNonNull("id")) {
                        if (bookElement.get("id").textValue().equalsIgnoreCase(Integer.toString(id))) {
                            ((ArrayNode) catalog).remove(i);
                            ((ObjectNode) rootNode).putArray("catalog").add(catalog);
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
        return null;
    }

}
