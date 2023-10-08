package com.proj.libraryproject.book;

import lombok.Data;

@Data
public class BookDTO {
    private String title;
    private String author;
    private String publisher;

    public BookDTO()
    {

    }

    public BookDTO(Book book)
    {
        title = book.getTitle();
        author = book.getAuthor();
        publisher = book.getPublisher();
    }
}
