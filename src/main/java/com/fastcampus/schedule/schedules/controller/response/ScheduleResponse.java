package com.fastcampus.schedule.schedules.controller.response;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ScheduleResponse {

    private Long id;
    private Category category;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;

    public static ScheduleResponse fromEntity(Schedule schedule){
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getCategory(),
                schedule.getUser(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getMemo()
        );
    }
}
