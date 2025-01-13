package com.yelman.shopingserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yelman.shopingserver.model.enums.ActiveEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shopping_store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "e_mail", nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "store_name", nullable = true, length = 255)
    private String storeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = true, length = 20)
    private ActiveEnum isActive;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "shopping_categories", length = 100)
    private String category;

    @Column(name = "company_type", length = 50)
    private String companyType;

    @Column(name = "tax_certificate", nullable = false, length = 255)
    private String taxCertificate;

    @Column(name = "tax_office", length = 255)
    private String taxOffice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_hopping_users"))
    private User author;

    @Column(name = "country", nullable = false, length = 90)
    private String country;

    @Column(name = "city", nullable = false, length = 90)
    private String city;

    @Column(name = "district", length = 90)
    private String district;
    @CreationTimestamp
    @Column(name = "creation_time", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private OffsetDateTime updatedAt;
}
