package com.yelman.shopingserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ilceler")
public class Ilceler {
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
