package com.sber.bookcatalog.repository;

import com.sber.bookcatalog.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepositoryJpa extends JpaRepository<Author, Long> {
    @Query("select a.name from Author a order by a.name")
    List<String> readListAuthors();
}
