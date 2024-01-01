package com.catarot.server.repos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.catarot.server.models.Tarot;

@Repository
public class RedisRepo {
    @Autowired
    private RedisTemplate<String, Object> template;

    public void cacheTarotCards(String key, List<Tarot> cards) {
        template.opsForValue().set(key, cards);
    }

    public List<Tarot> getTarotCardsFromCache(String key) {
        return (List<Tarot>) template.opsForValue().get(key);
    }

    public void addFavouriteCard(String userName, Tarot card) {
        List<Tarot> favourites = getFavouriteCards(userName);
        if (favourites == null) {
            favourites = new ArrayList<>();
        }
        favourites.add(card);
        template.opsForValue().set("favourites:" + userName, favourites);
    }

    public List<Tarot> getFavouriteCards(String userId) {
        return (List<Tarot>) template.opsForValue().get("favorites:" + userId);
    }
}
