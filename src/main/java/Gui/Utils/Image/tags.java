package Gui.Utils.Image;

import DataStructures.ImageWrapper;
import Gui.FXMLController;
import IO.ImageSQLHandler;
import Main.Main;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

public class tags {

    public static void saveTags(FXMLController fxmlController) {
        try {
            ImageWrapper image = new ImageWrapper(ImageSQLHandler.getImageQuery().getString("path"), null);

            ObservableList elements = fxmlController.tagList.getItems();
            ListIterator listIterator = elements.listIterator();
            PreparedStatement[] addImageTagAddToBatch = null;

            while (listIterator.hasNext()) {
                String nextElement = (String) listIterator.next();
                image.addTag(nextElement);
                addImageTagAddToBatch = ImageSQLHandler.addImageTagAddToBatch(image, nextElement);
            }
            ImageSQLHandler.addImageTagExecuteBatch(addImageTagAddToBatch);
            elements = null;

        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void suggestions(FXMLController fxmlController) {
        if (Main.computeSuggestions) {
            try {
                ImageWrapper image = new ImageWrapper(ImageSQLHandler.getImageQuery().getString("path"), null);
                fxmlController.suggestionList.getItems().removeAll(fxmlController.suggestionList.getItems());
                if (image == null) {
                    return;
                }

                Set<Map.Entry<String, Double>> computeSuggestions = Main.getTagAlgorithms().computeSuggestions(image.getPath());
                if (computeSuggestions == null) {
                    return;
                }
                Iterator<Map.Entry<String, Double>> next = computeSuggestions.iterator();
                while (next.hasNext()) {
                    Map.Entry<String, Double> entry = next.next();
                    if (entry.getValue() >= 0.01 && !image.returnTags().contains(entry.getKey())) {
                        fxmlController.suggestionList.getItems().add(entry.getKey() + ", " + entry.getValue().toString().subSequence(0, 3));
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Image not found" + ex);
            }
            fxmlController.suggestionList.refresh();

        }
    }

    public static ArrayList<String> addImplications(String t, ArrayList<String> tags) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<String> implications = ImageSQLHandler.returnImplicationsForTag(t);
        if (implications == null || implications.isEmpty()) {
            return tags;
        }
        for (String s : implications) {
            if (!tags.contains(s)) {
                tags.add(s);
                addImplications(s, tags);
            }
        }
        if (Main.debugMode) {
            System.out.println("Calculating implications took: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        }
        return tags;
    }

    public static void loadTags(FXMLController fxmlController) {
        //Very Fast operation
        try {
            if(ImageSQLHandler.getImageQuery().isBeforeFirst()){
                ImageSQLHandler.getImageQuery().first();
            }
            ImageWrapper image = new ImageWrapper(ImageSQLHandler.getImageQuery().getString("path"), null);
            fxmlController.tagList.getItems().removeAll(fxmlController.tagList.getItems());
            for (String t : image.returnTags()) {
                fxmlController.tagList.getItems().add(t);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        fxmlController.tagList.refresh();
    }

    public static void filter(FXMLController fxmlController) {
        Object[] array = new Object[5];
        ListIterator whiteList = fxmlController.tagBox.getItems().listIterator();
        ArrayList<String> tags = new ArrayList<>();
        while (whiteList.hasNext()) {
            tags.add((String) whiteList.next());
        }
        ListIterator blackList = fxmlController.BLtagBox.getItems().listIterator();
        ArrayList<String> BLtags = new ArrayList<>();
        while (blackList.hasNext()) {
            BLtags.add((String) blackList.next());
        }
        array[0] = fxmlController.suffixField.getText();
        array[1] = Integer.parseInt(fxmlController.countBegin.getText().trim());
        array[2] = Integer.parseInt(fxmlController.countEnd.getText().trim());
        array[3] = tags;
        array[4] = BLtags;
        ImageSQLHandler.getFilteredImages(array);
        Main.getImageViewer().drawSpecific(0);
        fxmlController.thumbs();
    }
}
