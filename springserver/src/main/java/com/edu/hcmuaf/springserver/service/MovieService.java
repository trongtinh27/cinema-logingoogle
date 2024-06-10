package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Category;
import com.edu.hcmuaf.springserver.entity.Movie;
import com.edu.hcmuaf.springserver.entity.MovieCategory;
import com.edu.hcmuaf.springserver.repositories.MovieCategoryRepository;
import com.edu.hcmuaf.springserver.repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieCategoryRepository movieCategoryRepository;
    public List<Movie> getAllMovie() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(int id) {
        return movieRepository.findOneById(id);
    }
    public void deleteMovieById(long id) {movieRepository.deleteById(id);}

    public Movie createMovie(Movie movie) {
        movie.setBackground_img_url(movie.getBackground_img_url());
        movie.setTitle_img_url(movie.getTitle_img_url());
        movie.setTitle(movie.getTitle());
        movie.setReleased_date(movie.getReleased_date());
        movie.setTrailer_video_url(movie.getTrailer_video_url());
        movie.setPoster_url(movie.getPoster_url());
        movie.setDescription(movie.getDescription());
        movie.setSub_title(movie.getSub_title());
        movie.setAge_type(movie.getAge_type());
        movie.setType(movie.getType());
        movie = movieRepository.save(movie);


        for (Category category : movie.getCategories()) {
            MovieCategory movieCategory = new MovieCategory();
            movieCategory.setMovieId(movie.getId());
            movieCategory.setCategoryId(category.getId());
            movieCategoryRepository.save(movieCategory);
        }
        return movie;}

    public Movie updateMovie(Movie movie, int id) {
        Optional<Movie> optionalExistMovie = movieRepository.findById((long) id);
            Movie existMovie = optionalExistMovie.get();
            existMovie.setBackground_img_url(movie.getBackground_img_url());
            existMovie.setTitle_img_url(movie.getTitle_img_url());
            existMovie.setTitle(movie.getTitle());
            existMovie.setReleased_date(movie.getReleased_date());
            existMovie.setTrailer_video_url(movie.getTrailer_video_url());
            existMovie.setPoster_url(movie.getPoster_url());
            existMovie.setDescription(movie.getDescription());
            existMovie.setSub_title(movie.getSub_title());
            existMovie.setAge_type(movie.getAge_type());
            existMovie.setType(movie.getType());
            existMovie = movieRepository.save(existMovie);

        movieCategoryRepository.deleteAllByMovieId(id);

        for (Category category : movie.getCategories()) {
            MovieCategory movieCategory = new MovieCategory();
//            Category existCategory = existMovie.getCategories().stream().filter(v -> v.getId() == category.getId()).findFirst().orElse(null);
            if (category != null) {
                movieCategory.setMovieId(existMovie.getId());
                movieCategory.setCategoryId(category.getId());
                movieCategoryRepository.save(movieCategory);
            }
        }
        return existMovie;
    }

    public Page<Movie> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Movie> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            if (filterJson.has("title")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + filterJson.get("title").asText() + "%"));
            }
            if (filterJson.has("type")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("type"), "%" + filterJson.get("type").asText() + "%"));
            }
            return predicate;
        };
        if (sortBy.equals("title")) {
            return movieRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "title")));
        }
        if (sortBy.equals("type"))  {
            return movieRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "type")));
        }
        return movieRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }

}
