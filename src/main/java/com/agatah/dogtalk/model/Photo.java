package com.agatah.dogtalk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "photos")
public class Photo {

    @Id
    @SequenceGenerator(name = "photo_sequence", sequenceName = "photo_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_sequence")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;
}
