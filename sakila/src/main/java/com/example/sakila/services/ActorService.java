package com.example.sakila.services;

import com.example.sakila.entities.Actor;
import com.example.sakila.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActorService
{
    @Autowired
    ActorRepository repository;

    public List<Actor> getAllActors(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Actor> pagedResult = repository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Actor>();
        }
    }

    public boolean hasMore(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id")); // Assuming 'id' is a default sorting parameter
        Page<Actor> pagedResult = repository.findAll(paging);

        // This will return true if there's another page after the current one
        return !pagedResult.isLast();
    }
}
