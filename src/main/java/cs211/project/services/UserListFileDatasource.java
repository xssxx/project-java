package cs211.project.services;

import cs211.project.models.user.User;
import cs211.project.models.user.UserList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;

    public UserListFileDatasource(String directoryName, String fileName) {
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
    public UserList readData() {
        UserList userList = new UserList();
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                // Split the string by ","
                String[] data = line.split(",");
                // Read the data based on index and handle data types accordingly
                String firstName=data[0].trim();
                String lastName=data[1].trim();
                String userName=data[2].trim();
                String password=data[3].trim();
                String imgSrc=data[4].trim();
                String role=data[5].trim();
                String status=data[6].trim();
                LocalDate date = LocalDate.parse(data[7].trim(), formatter);
                int reportCount = Integer.parseInt(data[8].trim());
                LocalDateTime latestLogin = LocalDateTime.parse(data[9].trim());
                userList.addUser(firstName,lastName,userName,password,imgSrc,role,status,date,reportCount,latestLogin);
            }

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    @Override
    public void writeData(UserList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (User user : data.getUsers()) {
                String formattedDate = user.getDate().format(dateFormatter);
                String[] name=user.getImgSrc().split("\\\\");
                String userData =
                        user.getFirstName() + ","
                        + user.getLastName() + ","
                        + user.getUserName() + ","
                        + user.getPassword() + ","
                        + name[name.length-1] + ","
                        + user.getRole() + ","
                        + user.getStatus() + ","
                        + formattedDate + ","
                        + user.getReportCount() + ","
                        + user.getLatestLogin();

                bufferedWriter.write(userData);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
