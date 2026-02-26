package com.projeto.e_commerce.customer;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Customer {

    @Id
    @GeneratedValue(strategy =GenerationType.UUID )
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Email(message = "O campo email precisa estar no formato correto !")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void OnCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void OnUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
