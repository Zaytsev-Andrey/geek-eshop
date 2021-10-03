package ru.geekbrains.persist.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.persist.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @EntityGraph("userWithRolesEntityGraph")
    Optional<User> findById(Long id);

    @EntityGraph("userWithRolesEntityGraph")
    Optional<User> findByEmail(String email);

    @EntityGraph("userWithOrdersEntityGraph")
    Optional<User> findUserByEmail(String email);
}
