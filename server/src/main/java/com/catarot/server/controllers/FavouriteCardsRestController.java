package com.catarot.server.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catarot.server.models.FavouriteCard;
import com.catarot.server.models.Tarot;
import com.catarot.server.service.FavouriteCardsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class FavouriteCardsRestController {
    @Autowired
    FavouriteCardsService favCardsService;

    @PostMapping("/favourite")
    public ResponseEntity<?> addFavoriteCard(@Valid @RequestBody FavouriteCard favouriteCard) throws IOException {
        try {
            favCardsService.addFavoriteCardService(favouriteCard.getUsername(), favouriteCard.getCardName());
            return ResponseEntity.ok("Card added to favorites");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/favourite/{userName}")
    public ResponseEntity<List<Tarot>> getFavoriteCards(@PathVariable String userName) {
        List<Tarot> cards = favCardsService.getFavoriteCardsService(userName);
        if (cards == null || cards.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cards);
    }
}
