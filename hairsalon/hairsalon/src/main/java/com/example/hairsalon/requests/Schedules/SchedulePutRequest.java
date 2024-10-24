package com.example.hairsalon.requests.Schedules;


import com.example.hairsalon.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SchedulePutRequest {
    
    private String scheduleStatus;
    private List<ScheduleDetail> details;

}
