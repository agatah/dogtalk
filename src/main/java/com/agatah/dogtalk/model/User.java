package com.agatah.dogtalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(name = "user_email_unique", columnNames = "email")})
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BehavioristProfile behavioristProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PetOwnerProfile petOwnerProfile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Collection<Role> roles = new ArrayList<>();


    public User setBehavioristProfile(BehavioristProfile behavioristProfile) {
        this.behavioristProfile = behavioristProfile;
        behavioristProfile.setUser(this);
        return this;
    }

    public User setPetOwnerProfile(PetOwnerProfile petOwnerProfile) {
        this.petOwnerProfile = petOwnerProfile;
        petOwnerProfile.setUser(this);
        return this;
    }


    //EqualsAndHashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 562048007;
    }
}
