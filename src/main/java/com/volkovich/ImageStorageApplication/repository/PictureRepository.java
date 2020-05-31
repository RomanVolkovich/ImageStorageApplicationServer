package com.volkovich.ImageStorageApplication.repository;

import com.volkovich.ImageStorageApplication.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findByAlbumId(Long albumId);
}
