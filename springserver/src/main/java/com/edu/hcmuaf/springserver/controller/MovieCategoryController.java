package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.entity.MovieCategory;
import com.edu.hcmuaf.springserver.service.MovieCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movie_category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieCategoryController {
    @Autowired
    private MovieCategoryService movieCategoryService;

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateMovieCategory(@PathVariable int id,@RequestBody List<MovieCategory> movieCategoryList) {
//        List<MovieCategory> updateMovieCategory = movieCategoryService.updateMovieCategory(id,movieCategoryList);
//        if (updateMovieCategory != null) {
//            return ResponseEntity.ok(updateMovieCategory);
//        } else return ResponseEntity.badRequest().body(null);
//    }
//    @PutMapping("/")
//    public ResponseEntity<?> update(@PathVariable long movieId,@RequestBody List<MovieCategory> movieCategory) {
//        List<MovieCategory> l = movieCategoryService.updateMovieCategory(movieId,movieCategory);
//        return ResponseEntity.ok(l);
//    }
}
