package com.example.sakila.controllers;

import com.example.sakila.dto.input.ActorInput;
import com.example.sakila.dto.input.ValidationGroup;
import com.example.sakila.dto.output.ActorOutput;
import com.example.sakila.entities.Actor;
import com.example.sakila.repositories.ActorRepository;
import com.example.sakila.services.ActorService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.example.sakila.dto.input.ValidationGroup.Create;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/actors")
public class ActorController {

    // Autowired to inject the ActorRepository dependency automatically
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorService actorService;


//    // returning list of actors
//    @GetMapping
//    public List<ActorOutput> readAll() {
//        // Retrieve all actors from the repository
//        final var actors = actorRepository.findAll();
//
//
//        // Convert each Actor entity to ActorOutput DTO and collect into a list
//        return actors.stream()
//                .map(ActorOutput::from)// ActorOutput::from is a method reference that points to a static method in the ActorOutput class. This method takes an Actor object (assumed to be the type of elements in the stream) and returns an ActorOutput object.
//                .collect(Collectors.toList());
//
//
//    }

//    @GetMapping
//    public ResponseEntity<List<ActorOutput>> getAllActors(
//            @RequestParam(defaultValue = "0") Integer pageNo,
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(defaultValue = "id") String sortBy) {
//        List<Actor> actors = actorService.getAllActors(pageNo,pageSize, sortBy);
//
//        List<ActorOutput> actorOutputs = actors.stream()
//                .map(ActorOutput::from)
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(actorOutputs, new HttpHeaders(), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ActorOutput>>> getAllActors(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Actor> actors = actorService.getAllActors(pageNo,pageSize, sortBy);


        List<EntityModel<ActorOutput>> actorsOutput = actors.stream()
                .map(ActorOutput::from)
                .map(EntityModel::of)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ActorOutput>> collectionModel = CollectionModel.of(actorsOutput);
        collectionModel.add(linkTo(methodOn(ActorController.class)
                .getAllActors(pageNo, pageSize, sortBy)).withSelfRel());

        // Check if there is a next page
        if (actorService.hasMore(pageNo, pageSize)) {
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
    public ActorOutput readById(@PathVariable Short id) {
        return actorRepository.findById(id)
                .map(ActorOutput::from) // Assuming there is a static method in ActorOutput that converts an Actor entity to DTO
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No such actor with id %d.", id)
                ));
    }


    // Post (creating and inputting)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActorOutput create(@Validated(Create.class) @RequestBody ActorInput data) {
        final var actor = new Actor();
        actor.setFirstName(data.getFirstName());
        actor.setLastName(data.getLastName());
        final var saved = actorRepository.save(actor);
        return ActorOutput.from(saved);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ActorOutput updateActor(@PathVariable Short id, @RequestBody ActorInput data) {
        return actorRepository.findById(id)
                .map(actor -> {
                    if (data.getFirstName() != null) { // Check if firstName is provided in the request
                        actor.setFirstName(data.getFirstName());
                    }
                    if (data.getLastName() != null) { // Check if lastName is provided in the request
                        actor.setLastName(data.getLastName());
                    }
                    return actorRepository.save(actor); // Save the updated actor
                })
                .map(ActorOutput::from) // Convert the updated actor to ActorOutput
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actor not found with id: " + id
                ));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorOutput> updateOrCreateActor(@PathVariable Short id, @RequestBody ActorInput data) {
        Actor actor = actorRepository.findById(id)
                .orElse(new Actor()); // If not found, create a new Actor

        // Update the actor's properties
        actor.setId(id);
        actor.setFirstName(data.getFirstName());
        actor.setLastName(data.getLastName());

        // Save the actor to the database
        Actor savedActor = actorRepository.save(actor);

        // Determine the status code: CREATED if new, OK if updated
        HttpStatus status = actor.getId() != null && actor.getId().equals(id) ? HttpStatus.OK : HttpStatus.CREATED;

        // Return the updated/created actor and the appropriate status code
        return new ResponseEntity<>(ActorOutput.from(savedActor), status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Short id) {
        if (!actorRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No such actor with id %d", id)
            );

        }
        actorRepository.deleteById(id);
    }
}
