package ru.geekbrains.persist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.model.Picture;


public interface PictureRepository extends JpaRepository<Picture, UUID> {
}
