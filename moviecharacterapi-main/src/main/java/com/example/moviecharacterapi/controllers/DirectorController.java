package com.example.moviecharacterapi.controllers;


import com.example.moviecharacterapi.models.Director;
import com.example.moviecharacterapi.models.Movie;
import com.example.moviecharacterapi.repositories.DirectorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/directors")
public class DirectorController {

    private final DirectorRepository directorRepository;

    public DirectorController(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Director>> getAllDirectors() {
        HttpStatus status;
        List<Director> directors = directorRepository.findAll();
        if (directors.size() == 0) {
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(directors, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirector(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        boolean exists = directorRepository.existsById(id);
        Director returnDirector = null;
        if (exists) {
            returnDirector = directorRepository.findById(id).get();
            status = HttpStatus.OK;
            return new ResponseEntity<>(returnDirector, status);
        }
        return new ResponseEntity<>(null, status);
    }

    @PostMapping
    public ResponseEntity<Director> addDirector(@RequestBody Director director) {
        HttpStatus status = HttpStatus.CREATED;
        Director returnDirector = directorRepository.save(director);
        return new ResponseEntity<>(returnDirector, status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Director> updateDirector(@PathVariable Long id, @RequestBody Director director) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        Director returnDirector = null;
        if (!id.equals(director.getDirectorId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(returnDirector, status);
        }
        if (directorRepository.existsById(director.getDirectorId())) {
            returnDirector = directorRepository.save(director);
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(returnDirector, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Director> deleteDirector(@PathVariable Long id) {
        // checks if it exists
        if (directorRepository.existsById(id)) {
            Director director = directorRepository.findById(id).get();
            Set<Movie> movies = director.getMovies();
            for (Movie movie : movies) {
                movie.setDirector(null);
            }
            directorRepository.delete(director);
            return new ResponseEntity<>(director, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
