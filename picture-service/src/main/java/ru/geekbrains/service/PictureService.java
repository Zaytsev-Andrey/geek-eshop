package ru.geekbrains.service;

import java.util.Optional;

public interface PictureService {

    Optional<String> getPictureContentTypeById(long id);

    byte[] downloadPictureById(long id);

    String savePicture(byte[] picture);

    void deletePictureById(long id);
}
