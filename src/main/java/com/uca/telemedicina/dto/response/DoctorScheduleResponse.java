package com.uca.telemedicina.dto.response;

import com.uca.telemedicina.entities.DoctorSchedule;
import lombok.Data;

@Data
public class DoctorScheduleResponse {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private String dayOfWeek;
    private String shiftStart;
    private String shiftEnd;

    public static DoctorScheduleResponse from(DoctorSchedule schedule) {
        DoctorScheduleResponse response = new DoctorScheduleResponse();
        response.setId(schedule.getId());
        response.setDayOfWeek(schedule.getDayOfWeek().toString());
        response.setShiftStart(schedule.getShiftStart().toString());
        response.setShiftEnd(schedule.getShiftEnd().toString());
        if (schedule.getDoctor() != null) {
            response.setDoctorId(schedule.getDoctor().getId());
            if (schedule.getDoctor().getUser() != null) {
                response.setDoctorName(
                        schedule.getDoctor().getUser().getFirstName() + " " +
                                schedule.getDoctor().getUser().getLastName()
                );
            }
        }
        return response;
    }
}