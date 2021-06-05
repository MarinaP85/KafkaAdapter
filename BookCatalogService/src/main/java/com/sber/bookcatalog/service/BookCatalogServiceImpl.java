package com.sber.bookcatalog.service;

import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import com.sber.bookcatalog.repository.BookCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    private final BookCatalogRepository bookCatalogRepository;

    @Autowired
    public BookCatalogServiceImpl(BookCatalogRepository bookCatalogRepository) {
        this.bookCatalogRepository = bookCatalogRepository;
    }

    @Override
    public boolean createAuthor(AuthorDto author) throws ServiceException {
        if (author != null) {
            try {
                return bookCatalogRepository.createAuthor(author);
            } catch (IOException e) {
                throw new ServiceException("Ошибка каталога");
            }

        } else return false;
    }

    @Override
    public List<String> readAllAuthors() throws ServiceException {
        try {
            return bookCatalogRepository.readAllAuthors();
        } catch (IOException e) {
            throw new ServiceException("Ошибка каталога");
        }

    }

    @Override
    public AuthorDto readAuthorById(int id) throws ServiceException {
        if (id > 0) {
            try {
                return bookCatalogRepository.readAuthorById(id);
            } catch (IOException e) {
                throw new ServiceException("Ошибка каталога");
            }
        } else return null;
    }

    @Override
    public boolean updateAuthor(AuthorDto author) throws ServiceException {
        if (author != null) {
            try {
                return bookCatalogRepository.updateAuthor(author);
            } catch (IOException e) {
                throw new ServiceException("Ошибка каталога");
            }
        } else return false;
    }

    @Override
    public boolean deleteAuthor(int id) throws ServiceException {
        if (id > 0) {
            try {
                return bookCatalogRepository.deleteAuthor(id);
            } catch (IOException e) {
                throw new ServiceException("Ошибка каталога");
            }
        } else return false;
    }

    @Override
    public BookDto readBookByAuthorAndTitle(String author, String title) throws ServiceException {
        if ((author.length() > 0) && (title.length() > 0)) {
            try {
                return bookCatalogRepository.readBookByAuthorAndTitle(author, title);
            } catch (IOException e) {
                throw new ServiceException("Ошибка каталога");
            }
        } else return null;
    }

}
