package cs211.project.services;

import cs211.project.models.teamEvent.Participant;
import cs211.project.models.teamEvent.ParticipantList;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ParticipantListFileDatasource implements Datasource<ParticipantList>{
    private String directoryName;
    private String fileName;

    public ParticipantListFileDatasource(String directoryName, String fileName) {
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
    public ParticipantList readData() {
        ParticipantList participants = new ParticipantList();
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
                if (line.isEmpty()) continue;

                // Split the string by ","
                String[] data = line.split(",");
                // Read the data based on index and handle data types accordingly
                String username = data[0].trim();
                String role = data[1].trim();
                String eventName = data[2].trim();
                String teamName = data[3].trim();

                participants.addParticipant(username, role, eventName, teamName);
            }

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return participants;
    }

    @Override
    public void writeData(ParticipantList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Participant participant : data.getParticipants()) {

                String line = participant.getUsername() + ","
                        + participant.getRole() + ","
                        + participant.getEventName() + ","
                        + participant.getTeamName();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}