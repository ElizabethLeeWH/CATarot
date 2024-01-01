package com.catarot.server.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.catarot.server.models.FavouriteCards;
import com.catarot.server.models.Tarot;
import com.catarot.server.models.User;
import com.catarot.server.service.AuthenticationService;
import com.catarot.server.service.TarotService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class TarotController {
    @Autowired
    TarotService tarotService;

    @Autowired
    User user;

    @Autowired
    AuthenticationService authService;

    @GetMapping("/threecards")
    public ModelAndView getThreeTarotCards(HttpSession sess) throws IOException{
        ModelAndView mav = new ModelAndView();
        Integer numOfCardsDrawn = 3;
        List<Tarot> tarotCards = tarotService.getRandCards(numOfCardsDrawn);
        sess.removeAttribute(null);
        mav.setStatus(HttpStatus.OK);
        mav.addObject("tarotCards", tarotCards);
        mav.setViewName("threecards");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchCards(@RequestParam String name, HttpSession sess) throws IOException {
        ModelAndView mav = new ModelAndView();
        // sess.removeAttribute("searchResults");
        List<Tarot> searchResults = tarotService.searchCardsByName(name);
        mav.setStatus(HttpStatus.OK);
        // sess.setAttribute("searchResults", searchResults);
        mav.addObject("searchResults", searchResults);
        mav.setViewName("search");
        return mav;
    }

    @GetMapping(path="/login")
    public ModelAndView login(){
        ModelAndView mav= new ModelAndView();
        mav.setStatus(HttpStatus.OK);
        mav.setViewName("login");
        return mav;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpSession sess) {
        boolean isAuthenticated = authService.authenticate(user.getUsername(), user.getPassword());
        if (isAuthenticated) {
            sess.setAttribute("user", user.getUsername());
            return "redirect:/"; // Redirect to the home page or dashboard
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login"; // Redirect back to the login page with an error message
        }
    }

    @GetMapping("/favourite")
    public ModelAndView favoriteCard() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("favouriteCards", new FavouriteCards());
        mav.setViewName("favourite"); // Name of your Thymeleaf template
        return mav;
    }

    @GetMapping("/allcardsmeaning")
    public ModelAndView allCardsMeaning() throws IOException{
        ModelAndView mav = new ModelAndView();
        List<Tarot> allTarotCards = tarotService.getAllCards();
        mav.addObject("allTarotCards", allTarotCards);
        mav.setViewName("allcardsmeaning");
        return mav;
    }
}
