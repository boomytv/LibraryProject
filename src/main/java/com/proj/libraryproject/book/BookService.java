package com.proj.libraryproject.book;

import com.proj.libraryproject.library.Library;
import com.proj.libraryproject.library.LibraryDTO;
import com.proj.libraryproject.library.LibraryRepository;
import com.proj.libraryproject.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository repository;

    public List<Book> findByLibraryId(int libraryId) {
        List<Book> bookList = new ArrayList<Book>();
        for(Book book : repository.findAll())
        {
            if (book.getLibrary().getId() == libraryId)
            {
                bookList.add(book);
            }
        }
        return bookList;
    }
    public Book findByLibraryIdAndBookId(int libraryId, int bookId) {
        Book book = new Book();
        for(Book bookCheck : repository.findAll())
        {
            if (bookCheck.getLibrary().getId() == libraryId)
            {
                if (bookCheck.getId() == bookId)
                {
                    book = repository.findById(bookId).orElse(null);
                }
            }
        }
        return book;
    }

    public boolean updateBookById(int libId, int bookId, BookDTO bookDTO)
    {
        Book bookToUpdate = findByLibraryIdAndBookId(libId, bookId);
        if (bookToUpdate.getAuthor() != null)
        {
            bookToUpdate.setAuthor(bookDTO.getAuthor());
            bookToUpdate.setTitle(bookDTO.getTitle());
            bookToUpdate.setPublisher(bookDTO.getPublisher());
            repository.save(bookToUpdate);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteBook(Book book) { repository.delete(book); }
    public Book insertBook(Book book) { return repository.saveAndFlush(book); }
}
