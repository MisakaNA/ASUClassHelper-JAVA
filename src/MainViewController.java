import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MainViewController {

    private final HashMap<String, RatingInfo> ratingInfoHolder = new HashMap<>();

    @FXML
    private ChoiceBox<String> semesterChoices = new ChoiceBox<>();
    @FXML
    private TextField className = new TextField("e.g CSE110");
    @FXML
    private CheckBox openClasses = new CheckBox();
    @FXML
    private CheckBox allClasses = new CheckBox();
    @FXML
    private Button search = new Button();
    @FXML
    private Label SEMESTER_ERROR = new Label();
    @FXML
    private Label CLASS_NAME_ERROR = new Label();
    @FXML
    private TextFlow classFlow;
    @FXML
    private TextFlow titleFlow = new TextFlow();
    @FXML
    private TextFlow numberFlow = new TextFlow();
    @FXML
    private TextFlow instructorFlow = new TextFlow();
    @FXML
    private TextFlow dayFlow = new TextFlow();
    @FXML
    private TextFlow startTFlow = new TextFlow();
    @FXML
    private TextFlow endTFlow = new TextFlow();
    @FXML
    private TextFlow locationFlow = new TextFlow();
    @FXML
    private TextFlow seatFlow = new TextFlow();
    @FXML
    private TextFlow ratingFlow = new TextFlow();
    @FXML
    private Label numOfResults = new Label();
    @FXML
    private ImageView loading = new ImageView();
    @FXML
    private Label ratingInfoLabel = new Label();
    @FXML
    private ScrollPane ratingInfoPane = new ScrollPane();
    @FXML
    private TextFlow ratingInfoFlow = new TextFlow();

    @FXML
    public void setSemester() {
        String[] arr = {"Fall 2020", "Spring 2021", "Fall 2021", "Spring 2022", "Fall 2022", "Spring 2023",
                "Fall 2023", "Spring 2024", "Fall 2024", "Spring 2025", "Fall 2025"};
        semesterChoices.setItems(FXCollections.observableArrayList(arr));
        semesterChoices.setStyle("-fx-font: 15 system; -fx-font-weight: bold; -fx-text-alignment: left;");
    }

    @FXML
    public void setClassName() {
        className.setText("");
    }

    @FXML
    public void setOpenClasses() {
        if(allClasses.isSelected()) {
            allClasses.setSelected(false);
        }
        if(!openClasses.isSelected()) {
            openClasses.setSelected(true);
        }
    }

    @FXML
    public void setAllClasses() {
        if(openClasses.isSelected()) {
            openClasses.setSelected(false);
        }
        if(!allClasses.isSelected()) {
            allClasses.setSelected(true);
        }
    }

    @FXML
    public void searchClass() throws IOException {
        loading.setVisible(true);
        resetLableVisibility(); resetTextFlows();
        
        if(semesterChoices.getValue() == null) {
            SEMESTER_ERROR.setStyle("-fx-opacity: 1");
            return;
        }

        if(className.getText().equals("")) {
            CLASS_NAME_ERROR.setStyle("-fx-opacity: 1");
            return;
        } else if(className.getText().length() < 6) {
            CLASS_NAME_ERROR.setText("Invalid class name!");
            CLASS_NAME_ERROR.setStyle("-fx-opacity: 1");
            return;
        }

        ASUClassFinder finder = new ASUClassFinder(semesterChoices.getValue(), className.getText(), openClasses.isSelected());
        List<ASUClass> classList = finder.getWebpage();
        loadResult(classList);
        loading.setVisible(false);
    }

    private void resetLableVisibility() {
        SEMESTER_ERROR.setStyle("-fx-opacity: 0");
        CLASS_NAME_ERROR.setText("Please enter the class name!");
        CLASS_NAME_ERROR.setStyle("-fx-opacity: 0");
    }

    private void loadResult(List<ASUClass> classList) throws IOException {
        numOfResults.setText( "Total " + classList.size() + " results found for class " + className.getText() );
        int classTracker = 0;

        for(ASUClass c : classList) {

            Text classTxt = setResultText(c.getName() + "\n");
            classFlow.getChildren().add(classTxt);
            classFlow.setLineSpacing(5.92);

            Text titleTxt = setResultText(c.getTitle() + "\n");
            titleFlow.getChildren().add(titleTxt);
            titleFlow.setLineSpacing(5.92);

            Text numberTxt = setResultText(c.getNumber() + "\n");
            numberFlow.getChildren().add(numberTxt);
            numberFlow.setLineSpacing(5.92);

            Hyperlink rmpLink = new Hyperlink(c.getInstructor());
            rmpLink.setOnAction(event -> {
                //RatingInfo info;
                ratingInfoFlow.getChildren().clear();
                for(ASUClass c1 : classList) {
                    if(c1.getInstructor().equals(rmpLink.getText())) {
                        if(ratingInfoHolder.containsKey(c1.getFullname())) {
                            setUpRatingInfo(ratingInfoHolder.get(c1.getFullname()));
                        } else {
                            Text text = new Text("\n\n\n\n\t\tNo Rating Information For This Professor");
                            text.setFill(Color.RED);
                            text.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 16));
                            ratingInfoFlow.getChildren().add(text);
                        }

                        ratingInfoFlow.setVisible(true);
                        ratingInfoLabel.setVisible(true);
                        ratingInfoPane.setVisible(true);

                        System.out.println("--Debug-- Showing rating info");
                        break;
                    }
                }

            });
            instructorFlow.getChildren().add(rmpLink);
            instructorFlow.getChildren().add(new Text("\n"));

            Text dayTxt = setResultText("  " + c.getWeekdays() + "\n");
            dayFlow.getChildren().add(dayTxt);
            dayFlow.setLineSpacing(5.92);

            Text startTimeTxt = setResultText(c.getStartTime() + "\n");
            startTFlow.getChildren().add(startTimeTxt);
            startTFlow.setLineSpacing(5.92);

            Text endTTxt = setResultText(c.getEndTime() + "\n");
            endTFlow.getChildren().add(endTTxt);
            endTFlow.setLineSpacing(5.92);

            Text locationTxt = setResultText(c.getLocation() + "\n");
            locationFlow.getChildren().add(locationTxt);
            locationFlow.setLineSpacing(5.92);

            Text seatTxt = setResultText(c.getSeatsOpen() + "\n");
            seatFlow.getChildren().add(seatTxt);
            seatFlow.setLineSpacing(5.92);

            String fullName = c.getFullname();
            RatingInfo info;

            //use map to check if the rating information is already exist
            if(ratingInfoHolder.containsKey(fullName)) {
                info = ratingInfoHolder.get(fullName);
            } else {
                ProfessorRating rating = new ProfessorRating(fullName);
                info = rating.getRatingInfo();
            }

            if(info != null && !ratingInfoHolder.containsKey(fullName)) {
                ratingInfoHolder.put(fullName, info);
            }

            Text ratingTxt = setResultText((info == null) ? "N/A\n" : info.getScore() + "\n");
            ratingTxt.setFont(Font.font("Calibri", 14));
            ratingTxt.setTextAlignment(TextAlignment.CENTER);
            ratingFlow.getChildren().add(ratingTxt);
            ratingFlow.setLineSpacing(5.92);

            System.out.printf("--Debug-- Class %d Data loaded to textflow\n", classTracker++);
        }
    }

    private void setUpRatingInfo(RatingInfo info) {
        Text t = new Text("Professor name:  ");
        t.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        Text name = new Text(info.getName() + "\n");
        name.setFont(Font.font("Calibri", 14));

        Text t1 = new Text("Department:  ");
        t1.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        Text department = new Text(info.getDepartment() + "\n");
        department.setFont(Font.font("Calibri", 14));

        Text t2 = new Text("Overall score:  ");
        t2.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        Text score = new Text(info.getScore() + "\n");
        score.setFont(Font.font("Calibri", 14));
        score.setFill(Color.RED);

        Text t3 = new Text("Would take again:  ");
        t3.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        Text takeAgain = new Text(info.getTakeAgain() + "\n");
        takeAgain.setFont(Font.font("Calibri", 14));
        takeAgain.setFill(Color.GREEN);

        Text t4 = new Text("Level of difficulty:  ");
        t4.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        Text levelDifficulty = new Text(info.getLevelOfDifficultly() + "\n");
        levelDifficulty.setFont(Font.font("Calibri", 14));
        levelDifficulty.setFill(Color.BLUE);

        Text t5 = new Text("Top tags:  ");
        t5.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        StringBuilder tags = new StringBuilder();
        for(String tag : info.getHotTags()) {
            tags.append(tag).append("    ");
        }
        Text topTags = new Text(tags.toString() + "\n");
        topTags.setFont(Font.font("Calibri", 14));

        Text t6 = new Text("Top Rating:  ");
        t6.setFont(Font.font("Calibri", FontWeight.BOLD, 14));
        Text topRating = new Text(info.getTopRating());
        topRating.setFont(Font.font("Calibri", 14));

        ratingInfoFlow.getChildren().addAll(t, name, t1, department, t2, score, t3, takeAgain, t4, levelDifficulty, t5, topTags, t6, topRating);
        ratingInfoFlow.setLineSpacing(8);

        System.out.println("--Debug-- Rating Data loaded to textflow");
    }

    private Text setResultText(String text) {
        Text txt = new Text(text);
        txt.setFont(Font.font("Calibri", 14));
        txt.setTextAlignment(TextAlignment.CENTER);

        return txt;
    }

    private void resetTextFlows() {
        numOfResults.setText("");
        ratingInfoHolder.clear();
        ratingInfoFlow.setVisible(false);
        ratingInfoLabel.setVisible(false);
        ratingInfoPane.setVisible(false);
        ratingInfoFlow.getChildren().clear();
        classFlow.getChildren().clear();
        titleFlow.getChildren().clear();
        numberFlow.getChildren().clear();
        instructorFlow.getChildren().clear();
        dayFlow.getChildren().clear();
        startTFlow.getChildren().clear();
        endTFlow.getChildren().clear();
        locationFlow.getChildren().clear();
        seatFlow.getChildren().clear();
        ratingFlow.getChildren().clear();
    }

}
