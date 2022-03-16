package ru.geekbrains.service;

import java.util.Optional;
import java.util.UUID;


public interface PictureService {

    Optional<String> getPictureContentTypeById(UUID id);
    
    byte[] downloadPictureById(UUID id);

    String uploadPicture(byte[] picture);

    void deletePictureById(UUID id);
}
