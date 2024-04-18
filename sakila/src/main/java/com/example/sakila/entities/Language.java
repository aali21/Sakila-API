package com.example.sakila.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name= "language")
@Getter

public class Language {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name= "language_id")
    private Byte id;

    @Column(name= "name")
    private String name;
}
