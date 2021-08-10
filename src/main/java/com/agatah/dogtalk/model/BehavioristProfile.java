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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "behaviorists")
public class BehavioristProfile{

    @Id
    @SequenceGenerator(name = "behaviorist_sequence", sequenceName = "behaviorist_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "behaviorist_sequence")
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
    private List<Privilege> privileges = new ArrayList<>();

    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BehavioristProfile addSchoolWithPrivilege(School school, PrivilegeType privilege){
        privileges.add(new Privilege().setPrivilegeType(privilege).setSchool(school).setBehaviorist(this));
        return this;
    }

    public BehavioristProfile removeSchoolWithPrivilege(School school){
        Optional<Privilege> privilegeOpt = privileges.stream()
                .filter(s -> s.getSchool().equals(school))
                .findFirst();
        if(privilegeOpt.isPresent()){
            privileges.remove(privilegeOpt.get());
            school.getPrivileges().remove(privilegeOpt.get());
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
