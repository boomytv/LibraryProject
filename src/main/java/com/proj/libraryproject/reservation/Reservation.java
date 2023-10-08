package com.proj.libraryproject.reservation;


import com.proj.libraryproject.book.Book;
import com.proj.libraryproject.book.BookDTO;
import com.proj.libraryproject.library.Library;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private Date reservetimestart;
    @Column
    private Date reservetimeend;
    @Column
    private String person;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Reservation() {

    }

    public Reservation(ReservationDTO reservationDTO, Book bookNew)
    {
        id = getId();
        reservetimestart = reservationDTO.getReservetimestart();
        reservetimeend = reservationDTO.getReservetimeend();
        person = reservationDTO.getPerson();
        book = bookNew;
    }
}
