package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.persist.Picture;
import ru.geekbrains.repository.PictureRepository;
import ru.geekbrains.service.PictureService;
import ru.geekbrains.service.PictureServiceFileImp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PictureServiceTest {

    private UUID id;

    private PictureService pictureService;

    private PictureRepository pictureRepository;

    private String storagePath = "/Users/yuliya/projects/geek-eshop/storage";
//    private String storagePath = "@project.basedir@/storage";

    @BeforeEach
    public void init() {
        pictureRepository = mock(PictureRepository.class);
        pictureService = new PictureServiceFileImp(pictureRepository, storagePath);
        id = UUID.randomUUID();
    }

    @Test
    public void testPictureService() {
        byte[] expectedPicture = {1};
        String uuid = pictureService.uploadPicture(expectedPicture);
        assertFalse(uuid.isBlank());
        assertTrue(Files.exists(Path.of(storagePath, uuid)));

        when(pictureRepository.findById(id))
                .thenReturn(Optional.of(new Picture("", "", uuid)));

        byte[] optPicture = pictureService.downloadPictureById(id);
        assertEquals(1, optPicture.length);
        assertArrayEquals(expectedPicture, optPicture);

        pictureService.deletePictureById(id);

        assertFalse(Files.exists(Path.of(storagePath, uuid)));
    }
}
