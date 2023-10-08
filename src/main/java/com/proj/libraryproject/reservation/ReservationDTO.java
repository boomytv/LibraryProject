package com.proj.libraryproject.reservation;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class ReservationDTO {
    private Date reservetimestart;
    private Date reservetimeend;
    private String person;

    public ReservationDTO() {

    }
    public ReservationDTO(Reservation reservation)
    {
        reservetimestart = reservation.getReservetimestart();
        reservetimeend = reservation.getReservetimeend();
        person = reservation.getPerson();
    }
}
