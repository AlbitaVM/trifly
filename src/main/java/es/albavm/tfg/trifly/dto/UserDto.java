package es.albavm.tfg.trifly.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record UserDto(
    Long id,
    String name,
    String email,
    @JsonIgnore
    String password 
) {}
