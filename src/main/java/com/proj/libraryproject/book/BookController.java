package com.proj.libraryproject.book;

import com.proj.libraryproject.library.Library;
import com.proj.libraryproject.library.LibraryDTO;
import com.proj.libraryproject.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    LibraryService libraryService;

    @GetMapping(path = "api/library/{libId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAllBooks(@PathVariable int libId) {
        List<Book> bookList = bookService.findByLibraryId(libId);
        List<BookDTO> bookDTOList = new ArrayList<>();
        for(Book book : bookList)
        {
            bookDTOList.add(new BookDTO(book));
        }
        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }
    @GetMapping(path = "/api/library/{libId}/book/{bookId}")
    public ResponseEntity<BookDTO> getBook(@PathVariable int libId, @PathVariable int bookId) {
        Book book = bookService.findByLibraryIdAndBookId(libId, bookId);
        if (book.getTitle() == null)
        {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(new BookDTO(book), HttpStatus.OK);
    }

    @PostMapping(path = "/api/library/{libId}/book")
    public ResponseEntity<String> addBook(@PathVariable int libId, @RequestBody BookDTO bookDTO) {
        if(!isRequestDataInvalid(bookDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        Library library = libraryService.selectLibrary(libId);
        if (library.getStreet() == null) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book(bookDTO, library);
        bookService.insertBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/api/library/{libId}/book/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable int libId, @PathVariable int bookId) {
        Book book = bookService.findByLibraryIdAndBookId(libId, bookId);
        if (book.getTitle() == null)
        {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBook(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/api/library/{libId}/book/{bookId}")
    public ResponseEntity<String> updateBook(@PathVariable int libId, @PathVariable int bookId, @RequestBody BookDTO bookDTO)
    {
        if (!isRequestDataInvalid(bookDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        boolean updated = bookService.updateBookById(libId, bookId, bookDTO);
        if (updated)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isRequestDataInvalid (BookDTO request) {
        return request.getTitle() != null && request.getAuthor() != null && request.getPublisher() != null &&
                !request.getTitle().equals("") && !request.getAuthor().equals("") && !request.getPublisher().equals("");
    }
}
