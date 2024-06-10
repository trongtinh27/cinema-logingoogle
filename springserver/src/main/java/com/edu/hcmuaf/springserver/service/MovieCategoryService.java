package com.edu.hcmuaf.springserver.service;

        import com.edu.hcmuaf.springserver.entity.MovieCategory;
        import com.edu.hcmuaf.springserver.repositories.MovieCategoryRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.ArrayList;
        import java.util.List;

@Service
public class MovieCategoryService {
    @Autowired
    private MovieCategoryRepository movieCategoryRepository;
//    public List<MovieCategory> test2(int movie_id) {
//        return movieCategoryRepository.findMovieCategoriesByMovie_id(movie_id);
//    }

//    public List<MovieCategory> updateMovieCategory(long movieId, List<MovieCategory> movieCategoryList) {
//
//        for (MovieCategory movieCategory : movieCategoryList) {
//
//        }
//        return movieCategoryList;
////        List<MovieCategory> updatedMovieCategories = new ArrayList<>();
////
////        for (MovieCategory movieCategory : movieCategoryList) {
////            MovieCategory existingMovieCategory = movieCategoryRepository.findOneByMovieIdAndCategoryId(movieId, movieCategory.getCategoryId());
////
////            existingMovieCategory.setMovieId(movieId);
////            existingMovieCategory.setCategoryId(movieCategory.getCategoryId());
////
////            MovieCategory savedMovieCategory = movieCategoryRepository.save(existingMovieCategory);
////            updatedMovieCategories.add(savedMovieCategory);
////        }
////        return updatedMovieCategories;
//    }
}
