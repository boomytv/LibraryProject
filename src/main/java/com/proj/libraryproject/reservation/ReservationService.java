package com.proj.libraryproject.reservation;

import com.proj.libraryproject.book.Book;
import com.proj.libraryproject.book.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository repository;

    public List<Reservation> findByLibraryIdAndBookId(int libraryId, int bookId) {
        List<Reservation> reservationList = new ArrayList<Reservation>();
        for(Reservation reservation : repository.findAll())
        {
            if (reservation.getBook().getLibrary().getId() == libraryId)
            {
                if(reservation.getBook().getId() == bookId)
                {
                    reservationList.add(reservation);
                }
            }
        }
        return reservationList;
    }

    public Reservation findByLibraryIdAndBookIdAndReservationId(int libraryId, int bookId, int resId) {
        Reservation reservation = new Reservation();
        for(Reservation reservationCheck : repository.findAll())
        {
            if (reservationCheck.getBook().getLibrary().getId() == libraryId)
            {
                if(reservationCheck.getBook().getId() == bookId)
                {
                    if (reservationCheck.getId() == resId)
                    {
                        reservation = repository.findById(resId).orElse(null);
                    }
                }
            }
        }
        return reservation;
    }

    public boolean updateReservationById(int libId, int bookId, int resId, ReservationDTO reservationDTO)
    {
        Reservation reservationToUpdate = findByLibraryIdAndBookIdAndReservationId(libId, bookId, resId);
        if (reservationToUpdate.getPerson() != null)
        {
            reservationToUpdate.setPerson(reservationDTO.getPerson());
            reservationToUpdate.setReservetimestart(reservationDTO.getReservetimestart());
            reservationToUpdate.setReservetimeend(reservationDTO.getReservetimeend());
            repository.save(reservationToUpdate);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteReservation(Reservation reservation) { repository.delete(reservation); }

    public Reservation insertReservation(Reservation reservation) { return repository.saveAndFlush(reservation); }
}
