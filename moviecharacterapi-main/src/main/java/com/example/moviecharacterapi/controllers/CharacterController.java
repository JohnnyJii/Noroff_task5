package com.example.moviecharacterapi.controllers;

import com.example.moviecharacterapi.models.Character;
import com.example.moviecharacterapi.models.Genre;
import com.example.moviecharacterapi.models.Movie;
import com.example.moviecharacterapi.repositories.CharacterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/characters")
public class CharacterController {
    private final CharacterRepository characterRepository;

    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    //CRUD
    @GetMapping()
    public ResponseEntity<List<Character>> getAllCharacters() {
        HttpStatus status;
        List<Character> characters = characterRepository.findAll();
        if (characters.size() == 0) {
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(characters, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Character> getCharacter(@PathVariable Long id) {
        Character returnChar = new Character();
        HttpStatus status;
        // We first check if the Library exists, this saves some computing time.
        if (characterRepository.existsById(id)) {
            status = HttpStatus.OK;
            returnChar = characterRepository.findById(id).get();
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(returnChar, status);
    }

    @PostMapping
    public ResponseEntity<Character> addCharacter(@RequestBody Character character) {
        Character returnChar = characterRepository.save(character);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(returnChar, status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Character> updateCharacter(@PathVariable Long id, @RequestBody Character character) {
        Character returnChar = new Character();
        HttpStatus status;
        /*
         We want to check if the request body matches what we see in the path variable.
         This is to ensure some level of security, making sure someone
         hasn't done some malicious stuff to our body.
        */
        if (!id.equals(character.getCharacterId())) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(returnChar, status);
        }
        returnChar = characterRepository.save(character);
        status = HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(returnChar, status);
    }

    //Deletes the character
    @DeleteMapping("/{id}")
    public ResponseEntity<Character> deleteCharacter(@PathVariable Long id) {
        // checks if it exists
        if (characterRepository.existsById(id)) {
            Character character = characterRepository.findById(id).get();

            Set<Movie> movies = character.getMovies();
            for (Movie movie : movies) {
                movie.getCharacters().remove(character);
            }
            characterRepository.delete(character);
            return new ResponseEntity<>(character, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
