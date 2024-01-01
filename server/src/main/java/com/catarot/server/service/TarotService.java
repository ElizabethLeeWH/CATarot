package com.catarot.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.catarot.server.models.Tarot;
import com.catarot.server.repos.RedisRepo;
import jakarta.json.*;

@Service
public class TarotService {

    @Autowired
    RedisRepo redisRepo;

    // Map<String, String> mapOfNameToImgTag = new HashMap<>();
    public static final String URL = "https://tarotapi.dev/api/v1/cards";
    private static final String ALL_CARDS_CACHE_KEY = "AllCards";
    private static final String RAND_CARDS_CACHE_KEY = "RandCards";

    public Map<String, String> mapImgTagToName() throws IOException{
        Resource resource = new ClassPathResource("/static/tarot-images.json");
        InputStream is = resource.getInputStream();
        JsonReader jr = Json.createReader(is);
        JsonObject jo = jr.readObject();
        JsonArray ja = jo.getJsonArray("cards");
        Map<String, String> mapOfNameToImgTag = new HashMap<>();

        for (JsonValue jsonValue : ja) {
            JsonObject obj = jsonValue.asJsonObject();
            
            String nameOfCard = obj.getString("name");
            String imgTag =obj.getString("img");
            mapOfNameToImgTag.put(nameOfCard, imgTag);           
        }
        return mapOfNameToImgTag;
    }

    // @Cacheable(value = "cardsCache", key = "'AllCards'")
    public List<Tarot> getAllCards() throws IOException {
        List<Tarot> cards = redisRepo.getTarotCardsFromCache(ALL_CARDS_CACHE_KEY);
        if (cards == null) {
            cards = mapImagesToCards(fetchCards(URL));
            redisRepo.cacheTarotCards(ALL_CARDS_CACHE_KEY, cards);
        }
        return cards; // Return a copy of the cards
    }

    // @Cacheable(value = "cardsCache", key = "'NumCards' + #num")
    public List<Tarot> getRandCards(Integer num) throws IOException {
        String url = UriComponentsBuilder
            .fromUriString(URL + "/random")
            .queryParam("n", num)
            .toUriString();

        List<Tarot> cards = redisRepo.getTarotCardsFromCache(RAND_CARDS_CACHE_KEY);
        if (cards == null) {
            cards = mapImagesToCards(fetchCards(url));
            redisRepo.cacheTarotCards(RAND_CARDS_CACHE_KEY, cards);
        }
        return cards; // Return a copy of the cards
    }

    // @Cacheable(value = "searchCache", key = "#name.toLowerCase().trim()")
    public List<Tarot> searchCardsByName(String name) throws IOException{

        List<Tarot> allCards = getAllCards();

        List<Tarot> searchResult = allCards.stream()
            .filter(card -> card.getName().replace("\"", "").toLowerCase().contains(name.toLowerCase().trim()))
            .collect(Collectors.toList());
        System.out.println("Searching for: " + name + ", found: " + searchResult.size() + " results" + searchResult);
        return searchResult;
    }

    private List<Tarot> fetchCards(String url) throws IOException {
        List<Tarot> fetchedCards =new ArrayList<>();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.getForEntity(url, String.class);
        JsonReader jr = Json.createReader(new StringReader(resp.getBody()));
        JsonObject jo = jr.readObject();
        JsonArray ja = jo.getJsonArray("cards");

        for (JsonValue jsonVal : ja) {
            JsonObject obj = jsonVal.asJsonObject();
            Tarot t = new Tarot(); 
            t.setName(obj.get("name").toString());
            t.setNumber(Integer.parseInt(obj.get("value_int").toString()));
            t.setArcana(obj.get("type").toString());
            if(obj.get("suit") != null){
                t.setSuit(obj.get("suit").toString());
            } else {
                t.setSuit("na");
            }
            t.setMeaningUp(obj.get("meaning_up").toString());
            t.setMeaningRev(obj.get("meaning_rev").toString());
            t.setDesc(obj.get("desc").toString());
            fetchedCards.add(t);
        }
        return fetchedCards;
    }

    private List<Tarot> mapImagesToCards(List<Tarot> cards) throws IOException {
        Map<String, String> mapOfNameToImgTag = mapImgTagToName();
        for (Tarot card : cards) {
            String nameOfCard = card.getName().replace("\"", "");
            if (mapOfNameToImgTag.containsKey(nameOfCard)) {
                String img = mapOfNameToImgTag.get(nameOfCard);
                card.setImg(img);
            }
        }
        return cards;
    }
}
