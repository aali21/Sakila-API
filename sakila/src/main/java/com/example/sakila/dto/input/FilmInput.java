package com.example.sakila.dto.input;

import com.example.sakila.enums.Rating;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

import static com.example.sakila.dto.input.ValidationGroup.Create;
@Data
public class FilmInput {

    @NotNull(groups = {Create.class})
    @Size(min = 1, max= 45)
    private String title;
    @NotNull(groups = {Create.class})
    @Size(min = 1, max= 55)
    private String description;
    @NotNull(groups = {Create.class})
    @Min(1901)
    @Max(2155)
    private Short releaseYear;

    @NotNull(groups = {Create.class})
    private Byte languageID;

//    private Rating rating;

    @NotNull(groups = {Create.class})
    private Byte rentalDuration;

}
