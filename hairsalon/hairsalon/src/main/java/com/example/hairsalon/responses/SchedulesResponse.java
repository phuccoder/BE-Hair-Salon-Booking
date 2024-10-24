package com.example.hairsalon.responses;

import java.time.LocalTime;

import com.example.hairsalon.enums.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SchedulesResponse {

    private Integer scheduleID;

    private Long stylistID;

    private String stylistName;

    private DayOfWeek dayOfWeek;

    private String scheduleStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "08:00:00")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "17:00:00")
    private LocalTime endTime;
}