package ru.geekbrains.persist;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role",
            nullable = false)
    private String role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id",
                    foreignKey = @ForeignKey(name = "fk_role_id")),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
