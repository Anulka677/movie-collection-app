package com.example.moviecollection.controller;

import com.example.moviecollection.model.Director;
import com.example.moviecollection.service.DirectorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @PostMapping
    public Director addDirector(@RequestBody Director director) {
        return directorService.saveDirector(director);
    }
}
