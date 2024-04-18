package com.example.sakila.dto.output;


import com.example.sakila.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryReferenceOutput {
    private Byte id;
    private String name;


    public static CategoryReferenceOutput from(Category category) {
        return new CategoryReferenceOutput(
                category.getId(),
                category.getName());


    }

}
