package cs211.project.services;

import cs211.project.models.event.Event;
import cs211.project.models.event.EventList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventListFileDatasource implements Datasource<EventList> {
    private String directoryName;
    private String fileName;

    public EventListFileDatasource(String directoryName, String fileName) {
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
    public EventList readData() {
        EventList events = new EventList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",");
                String eventName = data[0].trim();
                String imgSrc = data[1].trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate start = LocalDate.parse(data[2].trim(), formatter);
                LocalDate end = LocalDate.parse(data[3].trim(), formatter);
                String creatorName = data[4].trim();
                String creatorImgSrc = data[5].trim();
                int joinedCount = Integer.parseInt(data[6].trim());
                String description = data[7].trim();
                int maxJoinCount = Integer.parseInt(data[8].trim());
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime startTime = LocalTime.parse(data[9].trim(), timeFormatter);
                LocalTime endTime = LocalTime.parse(data[10].trim(), timeFormatter);

                events.addEvent(eventName, imgSrc, start, end, joinedCount, creatorName, creatorImgSrc, description, maxJoinCount, startTime, endTime);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return events;
    }

    @Override
    public void writeData(EventList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Event event : data.getEvents()) {
                String[] eventImgName = event.getImgSrc().split("\\\\");
                String[] creatorImgName = event.getCreatorImgSrc().split("\\\\");

                String line = event.getEventName() + ","
                        + eventImgName[eventImgName.length - 1] + ","
                        + event.getStart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ","
                        + event.getEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ","
                        + event.getCreatorName() + ","
                        + creatorImgName[creatorImgName.length - 1] + ","
                        + event.getJoinCount() + ","
                        + event.getDescription() + ","
                        + event.getMaxJoinCount() + ","
                        + event.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + ","
                        + event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

