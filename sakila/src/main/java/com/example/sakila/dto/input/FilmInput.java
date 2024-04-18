package com.example.sakila.dto.input;

import com.example.sakila.enums.Rating;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

import static com.example.sakila.dto.input.ValidationGroup.Create;
@Data
public class FilmInput {

    @NotNull(groups = {Create.class}, message = "Title is required") //
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

//    @Pattern(regexp = "^(?:NC_17|R|G|PG_13|PG)$", message = "Invalid rating, it should be in either of this format [NC_17, R, G, PG_13, PG]")
    private Rating rating;

    @NotNull(groups = {Create.class}, message = "Rental duration must be at least 1")
    private Byte rentalDuration;

}
