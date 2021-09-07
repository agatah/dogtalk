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
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "behaviorists")
public class BehavioristProfile{

    @Id
    @Column(insertable = false, updatable = false)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "behaviorist_contacts",
            joinColumns = {@JoinColumn(name = "behaviorist_id")},
            inverseJoinColumns = {@JoinColumn(name = "contact_id")})
    private List<Contact> contactList = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(columnDefinition = "TEXT")
    private String qualifications;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "behaviorist", orphanRemoval = true)
    private List<BehavioristPrivilegesInSchool> behavioristPrivilegesInSchools = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    public BehavioristProfile addSchoolWithPrivileges(School school, List<Privilege> privileges){
        behavioristPrivilegesInSchools.add(new BehavioristPrivilegesInSchool()
                .setPrivileges(privileges)
                .setSchool(school)
                .setBehaviorist(this));
        return this;
    }

    public BehavioristProfile removeSchoolWithPrivilege(School school){
        Optional<BehavioristPrivilegesInSchool> privilegeOpt = behavioristPrivilegesInSchools.stream()
                .filter(s -> s.getSchool().getId().equals(school.getId()))
                .findFirst();
        if(privilegeOpt.isPresent()){
            behavioristPrivilegesInSchools.remove(privilegeOpt.get());
            school.getBehavioristPrivilegesInSchools().remove(privilegeOpt.get());
        }
        return this;
    }

    public BehavioristProfile addContact(Contact contact){
        contactList.add(contact);
        return this;
    }

    public BehavioristProfile removeContact(Contact contact){
        contactList.remove(contact);
        return this;
    }



    //EqualsAndHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BehavioristProfile that = (BehavioristProfile) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 1903851753;
    }
}
