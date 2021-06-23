package com.sber.bookcatalog.repository;

import com.sber.bookcatalog.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepositoryJpa extends JpaRepository<Book, Long> {

    @Query("select b from Book b left join Author a on b.author = a where b.title = :title and a.name = :name")
    List<Book> readBookByAuthorAndTitle(@Param("name") String authorName, @Param("title") String title);
}
