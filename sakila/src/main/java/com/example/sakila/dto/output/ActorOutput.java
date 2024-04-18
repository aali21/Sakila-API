package com.example.sakila.dto.output;


import com.example.sakila.controllers.ActorController;
import com.example.sakila.entities.Actor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Data Transfer Object (DTO) for Actor entities.
 * This class is used to transport actor data in a more controlled manner from the server to the client.
 * It includes only the necessary fields that need to be exposed via the API.
 *
 */
@Getter
@AllArgsConstructor
//public class ActorOutput {
public class ActorOutput extends RepresentationModel<ActorOutput> {
    private Short id;
    private String firstName;
    private String lastName;
//    private List<FilmReferenceOutput> films; // if you want to include films of each actor uncomment this


    /**
     * Converts an Actor entity to an ActorOutput DTO.
     * This static method is a factory method for creating instances of ActorOutput based on the Actor entity.
     *
     * @param actor The Actor entity to convert.
     * @return An ActorOutput object containing the id, firstName, and lastName of the provided actor.
     */
    public static ActorOutput from(Actor actor) {

        ActorOutput output = new ActorOutput(
                actor.getId(),
                actor.getFirstName(),
                actor.getLastName()
//                actor.getFilms()
//                        .stream()
//                        .map(FilmReferenceOutput::from)
//                        .collect(Collectors.toList())
        );

        // Add link to self
        output.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(ActorController.class)
                .readById(actor.getId()))
                .withSelfRel());
        return output;


    }

    public String getFullName() {
        return firstName+ " "+ lastName;
    }


}
