import java.util.ArrayList;
import java.util.List;

class RatingInfo {
    private String name, score, takeAgain, levelOfDifficultly, department, topRating;
    private List<String> hotTags = new ArrayList<>();

    void setName(String name) {
        this.name = name;
    }
    String getName(){ return name; }

    void setScore(String score) {
        this.score = score;
    }

    String getScore() {
        return score;
    }

    void setHotTags(String hotTag) {
        hotTags.add(hotTag);
    }

    List<String> getHotTags() {
        return hotTags;
    }

    void setTakeAgain(String takeAgain) {
        this.takeAgain = takeAgain;
    }

    String getTakeAgain() {
        return takeAgain;
    }

    void setLevelOfDifficultly(String levelOfDifficultly) {
        this.levelOfDifficultly = levelOfDifficultly;
    }

    String getLevelOfDifficultly() {
        return levelOfDifficultly;
    }

    void setDepartment(String department) {
        this.department = department;
    }

    String getDepartment() {
        return department;
    }

    void setTopRating(String topRating) {
        this.topRating = topRating;
    }

    String getTopRating() {
        return topRating;
    }
}
