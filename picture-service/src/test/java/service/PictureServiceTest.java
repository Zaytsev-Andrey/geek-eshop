package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.repository.PictureRepository;
import ru.geekbrains.service.PictureService;
import ru.geekbrains.service.PictureServiceFileImp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PictureServiceTest {

    private PictureService pictureService;

    private PictureRepository pictureRepository;

    private String storagePath = "@project.basedir@/storage";

    @BeforeEach
    public void init() {
        pictureRepository = mock(PictureRepository.class);
        pictureService = new PictureServiceFileImp(pictureRepository, storagePath);
    }

    @Test
    public void testPictureService() {
        byte[] expectedPicture = {1};
        String uuid = pictureService.createPicture(expectedPicture);
        assertFalse(uuid.isBlank());
        assertTrue(Files.exists(Path.of(storagePath, uuid)));

        when(pictureRepository.findById(1L))
                .thenReturn(Optional.of(new Picture("", "", uuid, null)));

        Optional<byte[]> optPicture = pictureService.getPictureDataById(1);
        assertNotNull(optPicture.get());
        assertArrayEquals(expectedPicture, optPicture.get());

        pictureService.deletePictureById(1);

        optPicture = pictureService.getPictureDataById(1);
        assertFalse(optPicture.isPresent());
        assertFalse(Files.exists(Path.of(storagePath, uuid)));
    }
}
