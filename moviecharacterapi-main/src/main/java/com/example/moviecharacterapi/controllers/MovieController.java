package com.example.moviecharacterapi.controllers;

import com.example.moviecharacterapi.models.Character;
import com.example.moviecharacterapi.models.Genre;
import com.example.moviecharacterapi.models.Movie;
import com.example.moviecharacterapi.repositories.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {

        this.movieRepository = movieRepository;
    }

    //CRUD
    @GetMapping()
    public ResponseEntity<List<Movie>> getAllMovies() {
        HttpStatus status;
        List<Movie> movies = movieRepository.findAll();
        if (movies.size() == 0) {
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(movies, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        boolean exists = movieRepository.existsById(id);
        Movie returnMovie = null;
        if (exists) {
            returnMovie = movieRepository.findById(id).get();
            status = HttpStatus.OK;
            return new ResponseEntity<>(returnMovie, status);
        }
        return new ResponseEntity<>(null, status);
    }

    @GetMapping("/{id}/characters")
    public ResponseEntity<Set<Character>> getMovieCharacters(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        boolean exists = movieRepository.existsById(id);
        if (exists) {
            Movie movie = movieRepository.findById(id).get();
            Set<Character> characters = movie.getCharacters();
            status = HttpStatus.OK;
            return new ResponseEntity<>(characters, status);
        }
        return new ResponseEntity<>(null, status);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        HttpStatus status = HttpStatus.CREATED;
        Movie returnMovie = movieRepository.save(movie);
        return new ResponseEntity<>(returnMovie, status);
    }

    @PutMapping
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        Movie returnMovie = null;
        if (movieRepository.existsById(movie.getMovieId())) {
            returnMovie = movieRepository.save(movie);
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(returnMovie, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        if (movieRepository.existsById(id)) {
            Movie movie = movieRepository.findById(id).get();
            Set<Character> characters = movie.getCharacters();
            for (Character character : characters) {
                character.getMovies().remove(movie);
            }

            Set<Genre> genres = movie.getGenres();
            for (Genre genre : genres) {
                genre.getMovies().remove(movie);
            }

            if (movie.getDirector() != null) {
                movie.getDirector().getMovies().remove(movie);
            }
            if (movie.getFranchise() != null) {
                movie.getFranchise().getMovies().remove(movie);
            }

            movieRepository.delete(movie);
            status = HttpStatus.OK;
            return new ResponseEntity<>(movie, status);

        }
        return new ResponseEntity<>(null, status);
    }


}
