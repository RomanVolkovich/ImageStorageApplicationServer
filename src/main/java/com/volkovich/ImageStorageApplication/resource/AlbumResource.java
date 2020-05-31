package com.volkovich.ImageStorageApplication.resource;

import com.volkovich.ImageStorageApplication.domain.Album;
import com.volkovich.ImageStorageApplication.exception.LogicException;
import com.volkovich.ImageStorageApplication.repository.AlbumRepository;
import com.volkovich.ImageStorageApplication.service.AlbumLogic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/album")
@CrossOrigin("http://localhost:8001")
public class AlbumResource {

    private final AlbumRepository albumRepository;
    private final AlbumLogic albumLogic;

    public AlbumResource(AlbumRepository albumRepository,
                         AlbumLogic albumLogic) {
        this.albumRepository = albumRepository;
        this.albumLogic = albumLogic;
    }

    @GetMapping
    public List<Album> getAllAlbums() {
        return albumLogic.getAll();
    }

    @GetMapping("/{id}")
    public Album getAlbumById(@PathVariable Long id) throws LogicException {
        return albumRepository.findById(id)
            .orElseThrow(() -> new LogicException("Не найден альбом с указанным ид"));
    }

    @PostMapping("/new")
    public Album newAlbum(@RequestBody Album album) throws LogicException {
        return albumLogic.createAlbum(album);
    }

    @PostMapping("/edit")
    public Album editAlbum(@RequestBody Album album) throws LogicException {
        return albumLogic.editAlbum(album);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity deleteAlbum(@PathVariable Long id) throws LogicException {
        albumLogic.deleteAlbum(id);
        return ResponseEntity.ok(null);
    }
}
