package com.sber.bookcatalog.service;

import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;

import java.util.List;

public interface CatalogService {
    /**
     * Создает нового автора
     *
     * @param author - новый автор
     */
    boolean createAuthor(AuthorDto author);

    /**
     * Возвращает список имен всех авторов
     *
     * @return список авторов
     */
    List<String> readAllAuthors();

    /**
     * Возвращает автора по его ID
     *
     * @param id - ID автора
     * @return - автор с заданным ID
     */
    AuthorDto readAuthorById(int id);

    /**
     * Обновляет данные автора с заданным ID,
     * в соответствии с переданным автором
     *
     * @param author - автор в соответсвии с которым нужно обновить данные
     * @param id     - id автора которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean updateAuthor(AuthorDto author, int id);

    /**
     * Удаляет автора с заданным ID
     *
     * @param id - id автора, которого нужно удалить
     * @return - true если автор был удален, иначе false
     */
    boolean deleteAuthor(int id);

    /**
     * Возвращает книгу по имени автора и названию
     *
     * @param title  - название книги
     * @param author - имя автора
     * @return - книга по имени автора и названию
     */
    BookDto readBookByAuthorAndTitle(String author, String title);

}
