package com.bassbook.repository;

import com.bassbook.model.Video;
import com.bassbook.model.Tuning;
import com.bassbook.model.Technique;
import com.bassbook.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByYoutubeVideoId(String youtubeVideoId);
    boolean existsByYoutubeVideoId(String youtubeVideoId);
    
    @Query("SELECT DISTINCT v.song FROM Video v WHERE " +
           "(:songName IS NULL OR LOWER(v.song.name) LIKE LOWER(CONCAT('%', :songName, '%'))) AND " +
           "(:artistId IS NULL OR v.song.artist.id = :artistId) AND " +
           "(:tuning IS NULL OR v.tuning = :tuning) AND " +
           "(:technique IS NULL OR :technique MEMBER OF v.techniques) AND " +
           "(:tag IS NULL OR v.tag = :tag) AND " +
           "(:userId IS NULL OR v.userId = :userId OR v.userId IS NULL)")
    Page<com.bassbook.model.Song> findDistinctSongsByFilters(@Param("songName") String songName,
                                                           @Param("artistId") Long artistId,
                                                           @Param("tuning") Tuning tuning,
                                                           @Param("technique") Technique technique,
                                                           @Param("tag") Tag tag,
                                                           @Param("userId") String userId,
                                                           Pageable pageable);
    
    @Query("SELECT v FROM Video v WHERE " +
           "v.song.id = :songId AND " +
           "(:userId IS NULL OR v.userId = :userId OR v.userId IS NULL)")
    List<Video> findBySongIdAndUserId(@Param("songId") Long songId, @Param("userId") String userId);
}