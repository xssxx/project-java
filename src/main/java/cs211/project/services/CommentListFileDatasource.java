package cs211.project.services;

import cs211.project.models.teamEvent.Comment;
import cs211.project.models.teamEvent.CommentList;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CommentListFileDatasource implements Datasource<CommentList>{
    private String directoryName;
    private String fileName;

    public CommentListFileDatasource(String directoryName, String fileName) {
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
    public CommentList readData() {
        CommentList comments = new CommentList();
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

            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                String eventName = data[0].trim();
                String  teamName = data[1].trim();
                String username = data[2].trim();
                String text = data[3].trim();

                comments.addComment(eventName, teamName, username, text);
            }

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return comments;
    }

    @Override
    public void writeData(CommentList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            for (Comment comment : data.getCommentList()) {

                String line = comment.getEventName() + ","
                            + comment.getTeamName() + ","
                            + comment.getUsername() + ","
                            + comment.getText();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

