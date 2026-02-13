package com.bassbook.service;

import com.bassbook.model.Artist;
import com.bassbook.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist createArtist(String name) {
        if (artistRepository.existsByName(name)) {
            throw new RuntimeException("Artist already exists");
        }
        Artist artist = new Artist();
        artist.setName(name);
        return artistRepository.save(artist);
    }
}