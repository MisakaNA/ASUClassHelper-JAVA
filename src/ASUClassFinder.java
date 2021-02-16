import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

class ASUClassFinder {
    private String semester, className;
    private boolean seatOpen;

    ASUClassFinder(String semester, String className, boolean seatOpen) {
        this.semester = semester;
        this.className = className;
        this.seatOpen = seatOpen;
    }

    List<ASUClass> getWebpage() throws IOException {
        List<ASUClass> classes = new ArrayList<>();
        String url = String.format(
                "https://webapp4.asu.edu/catalog/myclasslistresults?t=%s&s=%s&n=%s&hon=F&promod=F&e=%s&page=1",
                getSemesterCode(semester), className.substring(0, 3), className.substring(className.length() - 3), (seatOpen) ? "open" : "all"
        );

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
                .timeout(3000)
                .get();
        Elements classListBody;

        try {
            classListBody = doc.select("tbody").first().children();
        } catch (Exception e) {
            return classes;
        }

        for (Element classInList : classListBody) {
            classes.add(classHandler(classInList));
        }
        return classes;
    }

    private String getSemesterCode(String semester) {
        switch (semester) {
            case "Spring 2021":
                return "2211";
            case "Spring 2022":
                return "2221";
            case "Spring 2023":
                return "2231";
            case "Spring 2024":
                return "2241";
            case "Spring 2025":
                return "2251";
            case "Fall 2020":
                return"2207";
            case "Fall 2021":
                return"2217";
            case "Fall 2022":
                return"2227";
            case "Fall 2023":
                return"2237";
            case "Fall 2024":
                return"2247";
            case "Fall 2025":
                return"2257";
            default:
                return "";
        }
    }

    private ASUClass classHandler(Element classNode) {
        ASUClass classObj = new ASUClass();
        classObj.setName(classNode.child(0).text());
        classObj.setTitle(classNode.child(1).text());
        classObj.setNumber(classNode.child(2).text());
        classObj.setInstructor(classNode.child(3).text());

        String fullname = classNode.child(3).child(0).select("a[title]").attr("title");
        fullname = (fullname.equals("")) ? "" : fullname.split("\\|")[1];
        classObj.setFullname(checkFullName(fullname));

        classObj.setWeekdays(classNode.child(4).text());
        classObj.setStartTime(classNode.child(5).text());
        classObj.setEndTime(classNode.child(6).text());

        String shortLocation = classNode.child(7).text();
        shortLocation = (shortLocation.contains("\\-")) ? shortLocation : shortLocation.split("-")[0];
        classObj.setLocation(shortLocation);

        classObj.setSeatsOpen(classNode.child(10).text().replace(" of ", "/"));

        return classObj;
    }

    private String checkFullName(String fullName) {
        switch (fullName) {
            case "Farideh Tadayon-Navabi":
                return "Faye Tadayon-Navabi";
            case "Ihan Hsiao":
                return "Sharon Hsiao";
            default:
                return fullName;
        }
    }

}


