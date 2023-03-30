package com.fastcampus.schedule.exception;

import com.fastcampus.schedule.exception.constant.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleErrorResponse {

    private ErrorCode errorCode;
    private String message;
}
