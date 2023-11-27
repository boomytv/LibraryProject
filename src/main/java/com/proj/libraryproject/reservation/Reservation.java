package com.proj.libraryproject.reservation;


import com.proj.libraryproject.book.Book;
import com.proj.libraryproject.book.BookDTO;
import com.proj.libraryproject.library.Library;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name= "reservations")
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
    @Column
    private String creatorId;

    public Reservation() {

    }

    public Reservation(ReservationDTO reservationDTO, Book bookNew, String userid)
    {
        id = getId();
        reservetimestart = reservationDTO.getReservetimestart();
        reservetimeend = reservationDTO.getReservetimeend();
        person = reservationDTO.getPerson();
        book = bookNew;
        creatorId = userid;
    }
}
