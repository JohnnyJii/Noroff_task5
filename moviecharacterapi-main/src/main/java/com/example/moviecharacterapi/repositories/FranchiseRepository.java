package com.example.moviecharacterapi.repositories;

import com.example.moviecharacterapi.models.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {
}
