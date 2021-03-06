package ru.geekbrains.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.Picture;


public interface PictureRepository extends JpaRepository<Picture, UUID> {
}
