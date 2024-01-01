package com.catarot.server.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.catarot.server.models.FavouriteCard;
import com.catarot.server.models.Tarot;
import com.catarot.server.models.User;
import com.catarot.server.service.AuthenticationService;
import com.catarot.server.service.FavouriteCardsService;
import com.catarot.server.service.TarotService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class TarotController {
    @Autowired
    TarotService tarotService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    FavouriteCardsService favCardsService;

    @GetMapping("/threecards")
    public ModelAndView getThreeTarotCards(HttpSession sess) throws IOException{
        ModelAndView mav = new ModelAndView();
        Integer numOfCardsDrawn = 3;
        List<Tarot> tarotCards = tarotService.getRandCards(numOfCardsDrawn);
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        mav.setStatus(HttpStatus.OK);
        mav.addObject("tarotCards", tarotCards);
        mav.setViewName("threecards");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchCards(@RequestParam String name, HttpSession sess) throws IOException {
        ModelAndView mav = new ModelAndView();
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        // sess.removeAttribute("searchResults");
        List<Tarot> searchResults = tarotService.searchCardsByName(name);
        mav.setStatus(HttpStatus.OK);
        // sess.setAttribute("searchResults", searchResults);
        mav.addObject("searchResults", searchResults);
        mav.setViewName("search");
        return mav;
    }

    @GetMapping(path="/")
    public ModelAndView getLogin(HttpSession sess){
        ModelAndView mav= new ModelAndView();
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        mav.setViewName("index");
        return mav;
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute("user") User user, Model model, HttpSession sess) {
        boolean isAuthenticated = authService.authenticate(user.getUsername(), user.getPassword());
        if (isAuthenticated) {
            sess.setAttribute("user", user);
            sess.setMaxInactiveInterval(60 * 60);
            model.addAttribute("username", user.getUsername());
            return "redirect:/"; // Redirect to the home page or dashboard
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "index"; // Redirect back to the login page with an error message
        }
    }

    // @GetMapping("/dashboard")
    // public String dashboard(HttpSession sess, Model model) {
    //     User user = (User) sess.getAttribute("user");
    //     if (user != null) {
    //         model.addAttribute("username", user.getUsername());
    //         sess.setMaxInactiveInterval(60 * 60);
    //         return "dashboard";
    //     } else {
    //         return "redirect:/login"; // Redirect to login if user is not found in session
    //     }
    // }

    @PostMapping("/signout")
    public String signOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/favourite")
    public ModelAndView getFavCard(HttpSession sess, RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView mav = new ModelAndView();
        User user = (User) sess.getAttribute("user");
        List<Tarot> cards = tarotService.getAllCards();
        if (user != null) {
            // User is logged in
            mav.addObject("cards", cards);
            List<Tarot> userFavouriteCards = favCardsService.getFavoriteCardsService(user.getUsername());
            mav.addObject("username", user.getUsername());
            mav.addObject("favouriteCard", userFavouriteCards);
        } else {
            // User is not logged in
            RedirectView redirectView = new RedirectView("/");
            redirectView.setExposeModelAttributes(false);
            mav.setView(redirectView);
            // mav.addObject("alert", "Please log in to access this page.");
            // or using RedirectAttributes
            redirectAttributes.addFlashAttribute("alert", "Please log in to access favourites.");
            return mav;
            // mav.addObject("alert", "Please login to start favouriting cards");
            // mav.setViewName("redirect:/");
            // return mav;
        }
        mav.addObject("favouriteCard", new FavouriteCard());
        mav.setViewName("favourite"); // Name of your Thymeleaf template
        return mav;
    }

    @PostMapping("/favourite")
    public ResponseEntity<?> postFavCard(@ModelAttribute FavouriteCard favouriteCard) throws IOException{
        try {
            favCardsService.addFavoriteCardService(favouriteCard.getUsername(), favouriteCard.getCardName());
            return ResponseEntity.ok("Card added to favorites");
        } catch (RuntimeException e) {
            // Log the exception details for debugging
            // e.g., logger.error("Error adding favorite card: ", e);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/allcardsmeaning")
    public ModelAndView allCardsMeaning(HttpSession sess) throws IOException{
        ModelAndView mav = new ModelAndView();
        List<Tarot> allTarotCards = tarotService.getAllCards();
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        mav.addObject("allTarotCards", allTarotCards);
        mav.setViewName("allcardsmeaning");
        return mav;
    }
}
