package ru.geekbrains.persist.model;

import lombok.*;
import ru.geekbrains.persist.model.Role;

import javax.persistence.*;
import java.util.List;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User(Long id, String firstname, String lastname, String email, String password, List<Role> roles) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
