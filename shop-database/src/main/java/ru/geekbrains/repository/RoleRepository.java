package ru.geekbrains.repository;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Set<Role> findRoleByIdIn(Set<UUID> ids);
}
