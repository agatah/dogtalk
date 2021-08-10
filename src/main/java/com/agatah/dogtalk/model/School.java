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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "schools")
public class School {

    @Id
    @SequenceGenerator(name = "school_sequence", sequenceName = "school_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Privilege> privileges = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "school", orphanRemoval = true)
    private List<ServiceOffer> serviceOffers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "school_contacts",
            joinColumns = {@JoinColumn(name = "school_id")},
            inverseJoinColumns = {@JoinColumn(name = "contact_id")})
    private List<Contact> contacts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Location> locations = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Photo banner;

    @PreRemove
    public void preRemove(){
        for(Privilege privilege: new ArrayList<>(privileges)){
            privilege.getBehaviorist().removeSchoolWithPrivilege(this);
        }
    }

    public School addLocation(Location location){
        locations.add(location);
        location.getSchools().add(this);
        return this;
    }

    public School removeLocation(Location location){
        locations.remove(location);
        location.getSchools().remove(this);
        return this;
    }

    public School addService(ServiceOffer serviceOffer){
        serviceOffers.add(serviceOffer);
        serviceOffer.setSchool(this);
        return this;
    }

    public School removeService(ServiceOffer serviceOffer){
        serviceOffers.remove(serviceOffer);
        serviceOffer.setSchool(null);
        return this;
    }

    public School addPrivilege(Privilege privilege){
        privileges.add(privilege);
        return this;
    }

    public School addContact(Contact contact){
        contacts.add(contact);
        return this;
    }

    public School removeContact(Contact contact){
        contacts.remove(contact);
        return this;
    }


    //EqualsAndHashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        School school = (School) o;

        return Objects.equals(id, school.id);
    }

    @Override
    public int hashCode() {
        return 1694851400;
    }
}
