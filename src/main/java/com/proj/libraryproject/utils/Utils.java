package com.proj.libraryproject.utils;

import com.proj.libraryproject.library.Library;
import com.proj.libraryproject.library.LibraryDTO;

public class Utils {
    public static Library convertToLibrary(LibraryDTO libraryDTO) {
        Library library = new Library();
        library.setName(libraryDTO.getName());
        library.setStreet(libraryDTO.getStreet());
        library.setPhone(libraryDTO.getPhone());
        library.setDirector(libraryDTO.getDirector());
        return library;
    }
}
