package com.example.sakila.entities;

import com.example.sakila.enums.Rating;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name= "film")
@Getter
@Setter
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "film_id")
    private Short id;


    @Column(name= "title")
    private String title;

    @Column(name= "description")
    private String description;

    @ManyToOne
    @JoinColumn(name= "language_id")
    private Language language;

    @Column(name= "release_year")
    private Short releaseYear;

    @Column(name= "rental_duration")
    private Byte rentalDuration;


    @Column(name= "rental_rate")
    @Generated
    private BigDecimal rentalRate;

//    @Column(name= "rating")
//    private Rating rating;

//    @Column(name= "special_features")
//    private String specialFeatures;

//    @Column(name= "last_update")
//    private Timestamp lastUpdate;

    @ManyToMany(mappedBy = "films")
    private List<Actor> cast = new ArrayList<>();

    @ManyToMany(mappedBy = "films")
    private List<Category> genres = new ArrayList<>();

}


// @Getter is equivalent to