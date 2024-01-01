package com.catarot.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catarot.server.models.Tarot;
import com.catarot.server.repos.RedisRepo;

@Service
public class FavouriteCardsService {
    
    @Autowired
    RedisRepo redisRepo;

    @Autowired
    TarotService tService;
    
    public void addFavoriteCardService(String username, String cardName) throws IOException {
        // Convert cardName to a Tarot object (adjust based on your actual Tarot class)
        Tarot card = new Tarot();
        List<Tarot> tarots = tService.searchCardsByName(cardName.replace("\"", ""));
        for(Tarot tarot: tarots){
            System.out.println(tarot);
            if (cardName.equals(tarot.getName())) {
                card = tarot;
                break;
            }
        }

        if (card == null) {
            throw new RuntimeException("Card not found.");
        }
        // Check if the card is already a favorite
        List<Tarot> existingCards = redisRepo.getFavouriteCards(username);

        if (existingCards == null) {
            existingCards = new ArrayList<>();
        } else if (existingCards.contains(card)){
            throw new RuntimeException("Card is already in favorites.");
        }

        existingCards.add(card);
        // Create and save the new favorite card
        redisRepo.setFavouriteCards(username, existingCards);
    }

    public List<Tarot> getFavoriteCardsService(String username) {
        return redisRepo.getFavouriteCards(username);
    }
}
