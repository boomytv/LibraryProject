package com.proj.libraryproject.library;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String street;
    @Column
    private String phone;
    @Column
    private String director;
}
