package com.example.moviecharacterapi.controllers;

import com.example.moviecharacterapi.models.Genre;
import com.example.moviecharacterapi.models.Movie;
import com.example.moviecharacterapi.repositories.GenreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/genres")
public class GenreController {

    public GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Genre>> getAllGenres() {
        HttpStatus status;
        List<Genre> genres = genreRepository.findAll();
        if (genres.size() == 0) {
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(genres, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        Genre genre = new Genre();
        HttpStatus status;
        // We first check if the Library exists, this saves some computing time.
        if (genreRepository.existsById(id)) {
            status = HttpStatus.OK;
            genre = genreRepository.findById(id).get();
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(genre, status);
    }

    @PostMapping
    public ResponseEntity<Genre> addGenre(@RequestBody Genre genre) {
        Genre returnGenre = genreRepository.save(genre);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(returnGenre, status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        Genre returnGenre = new Genre();
        HttpStatus status;
        if (!id.equals(genre.getGenreId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(returnGenre, status);
        }
        returnGenre = genreRepository.save(genre);
        status = HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(returnGenre, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Genre> deleteGenre(@PathVariable Long id) {
        // checks if it exists
        if (genreRepository.existsById(id)) {
            Genre genre = genreRepository.findById(id).get();
            Set<Movie> movies = genre.getMovies();
            for (Movie movie : movies) {
                movie.setFranchise(null);
            }
            genreRepository.delete(genre);
            return new ResponseEntity<>(genre, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}

