package com.catarot.server.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catarot.server.models.Tarot;
import com.catarot.server.service.TarotService;

@RestController
@RequestMapping("/api/tarot")
public class TarotRestController {
    
    @Autowired
    private TarotService tarotService;

    
    @GetMapping("/number/{num}")
    public List<Tarot> getNumCards(@PathVariable Integer num) throws IOException {
        return tarotService.getRandCards(num);
    }

    @GetMapping("/search/{name}")
    public List<Tarot> searchCardsByName(@PathVariable String name) throws IOException {
        return tarotService.searchCardsByName(name);
    }

}
