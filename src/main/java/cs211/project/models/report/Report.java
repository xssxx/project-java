package cs211.project.models.report;

public class Report {
    private String userReport;
    private String userReported;
    private String text;

    public Report(String userReport, String userReported, String text){
        this.userReport = userReport;
        this.userReported = userReported;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserReport() {
        return userReport;
    }

    public String getUserReported() {
        return userReported;
    }

    public String getText() {
        return text;
    }

    public Boolean isUserReported(String userReported) {
        return userReported.equals(this.userReported);
    }

    @Override
    public String toString() {
        return userReport + " : " + text ;
    }
}
