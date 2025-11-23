package com.domaincrud.domaincrud.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "domains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_id")
    private Long domainId;      // PK -> domains.domain_id

    @Column(nullable = false)
    private String program;     // e.g. "MTech CSE"

    private String batch;       // e.g. "2023"

    @Column(nullable = false)
    private int capacity;       // total seats

    private String qualification; // e.g. "BTech CSE/IT"
}
