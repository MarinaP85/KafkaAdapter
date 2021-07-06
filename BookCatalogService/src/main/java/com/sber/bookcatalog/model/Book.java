package com.sber.bookcatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private double price;
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID")
    private Author author;

    public Book(String title, double price) {
        this.title = title;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id = " + id + '\n' +
                ", title = " + title + '\n' +
                ", price = " + price + '\n' +
                ", author = " + (author != null ? author.getName() : null) + '}' + '\n';
    }
}
