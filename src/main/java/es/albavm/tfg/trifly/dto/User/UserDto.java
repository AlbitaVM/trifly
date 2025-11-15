package es.albavm.tfg.trifly.dto.User;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserDto(
    Long id,
    String name,
    String email,
    List<String> roles,
    @JsonIgnore
    String password 
) {}
