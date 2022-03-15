import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        stage.setScene(new Scene(root));
        stage.setTitle("Star");
        stage.getIcons().add(new Image("/staricon.png"));
        stage.show();
    }
}
