package com.proj.libraryproject.reservation;

import com.proj.libraryproject.book.Book;
import com.proj.libraryproject.book.BookDTO;
import com.proj.libraryproject.book.BookService;
import com.proj.libraryproject.library.Library;
import com.proj.libraryproject.library.LibraryDTO;
import com.proj.libraryproject.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationController {

    @Autowired
    BookService bookService;

    @Autowired
    LibraryService libraryService;

    @Autowired
    ReservationService reservationService;

    @GetMapping(path = "api/library/{libId}/book/{bookId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationDTO>> getAllReservations(@PathVariable int libId, @PathVariable int bookId) {
        List<Reservation> reservationList = reservationService.findByLibraryIdAndBookId(libId, bookId);
        List<ReservationDTO> reservationDTOList = new ArrayList<>();
        if (reservationList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        for(Reservation reservation : reservationList)
        {
            reservationDTOList.add(new ReservationDTO(reservation));
        }
        return new ResponseEntity<>(reservationDTOList, HttpStatus.OK);
    }
    @GetMapping(path = "/api/library/{libId}/book/{bookId}/reservation/{resId}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable int libId, @PathVariable int bookId, @PathVariable int resId) {
        Reservation reservation = reservationService.findByLibraryIdAndBookIdAndReservationId(libId, bookId, resId);
        if (reservation.getPerson() == null)
        {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.OK);
    }

    @PostMapping(path = "/api/library/{libId}/book/{bookId}/reservation")
    public ResponseEntity<String> addReservation(@PathVariable int libId, @PathVariable int bookId, @RequestBody ReservationDTO reservationDTO) {
        if(!isRequestDataInvalid(reservationDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        Book book = bookService.findByLibraryIdAndBookId(libId, bookId);
        if (book.getTitle() == null)
        {
            return ResponseEntity.notFound().build();
        }
        int dateComparison = reservationDTO.getReservetimestart().compareTo(reservationDTO.getReservetimeend());
        if (dateComparison > 0)
        {
            return ResponseEntity.badRequest().body("The start date is after the end date");
        }
        Reservation reservation = new Reservation(reservationDTO, book);
        reservationService.insertReservation(reservation);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(path = "/api/library/{libId}/book/{bookId}/reservation/delete/{resId}")
    public ResponseEntity<String> deleteReservation(@PathVariable int libId, @PathVariable int bookId, @PathVariable int resId) {
        Reservation reservation = reservationService.findByLibraryIdAndBookIdAndReservationId(libId, bookId, resId);
        if (reservation.getPerson() == null)
        {
            return ResponseEntity.notFound().build();
        }
        reservationService.deleteReservation(reservation);
        return ResponseEntity.ok().build();
    }
    @PutMapping(path = "/api/library/{libId}/book/{bookId}/reservation/update/{resId}")
    public ResponseEntity<String> updateReservation(@PathVariable int libId, @PathVariable int bookId, @PathVariable int resId, @RequestBody ReservationDTO reservationDTO) {
        if(!isRequestDataInvalid(reservationDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        boolean updated = reservationService.updateReservationById(libId, bookId, resId, reservationDTO);
        if (updated)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
    private boolean isRequestDataInvalid (ReservationDTO request) {
        return request.getPerson() != null && request.getReservetimestart() != null && request.getReservetimeend()
                != null && !request.getPerson().equals("") && !request.getReservetimestart().toString().equals("")
                && !request.getReservetimeend().toString().equals("");
    }
}
