package com.example.sakila.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "actor")
@Getter
@Setter
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "actor_id")
    private Short id;


    @Column(name= "first_name")
    private String firstName;

    @Column(name= "last_name")
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = {@JoinColumn(name= "actor_id")},
            inverseJoinColumns = {@JoinColumn(name= "film_id")}
    )

    private List<Film> films = new ArrayList<>();

}


// @Getter does this
//public Short getId() {
//    return id;
//}
//
//public String getFirstName() {
//    return firstName;
//}
//
//public String getLastName() {
//    return lastName;
//}