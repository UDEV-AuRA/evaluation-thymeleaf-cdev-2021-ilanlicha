package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @RequestMapping(
            method = RequestMethod.POST
    )
    public RedirectView add(Album formAlbum, ModelMap model, RedirectAttributes attributes) {
        Album album = albumService.creerAlbum(formAlbum);
        Artist artist = album.getArtist();

        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Album ajouté à " + artist.getName() + " avec succès");
        return redirect(artist, model);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    public RedirectView delete(@PathVariable Long id, ModelMap model, RedirectAttributes attributes){
        Artist artist = albumService.getArtist(id);
        albumService.deleteAlbum(id);

        attributes.addFlashAttribute("type","success");
        attributes.addFlashAttribute("message","Album supprimé de " + artist.getName() + " avec succès");
        return redirect(artist, model);
    }

    private RedirectView redirect(Artist artist, ModelMap model){
        model.put("artist", artist);
        model.put("creation", false);
        return new RedirectView("/artists/" + artist.getId());
    }
}
