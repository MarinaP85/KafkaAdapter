package com.sber.bookcatalog.service;

import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.Author;
import com.sber.bookcatalog.model.Book;

import java.util.List;

public interface BookCatalogService {
    /**
     * Создает нового автора
     *
     * @param author - новый автор, должен быть не null
     * @return - автор с созданным ID
     */
    Author createAuthor(Author author) throws ServiceException;

    /**
     * Возвращает список имен всех авторов
     *
     * @return список авторов
     */
    List<String> readAllAuthors() throws ServiceException;

    /**
     * Возвращает автора по его ID
     *
     * @param id - ID автора, должен быть больше 0
     * @return - автор с заданным ID
     */
    Author readAuthorById(long id) throws ServiceException;

    /**
     * Обновляет данные автора,
     * в соответствии с переданным автором
     *
     * @param author - автор в соответсвии с которым нужно обновить данные,
     *               должен быть не null
     * @param id     - ID, в соответсвии с которым нужно обновить данные автора,
     *               должен быть >-1
     * @return - true если данные были обновлены, иначе false
     */
    boolean updateAuthor(long id, Author author) throws ServiceException;

    /**
     * Удаляет автора с заданным ID
     *
     * @param id - id автора, которого нужно удалить, должен быть больше 0
     * @return - true если автор был удален, иначе false
     */
    boolean deleteAuthor(long id) throws ServiceException;

    /**
     * Возвращает книгу по имени автора и названию
     *
     * @param title  - название книги, строка должна быть не пустая
     * @param author - имя автора, строка должна быть не пустая
     * @return - книга по имени автора и названию
     */
    Book readBookByAuthorAndTitle(String author, String title) throws ServiceException;

}
