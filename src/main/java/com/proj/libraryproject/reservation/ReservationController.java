package com.proj.libraryproject.reservation;

import com.proj.libraryproject.book.Book;
import com.proj.libraryproject.book.BookDTO;
import com.proj.libraryproject.book.BookService;
import com.proj.libraryproject.library.Library;
import com.proj.libraryproject.library.LibraryDTO;
import com.proj.libraryproject.library.LibraryService;
import com.proj.libraryproject.utils.UserService;
import com.proj.libraryproject.web.models.User;
import com.proj.libraryproject.web.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
public class ReservationController {

    @Autowired
    BookService bookService;

    @Autowired
    LibraryService libraryService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(path = "api/library/{libId}/book/{bookId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reservation>> getAllReservations(@PathVariable int libId, @PathVariable int bookId) {
        List<Reservation> reservationList = reservationService.findByLibraryIdAndBookId(libId, bookId);
        return new ResponseEntity<>(reservationList, HttpStatus.OK);
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
    public ResponseEntity<String> addReservation(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int libId, @PathVariable int bookId, @RequestBody ReservationDTO reservationDTO) {
        if(!isRequestDataInvalid(reservationDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        String token = "";
        String[] authorizationHeaderArray = authorizationHeader.split(" ");
        if (authorizationHeaderArray.length == 2 && "Bearer".equals(authorizationHeaderArray[0])) {
            token = authorizationHeaderArray[1];
        }
        else
        {
            return ResponseEntity.badRequest().body("Authorization token wrong");
        }
        String userId = jwtUtils.getUserIdFromJwtToken(token);
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
        Reservation reservation = new Reservation(reservationDTO, book, userId);
        reservationService.insertReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping(path = "/api/library/{libId}/book/{bookId}/reservation/{resId}")
    public ResponseEntity<String> deleteReservation(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int libId, @PathVariable int bookId, @PathVariable int resId) {
        Reservation reservation = reservationService.findByLibraryIdAndBookIdAndReservationId(libId, bookId, resId);
        if (reservation.getPerson() == null)
        {
            return ResponseEntity.notFound().build();
        }
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String[] authorizationHeaderArray = authorizationHeader.split(" ");
        if (authorizationHeaderArray.length == 2 && "Bearer".equals(authorizationHeaderArray[0])) {
            token = authorizationHeaderArray[1];
        }
        else
        {
            return ResponseEntity.badRequest().body("Authorization token wrong");
        }
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        if (Objects.equals(userId, reservation.getCreatorId()) || authentication.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()) || "ROLE_WORKER".equals(role.getAuthority())))
        {
            reservationService.deleteReservation(reservation);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
    }
    @PutMapping(path = "/api/library/{libId}/book/{bookId}/reservation/{resId}")
    public ResponseEntity<String> updateReservation(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int libId, @PathVariable int bookId, @PathVariable int resId, @RequestBody ReservationDTO reservationDTO) {
        if(!isRequestDataInvalid(reservationDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        String token = "";
        String[] authorizationHeaderArray = authorizationHeader.split(" ");
        if (authorizationHeaderArray.length == 2 && "Bearer".equals(authorizationHeaderArray[0])) {
            token = authorizationHeaderArray[1];
        }
        else
        {
            return ResponseEntity.badRequest().body("Authorization token wrong");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Reservation reservation = reservationService.findByLibraryIdAndBookIdAndReservationId(libId, bookId, resId);
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        if (Objects.equals(userId, reservation.getCreatorId()) || authentication.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()) || "ROLE_WORKER".equals(role.getAuthority())))
        {
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
        return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
    }
    private boolean isRequestDataInvalid (ReservationDTO request) {
        return request.getPerson() != null && request.getReservetimestart() != null && request.getReservetimeend()
                != null && !request.getPerson().equals("") && !request.getReservetimestart().toString().equals("")
                && !request.getReservetimeend().toString().equals("");
    }
}
