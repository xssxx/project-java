package cs211.project.services;

import cs211.project.models.event.Event;
import cs211.project.models.history.History;
import cs211.project.models.history.HistoryList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryListFileDatasource implements Datasource<HistoryList> {
    private String directoryName;
    private String fileName;

    public HistoryListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
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
    public HistoryList readData() {
        HistoryList historyList = new HistoryList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        try {
            String line;
            // Use while loop to read data line by line
            while ((line = buffer.readLine()) != null) {
                // If it is an empty line, skip it
                if (line.equals("")) continue;

                // Split the string by ","
                String[] data = line.split(",");
                // Read the data based on index and handle data types accordingly
                String User = data[0].trim();
                String eventImage = data[1].trim();
                String eventName = data[2].trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate eventDate = LocalDate.parse(data[3].trim(), formatter);
                String eventAction = data[4].trim();
                // Add the data to the list
                historyList.addHistory(User,eventImage, eventName, eventDate, eventAction);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return historyList;
    }

    @Override
    public void writeData(HistoryList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (History history : data.getHistoryArrayList()) {
                String[] historyImg = history.getEventImage().split("\\\\");

                String line = history.getUser() + ","
                        + historyImg[historyImg.length - 1] + ","
                        + history.getEventName() + ","
                        + history.getEventDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ","
                        + history.getEventAction();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
