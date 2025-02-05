package com.yelman.advertisementserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
public class Countries  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    @Column(name = "slug", length = 150)
    private String slug;
    @Column(name = "title", length = 150)
    private String title;
    @Column(name = "area_code")
    private String areaCode;


}

