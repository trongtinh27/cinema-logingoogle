package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.entity.Movie;
import com.edu.hcmuaf.springserver.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/movies")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<?> getListMovie() {
        List<Movie> listMovie = movieService.getAllMovie();

        if (listMovie != null) {
            listMovie.removeIf(movie -> movie.getIs_active() == 1);
            return ResponseEntity.ok(listMovie);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable int id) {
        Movie movie = movieService.getMovieById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@Param("name") String name) {
        if(!name.equals(" ") && !name.isEmpty()) {
            List<Movie> movies = movieService.getAllMovie();
            List<Movie> searchMovies = new ArrayList<>();
            if(movies != null) {
                movies.removeIf(movie -> movie.getIs_active() == 1);
                for (Movie movie : movies) {
                    if(movie.getTitle().toLowerCase().contains(name.toLowerCase())) {
                        searchMovies.add(movie);
                    }
                }
            }
            if(!searchMovies.isEmpty()) {
                return ResponseEntity.ok((searchMovies));
            }

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.createMovie(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@RequestBody Movie movie, @PathVariable int id) {
        return ResponseEntity.ok(movieService.updateMovie(movie, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable int id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Movie>> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "{}") String filter,
                                                      @RequestParam(defaultValue = "16") int perPage,
                                                      @RequestParam(defaultValue = "title") String sort,
                                                      @RequestParam(defaultValue = "DESC") String order) {
        Page<Movie> movies = movieService.getAllwithSort(filter, page, perPage, sort, order);
        return ResponseEntity.ok(movies);
    }
}