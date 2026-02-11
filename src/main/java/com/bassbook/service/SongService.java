package com.bassbook.service;

import com.bassbook.model.Song;
import com.bassbook.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<Song> getSongsByArtist(Long artistId) {
        return songRepository.findByArtistId(artistId);
    }

    public Song createSong(String name, Long artistId) {
        if (songRepository.existsByNameAndArtistId(name, artistId)) {
            throw new RuntimeException("Song already exists for this artist");
        }
        Song song = new Song();
        song.setName(name);
        return songRepository.save(song);
    }
}