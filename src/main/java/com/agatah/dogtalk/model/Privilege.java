package com.agatah.dogtalk.model;

import com.agatah.dogtalk.model.enums.PrivilegeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "behaviorist_school_privilage")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class Privilege {

    @Id
    @SequenceGenerator(name = "behaviorist_privilege_school", sequenceName = "behaviorist_privilege_school",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "behaviorist_privilege_school")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeType privilegeType;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    private BehavioristProfile behaviorist;

    public Privilege setSchool(School school){
        this.school = school;
        school.addPrivilege(this);
        return this;
    }

    @PreRemove
    public void preRemove(){
        if(school != null){
            school.getPrivileges().remove(this);
        }
    }


    //EqualsAndHashCode
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
