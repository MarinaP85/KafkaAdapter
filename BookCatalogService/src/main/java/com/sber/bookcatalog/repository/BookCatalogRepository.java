package com.sber.bookcatalog.repository;

import com.sber.bookcatalog.model.Author;
import com.sber.bookcatalog.model.Book;

import java.io.IOException;
import java.util.List;

//не используется, писалось для старого варианта с JSON-каталогом
public interface BookCatalogRepository {
    /**
     * Создает нового автора
     *
     * @param author - новый автор
     */
    boolean createAuthor(Author author) throws IOException;

    /**
     * Возвращает список имен всех авторов
     *
     * @return список авторов
     */
    List<String> readAllAuthors() throws IOException;

    /**
     * Возвращает автора по его ID
     *
     * @param id - ID автора
     * @return - автор с заданным ID
     */
    Author readAuthorById(int id) throws IOException;

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
    boolean updateAuthor(int id, Author author) throws IOException;

    /**
     * Удаляет автора с заданным ID
     *
     * @param id - id автора, которого нужно удалить
     * @return - true если автор был удален, иначе false
     */
    boolean deleteAuthor(int id) throws IOException;

    /**
     * Возвращает книгу по имени автора и названию
     *
     * @param title  - название книги
     * @param author - имя автора
     * @return - книга по имени автора и названию
     */
    Book readBookByAuthorAndTitle(String author, String title) throws IOException;
}
