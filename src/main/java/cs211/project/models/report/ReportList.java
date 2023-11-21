package cs211.project.models.report;

import java.util.ArrayList;

public class ReportList {
    ArrayList<Report> reportList;

    public ReportList() {
        reportList = new ArrayList<>();
    }

    public void addReport(String userReport,String userReported,String text){
        if (!userReport.isEmpty() && !userReported.isEmpty()){
            reportList.add(new Report(userReport,userReported,text));
        }
    }

    public ArrayList<Report> getReportList() {
        return reportList;
    }

}
