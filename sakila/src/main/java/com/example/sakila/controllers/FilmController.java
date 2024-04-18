package com.example.sakila.controllers;


import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.dto.input.FilmInput;
import com.example.sakila.dto.input.ValidationGroup;
import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.dto.output.FilmOutput;
import com.example.sakila.entities.Actor;
import com.example.sakila.entities.Film;
import com.example.sakila.entities.Language;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.repositories.FilmRepository;
import com.example.sakila.repositories.LanguageRepository;
import com.example.sakila.services.ActorService;
import com.example.sakila.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private FilmService filmService;

//    @GetMapping
//    public List<FilmOutput> readAll() {
//        final var films = filmRepository.findAll();
//        return films.stream()
//                .map(FilmOutput::from)
//                .collect(Collectors.toList());
//    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FilmOutput>>> getAllFilms(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Film> films = filmService.getAllFilms(pageNo,pageSize, sortBy);


        // Convert the list of Film entities to a list of EntityModel<FilmOutput>.
        // FilmOutput::from is a method reference that converts Film entities into FilmOutput DTOs.
        List<EntityModel<FilmOutput>> filmsOutput = films.stream()
                .map(FilmOutput::from)
                .map(EntityModel::of)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<FilmOutput>> collectionModel = CollectionModel.of(filmsOutput);
        collectionModel.add(linkTo(methodOn(FilmController.class)
                .getAllFilms(pageNo, pageSize, sortBy)).withSelfRel());

        // Check if there is a next page
        if (filmService.hasMore(pageNo, pageSize)) {
            collectionModel.add(linkTo(methodOn(ActorController.class)
                    .getAllActors(pageNo + 1, pageSize, sortBy))
                    .withRel("next"));
        }

        // Check if the current page is not the first page
        if (pageNo > 0) {
            collectionModel.add(linkTo(methodOn(ActorController.class)
                    .getAllActors(pageNo - 1, pageSize, sortBy))
                    .withRel("prev"));
        }


        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public FilmOutput readById(@PathVariable Short id) {
        return filmRepository.findById(id)
                .map(FilmOutput::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No such actor with id %d.", id)
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmOutput create(@Validated(ValidationGroup.Create.class) @RequestBody FilmInput data) {


        Language language = languageRepository.findById(data.getLanguageID())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No Language with id %d found.", data.getLanguageID())));
        final var film = new Film();
        film.setTitle(data.getTitle());
        film.setDescription(data.getDescription());
        film.setReleaseYear(data.getReleaseYear());
        film.setRentalDuration(data.getRentalDuration());
        film.setLanguage(language);
        film.setRating(data.getRating());

        final var saved = filmRepository.save(film);
//        return FilmOutput.from(saved);

        // this will output what user created and also outputting a link to what they created
        FilmOutput output = FilmOutput.from(saved);
        output.add(linkTo(methodOn(FilmController.class).readById(saved.getId())).withSelfRel());

        return ResponseEntity.created(linkTo(methodOn(FilmController.class)
                .readById(saved.getId())).toUri()).body(output).getBody();
    }



    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FilmOutput updateFilm(@PathVariable Short id, @Validated(ValidationGroup.Update.class) @RequestBody FilmInput data) {
        // Retrieve the existing film from the database
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No Film with id %d found.", id)));
        // Check and update each field only if provided
        if (data.getTitle() != null) {
            film.setTitle(data.getTitle());
        }
        if (data.getDescription() != null) {
            film.setDescription(data.getDescription());
        }
        if (data.getReleaseYear() != null) {
            film.setReleaseYear(data.getReleaseYear());
        }
        if (data.getRentalDuration() != null) {
            film.setRentalDuration(data.getRentalDuration());
        }
        if (data.getLanguageID() != null) {
            Language language = languageRepository.findById(data.getLanguageID())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format("No Language with id %d found.", data.getLanguageID())));
            film.setLanguage(language);
        }

        Film updated = filmRepository.save(film);
        return FilmOutput.from(updated);
        }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmOutput putUpdateFilm(@PathVariable Short id, @Validated(ValidationGroup.Update.class) @RequestBody FilmInput data) {
        // Retrieve the existing film from the database
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No Film with id %d found.", id)));

        // Fully update the film with new data from the request
        film.setTitle(data.getTitle());
        film.setDescription(data.getDescription());
        film.setReleaseYear(data.getReleaseYear());
        film.setRentalDuration(data.getRentalDuration());

        // Fetch the language entity and update the language of the film
        Language language = languageRepository.findById(data.getLanguageID())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No Language with id %d found.", data.getLanguageID())));
        film.setLanguage(language);


        Film updated = filmRepository.save(film);
        return FilmOutput.from(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Short id) {
        if (!filmRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No such film with id %d", id)
            );

        }
        filmRepository.deleteById(id);
    }

    }
