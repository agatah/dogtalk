package com.agatah.dogtalk.model;

import com.agatah.dogtalk.model.enums.PrivilegeType;
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
@Table(name = "behaviorists_schools")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class BehavioristPrivilegesInSchool {

    @Id
    @SequenceGenerator(name = "behaviorist_privilege_school", sequenceName = "behaviorist_privilege_school",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "behaviorist_privilege_school")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "behaviorists_schools_privileges",
            joinColumns = @JoinColumn(name = "behaviorist_school_id"),
            inverseJoinColumns = {@JoinColumn(name = "privilege_id")})
    private List<Privilege> privileges = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    private BehavioristProfile behaviorist;

    public BehavioristPrivilegesInSchool setSchool(School school){
        this.school = school;
        school.addPrivilege(this);
        return this;
    }

    @PreRemove
    public void preRemove(){
        if(school != null){
            school.getBehavioristPrivilegesInSchools().remove(this);
        }
    }

    public boolean hasPrivilegeManage(){
        return privileges.stream().map(Privilege::getPrivilegeType).anyMatch(p -> p.equals(PrivilegeType.MANAGE));
    }

    public boolean hasPrivilegeJoinRequest(){
        return privileges.stream().map(Privilege::getPrivilegeType).anyMatch(p -> p.equals(PrivilegeType.JOIN_REQUEST));
    }


    //EqualsAndHashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BehavioristPrivilegesInSchool behavioristPrivilegesInSchool = (BehavioristPrivilegesInSchool) o;

        return Objects.equals(id, behavioristPrivilegesInSchool.id);
    }

    @Override
    public int hashCode() {
        return 2090507994;
    }
}
