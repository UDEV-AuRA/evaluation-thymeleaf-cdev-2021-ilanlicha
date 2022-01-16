package com.ipiecoles.java.eval.th330.repository;

import com.ipiecoles.java.eval.th330.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
