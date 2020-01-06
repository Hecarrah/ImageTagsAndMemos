package Gui;

import Main.*;
import com.sun.javafx.application.LauncherImpl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMain extends Application {

    static FXMLController controller;
    static Stage stage;
    static Scene scene;
    static EventHandler<KeyEvent> imageListener;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("\\fxml\\FXML.fxml"));
            Parent root = loader.load();
            stage = primaryStage;
            stage.initStyle(StageStyle.DECORATED);
            stage.toFront();
            stage.setTitle("Hello");
            stage.setOnCloseRequest( event -> {System.out.println("Closing Stage"); System.exit(0);} );
            scene = new Scene(root);
            controller = (FXMLController) loader.getController();
            scene.getStylesheets().add(getClass().getClassLoader().getResource("\\styles\\fxml.css").toExternalForm());

            imageListener = (KeyEvent e) -> {
                if (e.getCode() == KeyCode.RIGHT && e.isShiftDown()) {
                    Main.getImageViewer().next();
                } else if (e.getCode() == KeyCode.LEFT &&  e.isShiftDown()) {
                    Main.getImageViewer().prev();
                }
            };

            imageEventFilter(false);

            stage.setScene(scene);
            stage.show();
            controller.thumbs();
            controller.setListAsCharacterList();

        } catch (IOException ex) {
            Logger.getLogger(FXMain.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        LauncherImpl.launchApplication(FXMain.class, FXPreloader.class, args);
    }

    public static FXMLController getController() {
        return controller;
    }

    public static void imageEventFilter(boolean bool) {
        if (bool && imageListener != null) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, imageListener);

        } else if (imageListener != null) {
            scene.removeEventFilter(KeyEvent.KEY_PRESSED, imageListener);
        }
    }
}
