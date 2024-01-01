package com.catarot.server.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catarot.server.models.Tarot;
import com.catarot.server.repos.RedisRepo;

@Service
public class FavouriteCardsService {
    
    @Autowired
    RedisRepo redisRepo;
    
    public void addFavoriteCard(String username, Tarot card) {
        // Check if the card is already a favorite
        List<Tarot> existingCards = redisRepo.getFavouriteCards(username);
        if (existingCards.contains(card)) {
            throw new RuntimeException("Card is already in favorites.");
        }
        // Create and save the new favorite card
        redisRepo.addFavouriteCard(username, card);
    }

    public List<Tarot> getFavoriteCards(String username) {
        return redisRepo.getFavouriteCards(username);
    }
}
