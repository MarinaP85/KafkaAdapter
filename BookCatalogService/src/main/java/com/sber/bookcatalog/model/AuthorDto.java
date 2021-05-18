package com.sber.bookcatalog.model;

import java.util.List;

public class AuthorDto {
    private int id;
    private String author;
    private List<BookDto> bookList;

    public AuthorDto() {
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public List<BookDto> getBookList() {
        return bookList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookList(List<BookDto> bookList) {
        this.bookList = bookList;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id + '\n' +
                ", author='" + author + '\n' +
                ", bookList=" + bookList.toString() +
                '}';
    }
}
