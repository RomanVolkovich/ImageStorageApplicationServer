package com.volkovich.ImageStorageApplication.service;

import com.volkovich.ImageStorageApplication.domain.Album;
import com.volkovich.ImageStorageApplication.domain.Picture;
import com.volkovich.ImageStorageApplication.exception.LogicException;
import com.volkovich.ImageStorageApplication.repository.AlbumRepository;
import com.volkovich.ImageStorageApplication.repository.PictureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Transactional
public class PictureLogic {

    private final PictureRepository pictureRepository;
    private final AlbumRepository albumRepository;

    @Autowired
    public PictureLogic(PictureRepository pictureRepository, AlbumRepository albumRepository) {
        this.pictureRepository = pictureRepository;
        this.albumRepository = albumRepository;
    }

    public Picture storePicture(Long albumId, MultipartFile file) throws LogicException, IOException {

        if (Objects.equals(albumId, null)) {
            throw new LogicException("Не передан ид альбома, в который сохраняется изображение");
        }

        if (Objects.equals(file, null) ||
            Objects.equals(file.getBytes(), null) ||
            Objects.equals(file.getOriginalFilename(), null) ||
            Objects.equals(file.getContentType(), null)) {

            throw new LogicException("Ошибка при передаче изображения на сохранение");
        }

        if (file.getSize() > 2000000) {
            throw new LogicException("Размер файла не должен превышать 2Мб");
        }

        if (!(Objects.equals(file.getContentType(), "image/jpeg") ||
            Objects.equals(file.getContentType(), "image/png"))) {

            throw new LogicException("Сервис поддерживает только png или jpeg формат");
        }

        return pictureRepository.save(new Picture(
            null, file.getOriginalFilename(), file.getBytes(), file.getContentType(),
            albumRepository.findById(albumId)
                .orElseThrow(() -> new LogicException("Не найден альбом, в который сохраняется изображение"))));
    }

    public Picture getPicture(Long id) throws LogicException {

        if (Objects.equals(id, null)) {
            throw new LogicException("Необходимо передать ид картинки для поиска");
        }

        return pictureRepository.findById(id)
            .orElseThrow(() -> new LogicException(String.format("Не найдена картинка с ид %s", id)));
    }

    public List<Picture> getPicturesByAlbumId(Long albumId) throws LogicException {

        if (Objects.equals(albumId, null)) {
            throw new LogicException("Необходимо указать ид альбома для поиска изображений");
        }

        return pictureRepository.findByAlbumId(albumId);
    }

    public void deletePicture(Long pictureId) throws LogicException {

        if (Objects.equals(pictureId, null)) {
            throw new LogicException("Необходимо указать ид изображения для удаления");
        }

        try {
            pictureRepository.deleteById(pictureId);
        } catch (EmptyResultDataAccessException e) {
            throw new LogicException("Попытка удаления несуществующего ид изображения");
        }
    }

    public Picture getRandomPictureByAlbumId(Album album) throws LogicException {

        if (Objects.equals(album.getId(), null)) {
            throw new LogicException("Не передан ид альбома");
        }

        List<Picture> picturesInAlbum = pictureRepository.findByAlbumId(album.getId());

        if (picturesInAlbum.size() == 0) {
            return null;
        }

        return picturesInAlbum.get(new Random().nextInt(picturesInAlbum.size()));
    }
}
