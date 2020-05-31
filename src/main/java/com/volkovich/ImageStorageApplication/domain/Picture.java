package com.volkovich.ImageStorageApplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_picture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "pic_byte", length = 1000)
    private byte[] picByte;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    @JsonIgnore
    private Album album;

}
