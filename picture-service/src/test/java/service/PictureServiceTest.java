package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.repository.PictureRepository;
import ru.geekbrains.service.PictureService;
import ru.geekbrains.service.PictureServiceFileImp;

import static org.mockito.Mockito.mock;

public class PictureServiceTest {

    private PictureService pictureService;

    private PictureRepository pictureRepository;

    private String storagePath = "/home/andrey/projects/geekbrains/level7/geek-eshop/storage";
//    private String storagePath = "@project.basedir@/storage";

    @BeforeEach
    public void init() {
        pictureRepository = mock(PictureRepository.class);
        pictureService = new PictureServiceFileImp(pictureRepository, storagePath);
    }

    @Test
    public void testPictureService() {
//        byte[] expectedPicture = {1};
//        String uuid = pictureService.uploadPicture(expectedPicture);
//        assertFalse(uuid.isBlank());
//        assertTrue(Files.exists(Path.of(storagePath, uuid)));
//
//        when(pictureRepository.findById(1L))
//                .thenReturn(Optional.of(new Picture("", "", uuid)));
//
//        byte[] optPicture = pictureService.downloadPictureById(1);
//        assertEquals(1, optPicture.length);
//        assertArrayEquals(expectedPicture, optPicture);
//
//        pictureService.deletePictureById(1);
//
//        assertFalse(Files.exists(Path.of(storagePath, uuid)));
    }
}
