package com.example.moviecharacterapi.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "franchises")
public class Franchise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long franchiseId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "franchise")
    private Set<Movie> movies;

    @JsonGetter("movies")
    public Set<String> getJsonMovies() {
        if (movies != null)
            return movies.stream().map(movie -> "/api/v1/movies/" + movie.getMovieId()).collect(Collectors.toSet());
        return null;
    }


    public long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
