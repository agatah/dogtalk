package com.agatah.dogtalk.model;

import com.agatah.dogtalk.model.enums.PrivilegeType;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "privileges")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Privilege {

    @Id
    @SequenceGenerator(name = "privilege_sequence", sequenceName = "privilege_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeType privilegeType;



//    EqualsAndHashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Privilege privilege = (Privilege) o;

        return Objects.equals(id, privilege.id);
    }

    @Override
    public int hashCode() {
        return 2090507994;
    }
}
