package com.example.moviecharacterapi.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private String year;

    @Column(name = "image")
    private String image;

    @Column(name = "trailer")
    private String trailer;

    @ManyToMany()
    @JoinTable(name = "movie_has_character",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "character_id")}
    )
    public Set<Character> characters;

    @ManyToMany()
    @JoinTable(name = "movie_has_genre",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    public Set<Genre> genres;

    @ManyToOne
    @JoinColumn(name = "director_id")
    public Director director;

    @ManyToOne
    @JoinColumn(name = "franchise_id")
    public Franchise franchise;

    @JsonGetter("characters")
    public Set<String> getJsonCharacters() {
        if (characters != null)
            return characters.stream().map(character -> "/api/v1/characters/" + character.getCharacterId()).collect(Collectors.toSet());
        return null;
    }

    @JsonGetter("genres")
    public Set<String> getJsonGenres() {
        if (genres != null)
            return genres.stream().map(genre -> "/api/v1/genres/" + genre.getGenreId()).collect(Collectors.toSet());
        return null;
    }

    @JsonGetter("franchise")
    public String getJsonFranchise() {
        if (franchise != null)
            return "/api/v1/franchises/" + franchise.getFranchiseId();
        return null;
    }

    @JsonGetter("director")
    public String getJsonDirector() {
        if (director != null)
            return "/api/v1/directors/" + director.getDirectorId();
        return null;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Franchise getFranchise() {
        return franchise;
    }

    public void setFranchise(Franchise franchise) {
        this.franchise = franchise;
    }
}
