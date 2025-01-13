package com.yelman.advertisementserver.model;

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
@Table(name = "sehirler")
public class Sehirler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    @Column(name = "baslik", length = 150)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countries_id", nullable = false)
    private Countries countries_id;

}
