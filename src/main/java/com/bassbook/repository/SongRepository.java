package com.bassbook.repository;

import com.bassbook.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByNameAndArtistId(String name, Long artistId);
    List<Song> findByArtistId(Long artistId);
    boolean existsByNameAndArtistId(String name, Long artistId);
    
    @Query("SELECT s FROM Song s WHERE " +
           "(:songName IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :songName, '%'))) AND " +
           "(:artistId IS NULL OR s.artist.id = :artistId)")
    List<Song> searchSongs(@Param("songName") String songName, @Param("artistId") Long artistId);
}