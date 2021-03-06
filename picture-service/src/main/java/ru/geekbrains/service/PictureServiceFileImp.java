package ru.geekbrains.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.repository.PictureRepository;
import ru.geekbrains.persist.Picture;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class PictureServiceFileImp implements PictureService {

    private final Logger logger = LoggerFactory.getLogger(PictureServiceFileImp.class);

    private final String storagePath;

    private final PictureRepository pictureRepository;

    @Autowired
    public PictureServiceFileImp(PictureRepository pictureRepository,
                                 @Value("${picture.storage.path}") String storagePath) {
        this.pictureRepository = pictureRepository;
        this.storagePath = storagePath;
    }

    @Override
    public Optional<String> getPictureContentTypeById(UUID id) {
        return pictureRepository.findById(id)
                .map(Picture::getContentType);
    }

	@Override
    public byte[] downloadPictureById(UUID id) {
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
                })
                .orElse(new byte[0]);
    }

    @Override
    public String uploadPicture(byte[] picture) {
        String fileName = UUID.randomUUID().toString();
        logger.info("Storage path: '{}'", storagePath);
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
    public void deletePictureById(UUID id) {
        Optional<Picture> pictureOpt = pictureRepository.findById(id);
        if (pictureOpt.isPresent()) {
            try {
                Files.delete(Path.of(storagePath, pictureOpt.get().getStorageUUID()));
                pictureRepository.delete(pictureOpt.get());
            } catch (IOException e) {
                logger.error("Picture with id: {} was not deleted", id);
            }
        }
    }
}
