package com.volkovich.ImageStorageApplication;

import com.volkovich.ImageStorageApplication.domain.Album;
import com.volkovich.ImageStorageApplication.domain.Picture;
import com.volkovich.ImageStorageApplication.exception.LogicException;
import com.volkovich.ImageStorageApplication.resource.AlbumResource;
import com.volkovich.ImageStorageApplication.resource.PictureResource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
// скриптом у нас внесены 2 альбома
// и 1 картинка (для альбома с ид 1)
@Sql("/db/album.sql")
@Sql("/db/picture.sql")
class AlbumLogicTest {

	@Autowired
	private AlbumResource albumResource;
	@Autowired
	private PictureResource pictureResource;

	@Test
	public void storeAndEditAlbum_Test() throws Exception {

		try {
			albumResource.newAlbum(null);
		} catch (LogicException le) {
			assertEquals(le.getMessage(), "Возникли ошибки при передаче альбома на сохранение");
		}

		Album album = new Album(null, "", "какое-то описание", new HashSet<>());
		try {
			albumResource.newAlbum(album);
		} catch (LogicException le) {
			assertEquals(le.getMessage(), "Наименование альбома обязательно к заполнению");
		}

		album.setName("третий альбом");

		album = albumResource.newAlbum(album);

		album.setId(0L);

		try {
			albumResource.editAlbum(album);
		} catch (LogicException le) {
			assertEquals(le.getMessage(), "Не найден альбом с указанным ид");
		}

		album.setName("");
		album.setDescription("новое описание");

		try {
			albumResource.editAlbum(album);
		} catch (LogicException le) {
			assertEquals(le.getMessage(), "Наименование альбома обязательно к заполнению");
		}

		album.setName("третий альбом");
		albumResource.editAlbum(album);
	}

	@Test
	public void deleteAlbum_Test() throws Exception {
		Album album = albumResource.newAlbum(new Album(null, "третий альбом", "какое-то описание", new HashSet<>()));

		try {
			albumResource.deleteAlbum(null);
		} catch (LogicException le) {
			assertEquals(le.getMessage(), "Не передан ид альбома для удаления");
		}

		try {
			albumResource.deleteAlbum(0L);
		} catch (LogicException e) {
			assertEquals(e.getMessage(), "Попытка удаления несуществующего ид альбома");
		}

		albumResource.deleteAlbum(album.getId());
	}
}
