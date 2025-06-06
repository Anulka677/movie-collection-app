package com.example.moviecollection.service;

import com.example.moviecollection.model.Director;
import com.example.moviecollection.repository.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    public Director saveDirector(Director director) {
        return directorRepository.save(director);
    }

    public Director updateDirector(Long id, Director updatedDirector) {
        Director existing = directorRepository.findById(id).orElseThrow();
        existing.setFullName(updatedDirector.getFullName());
        return directorRepository.save(existing);
    }

    public void deleteDirector(Long id) {
        directorRepository.deleteById(id);
    }

}
