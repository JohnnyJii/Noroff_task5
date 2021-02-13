package com.example.moviecharacterapi.controllers;

import com.example.moviecharacterapi.models.Character;
import com.example.moviecharacterapi.models.Franchise;
import com.example.moviecharacterapi.models.Movie;
import com.example.moviecharacterapi.repositories.FranchiseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/franchises")
public class FranchiseController {
    private final FranchiseRepository franchiseRepository;

    public FranchiseController(FranchiseRepository franchiseRepository, FranchiseRepository franchiseRepository1) {
        this.franchiseRepository = franchiseRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Franchise>> getAllFranchises() {
        HttpStatus status;
        List<Franchise> franchises = franchiseRepository.findAll();
        if (franchises.size() == 0) {
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(franchises, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Franchise> getFranchise(@PathVariable Long id) {
        Franchise returnFranchise = new Franchise();
        HttpStatus status;
        if (franchiseRepository.existsById(id)) {
            status = HttpStatus.OK;
            returnFranchise = franchiseRepository.findById(id).get();
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(returnFranchise, status);
    }

    @GetMapping("/{id}/movies")
    public ResponseEntity<Set<Movie>> getFranchiseMovies(@PathVariable Long id) {
        HttpStatus status;
        Set<Movie> franchiseMovies;
        if (franchiseRepository.existsById(id)) {
            status = HttpStatus.OK;
            Franchise franchise = franchiseRepository.findById(id).get();
            franchiseMovies = franchise.getMovies();
        } else {
            franchiseMovies = null;
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(franchiseMovies, status);
    }

    @GetMapping("/{id}/characters")
    public ResponseEntity<List<Character>> getFranchiseCharacters(@PathVariable Long id) {
        HttpStatus status;
        List<Character> franchiseCharacters = new ArrayList<Character>();
        if (franchiseRepository.existsById(id)) {
            status = HttpStatus.OK;
            Franchise franchise = franchiseRepository.findById(id).get();
            Set<Movie> franchiseMovies = franchise.getMovies();
            for (Movie movie : franchiseMovies) {
                Set<Character> movieCharacters = movie.getCharacters();
                for (Character character : movieCharacters) {
                    if (!franchiseCharacters.contains(character)) {
                        franchiseCharacters.add(character);
                    }
                }
            }
        } else {
            franchiseCharacters = null;
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(franchiseCharacters, status);
    }

    @PostMapping
    public ResponseEntity<Franchise> addFranchise(@RequestBody Franchise franchise) {
        Franchise returnFranchise = franchiseRepository.save(franchise);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(returnFranchise, status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Franchise> updateFranchise(@PathVariable Long id, @RequestBody Franchise franchise) {
        Franchise returnFranchise = new Franchise();
        HttpStatus status;
        if (!id.equals(franchise.getFranchiseId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(returnFranchise, status);
        }
        returnFranchise = franchiseRepository.save(franchise);
        status = HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(returnFranchise, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Franchise> deleteFranchise(@PathVariable Long id) {
        // checks if it exists
        if (franchiseRepository.existsById(id)) {
            Franchise franchise = franchiseRepository.findById(id).get();
            Set<Movie> movies = franchise.getMovies();
            for (Movie movie : movies) {
                movie.setFranchise(null);
            }
            franchiseRepository.delete(franchise);
            return new ResponseEntity<>(franchise, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
