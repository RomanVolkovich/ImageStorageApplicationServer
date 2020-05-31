package com.volkovich.ImageStorageApplication.resource;

import com.volkovich.ImageStorageApplication.domain.Picture;
import com.volkovich.ImageStorageApplication.exception.LogicException;
import com.volkovich.ImageStorageApplication.repository.AlbumRepository;
import com.volkovich.ImageStorageApplication.repository.PictureRepository;
import com.volkovich.ImageStorageApplication.service.PictureLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/picture")
@CrossOrigin("http://localhost:8001")
public class PictureResource {

    private final PictureRepository pictureRepository;
    private final PictureLogic pictureLogic;
    private final AlbumRepository albumRepository;

    public PictureResource(PictureRepository pictureRepository,
                           PictureLogic pictureLogic,
                           AlbumRepository albumRepository) {
        this.pictureRepository = pictureRepository;
        this.pictureLogic = pictureLogic;
        this.albumRepository = albumRepository;
    }

    @GetMapping("/{id:-?[\\d]+}")
    public Picture getById(@PathVariable Long id) throws LogicException {

        return pictureLogic.getPicture(id);
    }

    @GetMapping("/album/{albumId:-?[\\d]+}")
    public List<Picture> getByAlbumId(@PathVariable Long albumId) throws LogicException {
        return pictureLogic.getPicturesByAlbumId(albumId);
    }

    @GetMapping("/album/{albumId:-?[\\d]+}/random")
    public Picture getRandomByAlbumId(@PathVariable Long albumId) throws LogicException {

        return pictureLogic.getRandomPictureByAlbumId(albumRepository.findById(albumId)
            .orElseThrow(() -> new LogicException("Не найден запрашиваемый альбом")));
    }

    @GetMapping("/delete/{id:-?[\\d]+}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) throws LogicException {

        pictureLogic.deletePicture(id);

        return ResponseEntity.ok(HttpStatus.OK.value());
    }

    @PostMapping(value = "/upload/{albumId:-?[\\d]+}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin
    public Picture uploadPicture(@PathVariable Long albumId, @RequestParam("upload_file") MultipartFile file) throws LogicException, IOException {

        return pictureLogic.storePicture(albumId, file);
    }
}
