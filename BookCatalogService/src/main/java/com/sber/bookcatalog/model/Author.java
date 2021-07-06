package com.sber.bookcatalog.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> bookList;

    public Author(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id = " + id + '\n' +
                ", author = " + name + '\n' +
                ", bookList = " + bookList.toString() +
                '}';
    }
}
