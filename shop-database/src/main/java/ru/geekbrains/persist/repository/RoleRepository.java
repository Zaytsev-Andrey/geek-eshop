package ru.geekbrains.persist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
