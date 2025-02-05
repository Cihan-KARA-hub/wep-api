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
@Table(name = "ilceler")
public class Ilceler  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    @Column(name = "baslik", length = 150)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sehir_id", nullable = false)
    private Sehirler countries_id;
}
