package Main;

import Algorithms.ImageProcessing;
import Algorithms.TagAlgorithms;
import Gui.Gui;
import IO.IO;
import IO.ImageSQLHandler;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import Data.*;
import Gui.*;
import IO.NotesSQLDataHandler;
import IO.ThumbIO;
import IO.XivSQLDataHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;

public class Main {//248 is webm

    public static boolean debugMode = false;
    public static boolean computeSuggestions = true;
    //public static Gui gui;
    public static ThumbForm tf;
    public static TagAlgorithms algo;
    public static ImageProcessing imageProcessing;
    public static ImageViewer2 imageViewer;

    public static void main(String[] args) {
        try {
            ImageSQLHandler.createConnection();
            ImageSQLHandler.getImages();
            ImageSQLHandler.removeNonExisting();
            ImageSQLHandler.getTags();
            ImageSQLHandler.setTableSize(ImageSQLHandler.imageCount());
            ImageSQLHandler.removeOrphnaedTags();
            
            XivSQLDataHandler.createConnection();
            
            NotesSQLDataHandler.createConnection();
            
            //IO.loadImageXml();
            IO.rescanImages();
            ThumbIO.recreateThumbFiles(false);
            System.out.println("SQL done");
        } catch (Exception sQLException) {
            System.out.println("SQL went bonkers");
            System.out.println(sQLException);
        }
        System.out.println("starting!");

        algo = new TagAlgorithms();
        imageProcessing = new ImageProcessing();
        imageViewer = new ImageViewer2();
        
//        gui = new Gui();
//        gui.setVisible(true);
//        gui.update();
        FXMain.main(args);
        //FXMain.getController().thumbs();

//        try {
//            SaveLoad.loadAll();
//        } catch (IOException | ClassNotFoundException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Deck.handleFCs();
//        Deck.handleServers();
    }

    public void run() {
        String[] a = {""};
        main(a);
    }

    public static TagAlgorithms getTagAlgorithms() {
        return algo;
    }

    public static ImageProcessing getImageProcessing() {
        return imageProcessing;
    }

    public static ThumbForm getThumbImageForm() {
        return tf;
    }
    public static ImageViewer2 getImageViewer() {
        return imageViewer;
    }

}
