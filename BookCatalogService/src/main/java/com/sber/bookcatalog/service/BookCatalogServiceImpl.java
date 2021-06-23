package com.sber.bookcatalog.service;

import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.Author;
import com.sber.bookcatalog.model.Book;
import com.sber.bookcatalog.repository.AuthorRepositoryJpa;
import com.sber.bookcatalog.repository.BookRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    @Autowired
    AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    BookRepositoryJpa bookRepositoryJpa;

    @Transactional
    @Override
    public Author createAuthor(Author author) throws ServiceException {
        if (author != null) {
            try {
                List<Book> bookList = author.getBookList();
                bookList.forEach(book -> book.setAuthor(author));
                author.setBookList(bookList);

                return authorRepositoryJpa.save(author);
                //return bookCatalogRepository.createAuthor(author);
            } catch (Exception e) {
                throw new ServiceException("Ошибка каталога: " + e.getMessage());
            }

        } else return null;
    }

    @Transactional
    @Override
    public List<String> readAllAuthors() throws ServiceException {
        try {
            return authorRepositoryJpa.readAllAuthors();
            //return bookCatalogRepository.readAllAuthors();
        } catch (Exception e) {
            throw new ServiceException("Ошибка каталога: " + e.getMessage());
        }

    }

    @Transactional
    @Override
    public Author readAuthorById(long id) throws ServiceException {
        if (id > 0) {
            try {
                return authorRepositoryJpa.findById(id).orElse(null);
                //return bookCatalogRepository.readAuthorById(id);
            } catch (Exception e) {
                throw new ServiceException("Ошибка каталога: " + e.getMessage());
            }
        } else return null;
    }

    @Transactional
    @Override
    public boolean updateAuthor(long id, Author author) throws ServiceException {
        if (author != null) {
            try {
                List<Book> bookList = author.getBookList();
                bookList.forEach(book -> book.setAuthor(author));
                author.setBookList(bookList);

                Optional<Author> optionalAuthor = authorRepositoryJpa.findById(id);
                if (optionalAuthor.isPresent()) {
                    author.setId(id);
                    authorRepositoryJpa.save(author);
                    return true;
                } else {
                    return false;
                }
                //return bookCatalogRepository.updateAuthor(author);
            } catch (Exception e) {
                throw new ServiceException("Ошибка каталога: " + e.getMessage());
            }
        } else return false;
    }

    @Transactional
    @Override
    public boolean deleteAuthor(long id) throws ServiceException {
        if (id > 0) {
            try {
                if (authorRepositoryJpa.findById(id).isPresent()) {
                    authorRepositoryJpa.deleteById(id);
                    return true;
                } else {
                    return false;
                }
                //return bookCatalogRepository.deleteAuthor(id);
            } catch (Exception e) {
                throw new ServiceException("Ошибка каталога: " + e.getMessage());
            }
        } else return false;
    }

    @Transactional
    @Override
    public Book readBookByAuthorAndTitle(String author, String title) throws ServiceException {
        if ((author.length() > 0) && (title.length() > 0)) {
            try {
                return bookRepositoryJpa.readBookByAuthorAndTitle(author, title).get(0);
                //return bookCatalogRepository.readBookByAuthorAndTitle(author, title);
            } catch (Exception e) {
                throw new ServiceException("Ошибка каталога: " + e.getMessage());
            }
        } else return null;
    }

}
