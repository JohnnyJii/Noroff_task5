package com.example.moviecharacterapi.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "directors")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long directorId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "director")
    private Set<Movie> movies;

    @JsonGetter("movies")
    public Set<String> getJsonMovies() {
        if (movies != null)
            return movies.stream().map(movie -> "/api/v1/movies/" + movie.getMovieId()).collect(Collectors.toSet());
        return null;
    }


    public long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(long directorId) {
        this.directorId = directorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
