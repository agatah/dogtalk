package com.agatah.dogtalk.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "services")
public class ServiceOffer {

    @Id
    @SequenceGenerator(name = "service_sequence", sequenceName = "service_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_sequence")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;



    //EqualsAndHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ServiceOffer serviceOffer = (ServiceOffer) o;

        return Objects.equals(id, serviceOffer.id);
    }

    @Override
    public int hashCode() {
        return 1226124435;
    }
}
