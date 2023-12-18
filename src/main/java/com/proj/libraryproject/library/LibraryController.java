package com.proj.libraryproject.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.libraryproject.utils.UserService;
import com.proj.libraryproject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping(path = "api/libraries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Library>> getAllLibraries() {
        List<Library> libraries = libraryService.selectAllLibraries();
        if (libraries.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(libraries, HttpStatus.OK);
    }
    @GetMapping(path = "api/library/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Library> getLibrary(@PathVariable int id) {
        Library library = libraryService.selectLibrary(id);
        if(library == null)
        {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(library, HttpStatus.OK);
    }
    @PostMapping(path = "api/library", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addLibrary(@RequestBody String libraryString) throws IOException {
        LibraryDTO libraryToAdd = objectMapper.readValue(libraryString, LibraryDTO.class);
        if(!isRequestDataInvalid(libraryToAdd))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        List<Library> allLibraries = libraryService.selectAllLibraries();
        for(Library checkLibrary : allLibraries) {
            if (Objects.equals(checkLibrary.getStreet(), libraryToAdd.getStreet())) {
                return ResponseEntity.badRequest().build();
            }
        }
        Library library = Utils.convertToLibrary(libraryToAdd);
        libraryService.insertLibrary(library);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping (path = "api/library/{id}")
    public ResponseEntity<String> deleteLibrary(@PathVariable("id") int id) {
        Library library = libraryService.selectLibrary(id);
        if (library == null)
        {
            return ResponseEntity.notFound().build();
        }
        try {
            libraryService.deleteLibrary(library);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There are linked objects with Foreign Key to this class");
        }
    }
    @PutMapping (path = "api/library/{id}")
    public ResponseEntity<String> updateLibrary(@PathVariable("id") int id, @RequestBody LibraryDTO libraryDTO) {
        if(!isRequestDataInvalid(libraryDTO))
        {
            return ResponseEntity.unprocessableEntity().build();
        }
        boolean updated = libraryService.updateLibraryById(id, libraryDTO);
        if (updated)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isRequestDataInvalid (LibraryDTO request) {
        return request.getName() != null && request.getDirector() != null && request.getStreet() != null && request.getPhone() != null &&
                !request.getName().equals("") && !request.getDirector().equals("") && !request.getStreet().equals("") && !request.getPhone().equals("");
    }
}
