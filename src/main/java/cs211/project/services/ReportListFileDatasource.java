package cs211.project.services;

import cs211.project.models.report.Report;
import cs211.project.models.report.ReportList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReportListFileDatasource implements Datasource<ReportList>{
    private String directoryName;
    private String fileName;
    public ReportListFileDatasource(String directoryName, String fileName) {
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
    public ReportList readData() {
        ReportList reportList = new ReportList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");

                String userReport = data[0].trim();
                String userReported = data[1].trim();
                String text = data[2].trim();

                reportList.addReport(userReport,userReported,text);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return reportList;
    }

    @Override
    public void writeData(ReportList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Report report : data.getReportList()) {

                String line = report.getUserReport() + ","
                        + report.getUserReported() + ","
                        + report.getText();

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
