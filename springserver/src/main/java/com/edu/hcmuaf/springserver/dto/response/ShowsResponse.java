package com.edu.hcmuaf.springserver.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class ShowsResponse {
    private Long id;
    private int movieId;
    private int theatreId;
    private int room;
    private String date;
    private String start_time;
    private int status;
}
