package ru.geekbrains.persist.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "userWithRolesEntityGraph",
                attributeNodes = {
                        @NamedAttributeNode("roles")
                }
        ),
        @NamedEntityGraph(
                name = "userWithOrdersEntityGraph",
                attributeNodes = {
                        @NamedAttributeNode("orders")
                }
        )
})
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(
                        name = "idx_email",
                        columnList = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractPersistentObject {

    @Column(name = "firstname",
            nullable = false)
    private String firstname;

    @Column(name = "lastname",
            nullable = false)
    private String lastname;

    @Column(name = "email",
            nullable = false,
            unique = true)
    private String email;

    @Column(name = "password",
            nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name = "fk_user_id")),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    public User(UUID id, String firstname, String lastname, String email, String password, Set<Role> roles) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
