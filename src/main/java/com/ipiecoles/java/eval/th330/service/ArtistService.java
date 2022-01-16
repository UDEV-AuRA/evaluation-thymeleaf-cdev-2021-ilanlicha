package com.ipiecoles.java.eval.th330.service;

import com.ipiecoles.java.eval.th330.exception.ConflitException;
import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumService albumService;

    public Page<Artist> findAllArtists(Integer page, Integer size, String sortProperty, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortProperty);
        Pageable pageable = PageRequest.of(page,size,sort);
        return artistRepository.findAll(pageable);
    }

    public Artist findById(Long id) {
        Optional<Artist> artist = this.artistRepository.findById(id);
        if(!artist.isPresent()){
            throw new EntityNotFoundException("Impossible de trouver l'artiste d'identifiant " + id);
        }
        return artist.get();
    }

    public Long countAllArtists() {
        return artistRepository.count();
    }

    public Artist creerArtiste(Artist artist) throws ConflitException {
        if (artistRepository.existsByNameIgnoreCase(artist.getName())){
            throw new ConflitException("L'artise " + artist.getName() + " existe déjà");
        }else{
            return artistRepository.save(artist);
        }
    }

    public void deleteArtist(Long id) {
        //Supprimer les albums
        Set<Album> albums = findById(id).getAlbums();
        for (Album a : albums){
            albumService.deleteAlbum(a.getId());
        }

        //Supprimer l'artiste
        artistRepository.deleteById(id);
    }

    public Artist updateArtiste(Long id, Artist artist) {
        return artistRepository.save(artist);
    }

    public Page<Artist> findByNameLikeIgnoreCase(String name, Integer page, Integer size, String sortProperty, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortProperty);
        Pageable pageable = PageRequest.of(page,size,sort);
        return artistRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
