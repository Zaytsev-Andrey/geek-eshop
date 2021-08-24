package ru.geekbrains.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.persist.PictureRepository;
import ru.geekbrains.persist.model.Picture;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class PictureServiceFileImp implements PictureService {

    private final Logger logger = LoggerFactory.getLogger(PictureServiceFileImp.class);

    @Value("${picture.storage.path}")
    private String storagePath;

    private PictureRepository pictureRepository;

    @Autowired
    public PictureServiceFileImp(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Optional<String> getPictureContentTypeById(long id) {
        return pictureRepository.findById(id)
                .map(Picture::getContentType);
    }

    @Override
    public Optional<byte[]> getPictureDataById(long id) {
        return pictureRepository.findById(id)
                .map(picture -> Path.of(storagePath, picture.getStorageUUID()))
                .filter(Files::exists)
                .map(path -> {
                    try {
                        return Files.readAllBytes(path);
                    } catch (IOException e) {
                        logger.error("Can not read picture file with id: " + id, e);
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public String createPicture(byte[] picture) {
        String fileName = UUID.randomUUID().toString();
        try (OutputStream os = Files.newOutputStream(Path.of(storagePath, fileName))) {
            os.write(picture);
        } catch (IOException e) {
            logger.error("Can not write file", e);
            throw new RuntimeException(e);
        }
        return fileName;
    }

    @Override
    @Transactional
    public void deletePictureById(long id) {
        Optional<Picture> pictureOpt = pictureRepository.findById(id);
        if (pictureOpt.isPresent()) {
            try {
                pictureRepository.delete(pictureOpt.get());
                Files.delete(Path.of(storagePath, pictureOpt.get().getStorageUUID()));
            } catch (IOException e) {
                logger.error("Picture with id: {} was not deleted", id);
            }
        }
    }
}
