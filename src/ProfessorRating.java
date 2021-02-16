import javafx.scene.control.Alert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

class ProfessorRating {
    private String professorFullName;

    ProfessorRating(String fullName) {
        professorFullName = fullName;
    }

    private String getTeacherID() throws IOException {
        String teacherID = "";

        if(professorFullName.length() == 0) {
            return teacherID;
        }

        String teacherUrl = String.format("https://www.ratemyprofessors.com/search.jsp?queryoption=HEADER&queryBy=teacherName" +
                "&schoolName=Arizona+State+University&schoolID=45&query=%s", professorFullName);

        Document doc = Jsoup.connect(teacherUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
                .timeout(3000)
                .get();


        try {
            Elements professorInList = doc.getElementsByClass("listing PROFESSOR");
            teacherID = professorInList.get(0).child(0).attributes().get("href").split("tid=")[1];
            System.out.println("--Debug-- Get professor id");

        } catch (Exception e) {
            System.out.println("--Error-- No teacher ID found");
            return teacherID;
        }
        return teacherID;
    }

    RatingInfo getRatingInfo() throws IOException {
        String teacherID = this.getTeacherID();

        if(teacherID.length() == 0) {
            return null;
        }

        String ratingUrl = String.format("https://www.ratemyprofessors.com/ShowRatings.jsp?tid=%s", teacherID);
        Document doc = Jsoup.connect(ratingUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
                .timeout(3000)
                .get();

        RatingInfo info = new RatingInfo();
        try {
            String name = doc.getElementsByClass("NameTitle__Name-dowf0z-0 cfjPUG").text();
            info.setName(name);

            String score = doc.getElementsByClass("RatingValue__Numerator-qw8sqy-2 liyUjw").get(0).text();
            if(!score.equals("N/A")) { score += " / 5"; }
            info.setScore(score);

            Elements topTags = doc.getElementsByClass("TeacherTags__TagsContainer-sc-16vmh1y-0 dbxJaW");
            topTags = topTags.get(0).getElementsByClass("Tag-bs9vf4-0 hHOVKF");
            for(Element tag : topTags) {
                if(info.getHotTags().size() <= 4) {
                    info.setHotTags(tag.text());
                }
            }

            Elements takeAgainAndDifficulty = doc.getElementsByClass("FeedbackItem__FeedbackNumber-uof32n-1 kkESWs");
            info.setTakeAgain(takeAgainAndDifficulty.get(0).text());
            info.setLevelOfDifficultly(takeAgainAndDifficulty.get(1).text());

            String department = doc.getElementsByClass("NameTitle__Title-dowf0z-1 iLYGwn").text();
            department = department.substring(17, department.indexOf(" department"));
            info.setDepartment(department);

            String topRating = doc.getElementsByClass("Comments__StyledComments-dzzyvm-0 gRjWel").text();
            info.setTopRating(topRating);

            System.out.println("--Debug-- Professor information is loaded successfully");
        } catch (Exception e) {
            //Alert alert = new
            System.out.println("Uh-oh...something bad happened when finding rating information...");
        }
        return info;
    }
}
