package com.volkovich.ImageStorageApplication.repository;

import com.volkovich.ImageStorageApplication.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
