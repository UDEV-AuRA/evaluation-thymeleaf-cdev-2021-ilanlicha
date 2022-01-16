package com.ipiecoles.java.eval.th330.repository;

import com.ipiecoles.java.eval.th330.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Boolean existsByNameIgnoreCase(String name);
    Page<Artist> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
