package cs211.project.models.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

public class ScheduleList {
    private ArrayList<Schedule> scheduleList;

    public ScheduleList() {
        scheduleList = new ArrayList<>();
    }

    public void addSchedule(String eventName, LocalDate date, LocalTime startTime, LocalTime endTime, String activity, String teamName, String description, String status) {
        scheduleList.add(new Schedule(eventName, date, startTime, endTime, activity, teamName, description, status));
    }
    public void addSchedule(Schedule schedule) {
        scheduleList.add(schedule);
    }
    public ArrayList<Schedule> getScheduleList() {
        return scheduleList;
    }
    public void removeSchedule(Schedule schedule) { scheduleList.remove(schedule); }
    public void removeByEventName(String eventName) {
        Iterator<Schedule> iterator = scheduleList.iterator();

        while (iterator.hasNext()) {
            Schedule schedule = iterator.next();
            if (schedule.getEventName().equals(eventName)) {
                iterator.remove();
            }
        }
    }

    public void removeByTeamName(String teamName) {
        Iterator<Schedule> iterator = scheduleList.iterator();

        while (iterator.hasNext()) {
            Schedule schedule = iterator.next();
            if (schedule.getTeamName().equals(teamName)) {
                iterator.remove();
            }
        }
    }

    public boolean contains(Schedule schedule) {
        for (Schedule s : scheduleList) {
            if (s.equals(schedule)) {
                return true;
            }
        }
        return false;
    }

}
