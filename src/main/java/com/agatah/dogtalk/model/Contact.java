package com.agatah.dogtalk.model;

import com.agatah.dogtalk.model.enums.ContactType;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @SequenceGenerator(name = "contact_sequence", sequenceName = "contact_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_sequence")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ContactType contactType;

    @Column(nullable = false)
    private String value;



    //EqualsAndHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Contact contact = (Contact) o;

        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return 590563367;
    }
}
