package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.response.ShowsResponse;
import com.edu.hcmuaf.springserver.entity.ShowTime;
import com.edu.hcmuaf.springserver.service.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/shows")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;

    @GetMapping("/all")
    public ResponseEntity<?> getListShowTime() {
        List<ShowTime> showTimeList = showTimeService.getAllShowTime();
        if (showTimeList != null ) {
            return ResponseEntity.ok(showTimeList);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getShowsByMovieIdAndTheatreId(@RequestParam int movieId, @RequestParam int theatreId, @RequestParam String date) {
        List<ShowsResponse> responses = new ArrayList<>();

        List<ShowTime> showTimeList = showTimeService.getShowTimesByMovieIdAndTheatreId(movieId, theatreId);
        showTimeList.removeIf(showTime -> showTime.getStatus()==1);

        if(!showTimeList.isEmpty()){
            for (ShowTime shows : showTimeList) {
                ShowsResponse s = new ShowsResponse();

                LocalDate dt = shows.getStart_time().toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M");
                s.setDate(dt.format(formatter));

                LocalDateTime now = LocalDateTime.now();

                if(s.getDate().equals(date)) {
                    s.setId(shows.getId());
                    s.setMovieId(Math.toIntExact(shows.getMovie().getId()));
                    s.setRoom(shows.getRoom());
                    s.setTheatreId(Math.toIntExact(shows.getTheatre().getId()));
                    s.setStart_time(shows.getStart_time().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    s.setStatus(shows.getStatus());

                    if(now.isBefore(shows.getStart_time())) {
                        responses.add(s);
                    }
                }
            }
            return ResponseEntity.ok(responses);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShowTimeById(@PathVariable int id) {
        Optional<ShowTime> showTime = Optional.ofNullable(showTimeService.getShowTimeById(id));
        System.out.println("showtime: " + showTime);
        if (showTime.isPresent()) {
            return ResponseEntity.ok(showTime);
        }
        return ResponseEntity.badRequest().body(null);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowTime(@PathVariable long id) {
        showTimeService.deleteShowTime(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> createShowTime(@RequestBody ShowTime showTime) {
        return ResponseEntity.ok(showTimeService.createShowTime(showTime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShowTime(@RequestBody ShowTime showTime, @PathVariable int id) {
        return ResponseEntity.ok(showTimeService.updateShowTime(id, showTime));
    }

    @GetMapping
    public ResponseEntity<Page<ShowTime>> getAllShowTime(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "{}") String filter,
                                                      @RequestParam(defaultValue = "16") int perPage,
                                                      @RequestParam(defaultValue = "movie.title") String sort,
                                                      @RequestParam(defaultValue = "DESC") String order) {
        Page<ShowTime> showTimes = showTimeService.getAllwithSort(filter, page, perPage, sort, order);
        return ResponseEntity.ok(showTimes);
    }
}