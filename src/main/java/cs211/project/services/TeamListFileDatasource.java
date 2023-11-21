package cs211.project.services;

import cs211.project.models.teamEvent.Team;
import cs211.project.models.teamEvent.TeamList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamListFileDatasource implements Datasource<TeamList> {
    private String directoryName;
    private String fileName;

    public TeamListFileDatasource(String directoryName, String fileName) {
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
    public TeamList readData() {
        TeamList teamList = new TeamList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if ( line.trim().isEmpty() )
                    continue;

                String[] data = line.split(",");
                String teamName = data[0].trim();
                String eventName = data[1].trim();

                teamList.addTeam(teamName, eventName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return teamList;
    }

    @Override
    public void writeData(TeamList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            for (Team team : data.getTeamList()) {
                String line = team.getTeamName() + ","
                        + team.getEventName();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
