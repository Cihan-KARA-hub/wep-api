package com.yelman.shopingserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yelman.shopingserver.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @JsonIgnore
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;
    @JsonIgnore
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;
    @JsonIgnore
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "user_id")
    )

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities;


    public Set<Role> getAuthorities() {
        return this.authorities;
    }


    public String getPassword() {
        return this.password;
    }


    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }


    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }


    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }


    public boolean isEnabled() {
        return this.enabled;
    }
}