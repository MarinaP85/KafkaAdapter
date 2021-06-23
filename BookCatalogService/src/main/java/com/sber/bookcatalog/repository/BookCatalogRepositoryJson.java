package com.sber.bookcatalog.repository;

import com.sber.bookcatalog.model.Author;
import com.sber.bookcatalog.model.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sber.bookcatalog.configuration.BookCatalogProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BookCatalogRepositoryJson implements BookCatalogRepository {

    private final String bookCatalogDir;

    @Autowired
    public BookCatalogRepositoryJson(BookCatalogProperties properties) {
        this.bookCatalogDir = properties.getDirectory();
    }

    @Override
    public boolean createAuthor(Author author) throws IOException {
        //проходим по веткам файла, если id переданного автора не найден,
        //добавляем в конец и возвращаем true
        //если такой автор существует, возвращаем false
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

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
    }

    @Override
    public List<String> readAllAuthors() throws IOException {
        //проходим по веткам верхнего уровня, находим и возвращаем список всех имен авторов
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(bookCatalogDir + "\\BookCatalog.json");
        JsonNode rootNode = mapper.readTree(file);
        return rootNode.get("catalog").findValuesAsText("authorName");
//        String json;
//            json = new String(Files.readAllBytes(Paths.get(bookCatalogDir + "\\BookCatalog.json")));
//            return JsonPath.read(json, "$.catalog[*].author");
    }

    @Override
    public Author readAuthorById(int id) throws IOException {
        //проходим по веткам файла, если заданный id найден,
        //возвращаем соответствующую ветку, преобразованную в Author,
        //в противном случае возвращаем null
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(bookCatalogDir + "\\BookCatalog.json");
        JsonNode rootNode = mapper.readTree(file);
        JsonNode catalog = rootNode.get("catalog");
        JsonNode bookElement;
        if (catalog.isArray()) {
            for (int i = 0; i < catalog.size(); i++) {
                bookElement = catalog.get(i);
                if (bookElement.hasNonNull("id")) {
                    if (bookElement.get("id").numberValue().equals(id)) {
                        return mapper.convertValue(bookElement, Author.class);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateAuthor(Author author) throws IOException {
        //проходим по веткам файла, если id переданного автора найден,
        //удаляем ветку и на ее место добавляем переданной объект Author и возвращаем true
        //если такой автор не существует, возвращаем false
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
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
    }

    @Override
    public boolean deleteAuthor(int id) throws IOException {
        //проходим по веткам файла, если id переданного автора найден,
        //удаляем ветку и возвращаем true
        //если такой автор не существует, возвращаем false
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
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
    }

    @Override
    public Book readBookByAuthorAndTitle(String authorName, String title) throws IOException {
        //проходим по веткам файла, если заданное имя автора и назвние книги найдены,
        //возвращаем соответствующую ветку, преобразованную в Book,
        //в противном случае возвращаем null
        ObjectMapper mapper = new ObjectMapper();
        Author author;
        Book result;
        File file = new File(bookCatalogDir + "\\BookCatalog.json");
        JsonNode rootNode = mapper.readTree(file);
        JsonNode catalog = rootNode.get("catalog");
        JsonNode bookElement;
        JsonNode titleBook;
        if (catalog.isArray()) {
            for (int i = 0; i < catalog.size(); i++) {
                bookElement = catalog.get(i);
                if ((bookElement.hasNonNull("authorName")) && (bookElement.hasNonNull("bookList"))) {
                    if (bookElement.get("authorName").textValue().equalsIgnoreCase(authorName)) {
                        author = mapper.convertValue(bookElement, Author.class);
                        for (int j = 0; j < bookElement.get("bookList").size(); j++) {
                            titleBook = bookElement.get("bookList").get(j);
                            if (titleBook.hasNonNull("title")) {
                                if (titleBook.get("title").textValue().equalsIgnoreCase(title)) {
                                    result = mapper.convertValue(titleBook, Book.class);
                                    result.setAuthor(author);
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
//        String json;
//        try {
//            json = new String(Files.readAllBytes(Paths.get(bookCatalogDir + "\\BookCatalog.json")));
//            return JsonPath.parse(json).read("$.catalog[?(@.author == '" + author +
//                    "')].bookList[?(@.title == '" + title + "')]", Book.class);
//        } catch (IOException | InvalidPathException e) {
//            e.printStackTrace();
//            return null;
//        }
    }
}
