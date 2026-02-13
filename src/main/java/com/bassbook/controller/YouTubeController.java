package com.bassbook.controller;

import com.bassbook.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseYouTubeUrl(@RequestBody Map<String, String> request) {
        try {
            String url = request.get("url");
            if (url == null || url.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
            }

            String videoId = youTubeService.extractVideoId(url);
            YouTubeService.YouTubeVideoInfo videoInfo = youTubeService.getVideoInfo(videoId);
            
            Map<String, Object> response = Map.of(
                "videoId", videoInfo.videoId,
                "title", videoInfo.title,
                "channelName", videoInfo.channelName,
                "channelId", videoInfo.channelId
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<Map<String, Object>> getVideoInfo(@PathVariable String videoId) {
        try {
            YouTubeService.YouTubeVideoInfo videoInfo = youTubeService.getVideoInfo(videoId);
            
            Map<String, Object> response = Map.of(
                "videoId", videoInfo.videoId,
                "title", videoInfo.title,
                "channelName", videoInfo.channelName,
                "channelId", videoInfo.channelId
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}