package com.volkovich.ImageStorageApplication;

import com.volkovich.ImageStorageApplication.domain.Picture;
import com.volkovich.ImageStorageApplication.exception.LogicException;
import com.volkovich.ImageStorageApplication.resource.AlbumResource;
import com.volkovich.ImageStorageApplication.resource.PictureResource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
// скриптом у нас внесены 2 альбома
// и 1 картинка (для альбома с ид 1)
@Sql("/db/album.sql")
@Sql("/db/picture.sql")
class PictureLogicTest {

    @Autowired
    private AlbumResource albumResource;
    @Autowired
    private PictureResource pictureResource;

    @Test
    public void getById_Test() throws Exception {

        try {
            pictureResource.getById(null);
        } catch (LogicException le) {
            assertEquals(le.getMessage(), "Необходимо передать ид картинки для поиска");
        }

        try {
            pictureResource.getById(0L);
        } catch (LogicException le) {
            assertEquals(le.getMessage(), "Не найдена картинка с ид 0");
        }
    }

    @Test
    public void getByAlbumId_Test() throws Exception {

        try {
            pictureResource.getByAlbumId(null);
        } catch (LogicException le) {
            assertEquals(le.getMessage(), "Необходимо указать ид альбома для поиска изображений");
        }

        assertEquals(pictureResource.getByAlbumId(0L).size(), 0);
        assertEquals(pictureResource.getByAlbumId(1L).size(), 1);
        assertEquals(pictureResource.getByAlbumId(2L).size(), 0);
    }

    @Test
    public void getRandomByAlbumId_Test() throws Exception {

        assertEquals(pictureResource.getRandomByAlbumId(1L).getName(), "имя файла.png");
        assertNull(pictureResource.getRandomByAlbumId(2L));
    }

    @Test
    public void deletePicture_Test() throws Exception {

        try {
            pictureResource.delete(null);
        } catch (LogicException le) {
            assertEquals(le.getMessage(), "Необходимо указать ид изображения для удаления");
        }

        try {
            pictureResource.delete(0L);
        } catch (LogicException e) {
            assertEquals(e.getMessage(), "Попытка удаления несуществующего ид изображения");
        }

        albumResource.deleteAlbum(1L);
    }

    @Test
    public void uploadFile_Test() throws Exception {

        Picture picture = pictureResource.getById(1L);

        try {
            pictureResource.uploadPicture(null, null);
        } catch (LogicException e) {
            assertEquals(e.getMessage(), "Не передан ид альбома, в который сохраняется изображение");
        }

        try {
            pictureResource.uploadPicture(2L, null);
        } catch (LogicException e) {
            assertEquals(e.getMessage(), "Ошибка при передаче изображения на сохранение");
        }

        MultipartFile file = new MockMultipartFile(
            "fileData",
            "новый файл",
            null,
            picture.getPicByte());

        try {
            pictureResource.uploadPicture(2L, file);
        } catch (LogicException e) {
            assertEquals(e.getMessage(), "Ошибка при передаче изображения на сохранение");
        }

        file = new MockMultipartFile(
            "fileData",
            "новый файл",
            "text/plain",
            picture.getPicByte());

        try {
            pictureResource.uploadPicture(2L, file);
        } catch (LogicException e) {
            assertEquals(e.getMessage(), "Сервис поддерживает только png или jpeg формат");
        }

        file = new MockMultipartFile(
            "fileData",
            "новый файл",
            picture.getContentType(),
            picture.getPicByte());

        pictureResource.uploadPicture(2L, file);
    }
}
