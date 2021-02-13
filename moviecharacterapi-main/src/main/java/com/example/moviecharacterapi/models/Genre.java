package com.example.moviecharacterapi.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long genreId;

    @Column(name = "type")
    private String type;

    @ManyToMany
    @JoinTable(name = "movie_has_genre",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "movie_id")}
    )
    public Set<Movie> movies;

    @JsonGetter("movies")
    public Set<String> getJsonMovies() {
        if (movies != null)
            return movies.stream().map(movie -> "/api/v1/movies/" + movie.getMovieId()).collect(Collectors.toSet());
        return null;
    }


    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
