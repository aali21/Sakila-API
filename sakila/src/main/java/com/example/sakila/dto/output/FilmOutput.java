package com.example.sakila.dto.output;

import com.example.sakila.entities.Film;
import com.example.sakila.entities.Language;
import com.example.sakila.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



@Getter
@AllArgsConstructor
public class FilmOutput extends RepresentationModel<FilmOutput> {
    private Short id;
    private String title;
    private String description;
    private Short releaseYear;
    private Language language;
    private Byte rentalDuration;
//    private Rating rating;
    private List<ActorReferenceOutput> cast;
    private List<CategoryReferenceOutput> genres;



    public static FilmOutput from(Film film) {
        return new FilmOutput(film.getId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseYear(),
                film.getLanguage(), //getting the name (string) from getLanguage object
                film.getRentalDuration(),
//                film.getRating(),
                film.getCast()
                        .stream()
                        .map(ActorReferenceOutput::from)
                        .collect(Collectors.toList()),
                film.getGenres()
                        .stream()
                        .map(CategoryReferenceOutput::from)
                        .collect(Collectors.toList())


        );
    }

//    public String getTitleYear() {
//        return title+ " "+ releaseYear;
//    }
}

