package com.example.moviecharacterapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.moviecharacterapi.models.Movie;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
