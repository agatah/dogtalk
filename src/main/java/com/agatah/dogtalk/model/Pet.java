package com.agatah.dogtalk.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "pets")
public class Pet {

    @Id
    @SequenceGenerator(name = "pet_sequence", sequenceName = "pet_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_sequence")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private PetOwnerProfile owner;

    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pet_photos",
            joinColumns = {@JoinColumn(name = "pet_id")},
            inverseJoinColumns = {@JoinColumn(name = "photo_id")})
    private List<Photo> photos = new ArrayList<>();


    public Pet addPhoto(Photo photo){
        photos.add(photo);
        return this;
    }

    //EqualsAndHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Pet that = (Pet) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 1210207296;
    }
}
