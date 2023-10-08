package com.proj.libraryproject.library;

import com.proj.libraryproject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    LibraryRepository repository;

    public List<Library> selectAllLibraries() { return repository.findAll(); }
    public Library selectLibrary(int id) { return repository.findById(id).orElse(null); }
    public void deleteLibrary(Library library) { repository.delete(library); }
    public Library insertLibrary(Library library) { return repository.saveAndFlush(library); }

    public boolean updateLibraryById(int id, LibraryDTO libraryDTO)
    {
        Library libraryToUpdate = repository.findById(id).orElse(null);
        assert libraryToUpdate != null;
        if (libraryToUpdate.getStreet() != null)
        {
            libraryToUpdate.setPhone(libraryDTO.getPhone());
            libraryToUpdate.setName(libraryDTO.getName());
            libraryToUpdate.setDirector(libraryDTO.getDirector());
            libraryToUpdate.setPhone(libraryDTO.getPhone());
            repository.save(libraryToUpdate);
            return true;
        }
        else {
            return false;
        }
    }
}
