package com.catarot.server.models;

import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteCard {
    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Card name is required")
    private String cardName;
}
