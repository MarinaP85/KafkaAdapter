package com.sber.bookcatalog.model;

public class BookDto {
    private int bookId;
    private String title;
    private double price;

    public BookDto() {
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId + '\n' +
                ", title='" + title + '\n' +
                ", price=" + price +
                '}' + '\n';
    }
}
