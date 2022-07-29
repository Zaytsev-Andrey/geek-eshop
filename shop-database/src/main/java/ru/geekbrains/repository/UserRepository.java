package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.geekbrains.persist.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph("userWithRolesEntityGraph")
    Optional<User> findById(UUID id);

    @EntityGraph("userWithRolesEntityGraph")
    Optional<User> findByEmail(String email);

    @EntityGraph("userWithOrdersEntityGraph")
    Optional<User> findUserByEmail(String email);
    
    void deleteById(UUID id);
}
