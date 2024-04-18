package com.example.sakila.dto.input;

import jakarta.validation.groups.Default;
import lombok.Builder;

public class ValidationGroup {

    public interface Create extends Default {}
    public interface Update extends Default {}
}
