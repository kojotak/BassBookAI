package com.bassbook.controller;

import com.bassbook.model.Artist;
import com.bassbook.model.Song;
import com.bassbook.service.ArtistService;
import com.bassbook.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private SongService songService;

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    @PostMapping
    public Artist createArtist(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Artist name is required");
        }
        return artistService.createArtist(name.trim());
    }

    @GetMapping("/{artistId}/songs")
    public List<Song> getSongsByArtist(@PathVariable Long artistId) {
        return songService.getSongsByArtist(artistId);
    }

    @PostMapping("/{artistId}/songs")
    public Song createSong(@PathVariable Long artistId, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Song name is required");
        }
        return songService.createSong(name.trim(), artistId);
    }
}