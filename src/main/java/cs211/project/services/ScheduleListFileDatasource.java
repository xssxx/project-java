package cs211.project.services;

import cs211.project.models.event.Schedule;
import cs211.project.models.event.ScheduleList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleListFileDatasource implements Datasource<ScheduleList> {
    private String directoryName;
    private String fileName;

    public ScheduleListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted(directoryName);
    }

    private void checkFileIsExisted(String directoryName) {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ScheduleList readData() {
        ScheduleList scheduleList = new ScheduleList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String eventName = data[0].trim();
                LocalDate date = LocalDate.parse(data[1].trim(), formatter);
                LocalTime startTime = LocalTime.parse(data[2].trim());
                LocalTime endTime = LocalTime.parse(data[3].trim());
                String activity = data[4].trim();
                String teamName = data[5].trim();
                String description = data[6].trim();
                String status = data[7].trim();

                scheduleList.addSchedule(eventName, date, startTime, endTime, activity, teamName, description, status);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return scheduleList;
    }

    @Override
    public void writeData(ScheduleList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Schedule schedule : data.getScheduleList()) {
                String formattedDate = schedule.getDate().format(dateFormatter);

                String line = schedule.getEventName() + ","
                        + formattedDate + ","
                        + schedule.getStartTime() + ","
                        + schedule.getEndTime() + ","
                        + schedule.getActivity() + ","
                        + schedule.getTeamName() + ","
                        + schedule.getDescription() + ","
                        + schedule.getStatus();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
