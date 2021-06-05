package com.sber.bookcatalog.repository;

import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;

import java.io.IOException;
import java.util.List;

public interface BookCatalogRepository {
    /**
     * Создает нового автора
     *
     * @param author - новый автор
     */
    boolean createAuthor(AuthorDto author) throws IOException;

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
    AuthorDto readAuthorById(int id) throws IOException;

    /**
     * Обновляет данные автора,
     * в соответствии с переданным автором
     *
     * @param author - автор в соответсвии с которым нужно обновить данные
     * @return - true если данные были обновлены, иначе false
     */
    boolean updateAuthor(AuthorDto author) throws IOException;

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
    BookDto readBookByAuthorAndTitle(String author, String title) throws IOException;
}
