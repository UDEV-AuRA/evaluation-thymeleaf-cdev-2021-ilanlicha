package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.exception.ConflitException;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}"
    )
    public String detailArtist(@PathVariable Long id, ModelMap model) {
        model.put("artist", artistService.findById(id));
        model.put("creation", false);
        return "detailArtist";
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    public String findByName(@RequestParam String name ,
                                   @RequestParam Integer page,
                                   @RequestParam Integer size,
                                   @RequestParam String sortProperty,
                                   @RequestParam String sortDirection,
                                   ModelMap model) {
        Page<Artist> artistPage = artistService.findByNameLikeIgnoreCase(name, page, size, sortProperty, sortDirection);
        Integer nbElements = artistPage.getNumberOfElements();
        model.put("liste", artistPage);
        model.put("start", (page * size) + 1);
        model.put("end", (page * size) + nbElements);
        model.put("next", page + 1);
        model.put("previous", page - 1);
        model.put("name", true);
        return "listeArtists";
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String listeArtists(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sortProperty,
            @RequestParam String sortDirection,
            ModelMap model) {
        Page<Artist> artistPage = artistService.findAllArtists(page, size, sortProperty, sortDirection);
        Integer nbElements = artistPage.getNumberOfElements();
        model.put("liste", artistPage);
        model.put("start", (page * size) + 1);
        model.put("end", (page * size) + nbElements);
        model.put("next", page + 1);
        model.put("previous", page - 1);
        model.put("name", false);
        return "listeArtists";
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/save",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView saveArtist(Artist artist, RedirectAttributes attributes) throws ConflitException {
        return createOrUpdateArtist(artist, attributes);
    }

    private RedirectView createOrUpdateArtist(Artist artist, RedirectAttributes attributes) throws ConflitException {
        if(artist.getId() != null){
            artist = artistService.updateArtiste(artist.getId(), artist);
        } else {
            artist = artistService.creerArtiste(artist);
        }
        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Enregistrement de l'artiste effectuté");
        return new RedirectView("/artists/" + artist.getId());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/delete"
    )
    public RedirectView deleteArtist(@PathVariable Long id, RedirectAttributes attributes){
        try {
            artistService.deleteArtist(id);
            attributes.addFlashAttribute("type","success");
            attributes.addFlashAttribute("message","Artiste supprimé avec succès");
        }
        catch (Exception e){
            attributes.addFlashAttribute("type", "danger");
            attributes.addFlashAttribute("message", e.getMessage());
        }
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/new"
    )
    public String newArtist(final ModelMap model){
        model.put("artist", new Artist());
        model.put("creation", true);
        return "detailArtist";
    }
}