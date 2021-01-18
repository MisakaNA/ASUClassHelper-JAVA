import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ASUClassHelper extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader data_screen_loader = new FXMLLoader();
        data_screen_loader.setLocation(getClass().getResource("Main_Screen.fxml"));
        Parent data_screen_root = data_screen_loader.load();
        primaryStage.setTitle("ASU Class Helper");
        Scene data_screen_scene = new Scene(data_screen_root, 1200, 800);
        primaryStage.setScene(data_screen_scene);
        primaryStage.show();
    }
}
