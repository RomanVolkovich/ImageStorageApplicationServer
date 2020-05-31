package com.volkovich.ImageStorageApplication.service;

import com.volkovich.ImageStorageApplication.domain.Album;
import com.volkovich.ImageStorageApplication.exception.LogicException;
import com.volkovich.ImageStorageApplication.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlbumLogic {

    private final AlbumRepository albumRepository;
    private final PictureLogic pictureLogic;

    @Autowired
    public AlbumLogic(AlbumRepository albumRepository, PictureLogic pictureLogic) {
        this.albumRepository = albumRepository;
        this.pictureLogic = pictureLogic;
    }

    public List<Album> getAll() {

        List<Album> albums = albumRepository.findAll();

        albums.forEach(album -> {
            try {
                album.setPictures(Collections.singleton(pictureLogic.getRandomPictureByAlbumId(album)));
            } catch (LogicException e) {
                e.printStackTrace();
            }
        });

        return albums;
    }

    public Album createAlbum(Album album) throws LogicException {

        validateAlbum(album);

        return albumRepository.save(album);
    }

    public Album editAlbum(Album album) throws LogicException {

        validateAlbum(album);

        return albumRepository.save(album);
    }

    public void deleteAlbum(Long id) throws LogicException {

        if (Objects.equals(id, null)) {
            throw new LogicException("Не передан ид альбома для удаления");
        }

        try {
            albumRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new LogicException("Попытка удаления несуществующего ид альбома");
        }
    }

    private void validateAlbum(Album album) throws LogicException {

        if (Objects.equals(album, null)) {

            throw new LogicException("Возникли ошибки при передаче альбома на сохранение");
        }

        if (album.getDescription().length() > 254) {

            throw new LogicException("Описание не должно быть длинее 255 символов");
        }

        if (Objects.equals(album.getName(), null) ||
            Objects.equals(album.getName(), "")) {

            throw new LogicException("Наименование альбома обязательно к заполнению");
        }

    }
}
