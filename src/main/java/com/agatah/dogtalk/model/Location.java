package com.agatah.dogtalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@NoArgsConstructor
@Accessors(chain = true)
@Table(
        name = "locations",
        uniqueConstraints = {@UniqueConstraint(name = "city_name_unique", columnNames = "city")})
public class Location {

    @Id
    @SequenceGenerator(name = "location_sequence", sequenceName = "location_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_sequence")
    private Long id;

    private String city;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "locations")
    private List<School> schools = new ArrayList<>();



    //EqualsAndHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Location location = (Location) o;

        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return 1843932083;
    }
}
