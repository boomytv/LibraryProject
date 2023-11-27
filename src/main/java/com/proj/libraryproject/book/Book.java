package com.proj.libraryproject.book;

import com.proj.libraryproject.library.Library;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Optional;

@Entity
@Data
@Table(name= "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String publisher;
    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    public Book(BookDTO bookDTO, Library libraryNew)
    {
        id = getId();
        title = bookDTO.getTitle();
        author = bookDTO.getAuthor();
        publisher = bookDTO.getPublisher();
        library = libraryNew;
    }

    public Book() {

    }
}
