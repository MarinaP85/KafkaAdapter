package com.sber.bookcatalog.service;

import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import com.sber.bookcatalog.repository.BookCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    private final BookCatalogRepository bookCatalogRepository;

    @Autowired
    public BookCatalogServiceImpl(BookCatalogRepository bookCatalogRepository) {
        this.bookCatalogRepository = bookCatalogRepository;
    }

    @Override
    public boolean createAuthor(AuthorDto author) {
        if (author != null) {
            return bookCatalogRepository.createAuthor(author);
        } else return false;
    }

    @Override
    public List<String> readAllAuthors() {
        return bookCatalogRepository.readAllAuthors();
    }

    @Override
    public AuthorDto readAuthorById(int id) {
        if (id > 0) {
            return bookCatalogRepository.readAuthorById(id);
        } else return null;
    }

    @Override
    public boolean updateAuthor(AuthorDto author) {
        if (author != null) {
            return bookCatalogRepository.updateAuthor(author);
        } else return false;
    }

    @Override
    public boolean deleteAuthor(int id) {
        if (id > 0) {
            return bookCatalogRepository.deleteAuthor(id);
        } else return false;
    }

    @Override
    public BookDto readBookByAuthorAndTitle(String author, String title) {
        if ((author.length() > 0) && (title.length() > 0)) {
            return bookCatalogRepository.readBookByAuthorAndTitle(author, title);
        } else return null;
    }

}
