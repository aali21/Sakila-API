package com.example.sakila.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    // method converts the Rating enum value to a String to be stored in the database
    @Override
    public String convertToDatabaseColumn(Rating attribute) {
        // If the Rating attribute is null, return null
        return attribute == null ? null : attribute.name().replace('_', '-');
    }


    // method converts the String value retrieved from the database to a Rating enum value
    @Override
    public Rating convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Rating.valueOf(dbData.replace('-', '_').toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown value for enum Rating: " + dbData);
        }
    }
}
