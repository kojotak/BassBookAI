package com.bassbook.controller;

import com.bassbook.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @GetMapping("/tunings")
    public List<Map<String, String>> getTunings() {
        return Arrays.stream(Tuning.values())
            .map(tuning -> Map.of(
                "value", tuning.name(),
                "displayName", tuning.getDisplayName()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/techniques")
    public List<Map<String, String>> getTechniques() {
        return Arrays.stream(Technique.values())
            .map(technique -> Map.of(
                "value", technique.name(),
                "displayName", technique.getDisplayName()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/tags")
    public List<Map<String, String>> getTags() {
        return Arrays.stream(Tag.values())
            .map(tag -> Map.of(
                "value", tag.name(),
                "displayName", tag.getDisplayName()
            ))
            .collect(Collectors.toList());
    }
}