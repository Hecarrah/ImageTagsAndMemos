/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import Gui.Utils.Image.tags;
import DataStructures.ImageWrapper;
import Enum.Job;
import Enum.Race;
import Enum.Server;
import Enum.Sex;
import Enum.Subrace;
import Enum.xivIcons;
import Gui.Utils.Xiv.xivUtils;
import IO.ImageSQLHandler;
import IO.NotesSQLDataHandler;
import IO.XivSQLDataHandler;
import Main.Main;
import com.jfoenix.controls.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javax.swing.SwingUtilities;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class FXMLController implements Initializable {

    @FXML
    public StackPane applicationStackPane;
    public Tab imagesTab;

    public JFXTabPane xivCategoryTabPane;
    public Tab xivCharTab;
    public Tab xivGroupTab;
    public Tab xivConnectionsTab;
    public Tab xivNotesTab;

    public JFXComboBox TagComboBox;
    public MediaView MFrame;
    public SwingNode swingNodeImagePanel;
    public JFXListView suggestionList;
    public JFXListView tagList;
    public TilePane ThumbList;
    public TilePane SimilarThumbList;

    public ScrollPane thumbScroll;

    public JFXTextField suffixField;
    public JFXTextField countBegin;
    public JFXTextField countEnd;
    public JFXComboBox selectionComboBox;
    public JFXComboBox BLselectionComboBox;
    public JFXListView tagBox;
    public JFXListView BLtagBox;

    public JFXCheckBox loadSuggestionsCheckBox;
    public Slider SuggestionSlider;

    public JFXListView xivCharList;
    public JFXComboBox xivCharListComboBox;

    public Label xivCharacterSqlIDLabel;
    public JFXTextField xivLodestoneField;
    public JFXComboBox xivServerField;
    public JFXTextField xivNameField;
    public JFXComboBox xivRaceField;
    public JFXComboBox xivSubraceField;
    public JFXComboBox xivSexField;
    public JFXComboBox xivMainJobField;
    public JFXTextField xivFcField;
    public JFXListView xivNoteList;
    public JFXTextArea xivNoteTextFlow;
    public JFXTextField xivNoteTypeField;
    public JFXComboBox xivOwnerNameComboBox;
    public JFXTextField xivCharNotesAddNewType;
    public Label xivCharLastUpdatedField;

    public Label xivGroupSqlIDLabel;
    public JFXTextField xivGroupNameField;
    public JFXTextField xivGroupLinkField;
    public JFXComboBox xivGroupCharacterComboBox;
    public JFXListView xivGroupCharactersList;
    public JFXListView xivConnectionsList;
    public JFXButton xivSaveNotesButton;
    public Label xivGroupLastUpdatedField;

    public JFXCheckBox xivConnectionDirectedCheckBox;
    public JFXComboBox xivConnectionTargetComboBox;
    public JFXTextField xivConnectionLabelField;

    public JFXTreeView generalNoteTreeList;
    public JFXTextArea generalNoteTextArea;
    public JFXTextField generalNoteNameField;
    public JFXTextField generalNoteParentNameField;
    public JFXButton generalNoteSaveButton;
    public Label generalNoteSQLID;

    public JFXTreeView altNoteTreeList;
    public JFXTextArea altNoteTextArea;
    public JFXTextField altNoteNameField;
    public JFXTextField altNoteParentNameField;
    public JFXButton altNoteSaveButton;
    public Label altNoteSQLIDLabel;

    public BarChart xivRaceChart;
    public BarChart xivSubraceChart;
    public BarChart xivJobsChart;

    public boolean held = false;
    private static Map<File, ImageView> thumbMap = new HashMap<>();
    private static Map<File, ImageView> similarThumbMap = new HashMap<>();
    public static HashMap<ImageView, Image> selectedThumbs = new HashMap<>();
    public static HashMap<ImageView, Image> selectedSimilarThumbs = new HashMap<>();
    public static HashMap<String, Integer> charIds = new HashMap<>();
    public static HashMap<String, Integer> charSQLID = new HashMap<>();
    //public static HashTree<Integer, Integer> genNoteTree = new HashTree<>();
    public static HashMap<Integer, Integer> noteSQLID = new HashMap<>();
    public static HashMap<Integer, Integer> altNoteSQLID = new HashMap<>();
    public static HashMap<TreeItem, Integer> noteLeafSQLID = new HashMap<>();
    public static HashMap<TreeItem, Integer> altNoteLeafSQLID = new HashMap<>();
    public static HashMap<String, String> noteMap = new HashMap<>();
    public static HashMap<String, String> cCConNoteMap = new HashMap<>();
    public static HashMap<String, String> cCGonNoteMap = new HashMap<>();
    public static HashMap<String, Integer> cConMap = new HashMap<>();
    public boolean confirmation = false;

    private AutoCompletionBinding xivCharListComboBoxBinding;
    private AutoCompletionBinding xivGroupCharacterComboBoxBinding;
    private AutoCompletionBinding xivConnectionTargetComboBoxBinding;
    private AutoCompletionBinding TagComboBoxBinding;
    private AutoCompletionBinding selectionComboBoxBinding;
    private AutoCompletionBinding BLselectionComboBoxBinding;

    private ArrayList<String> tagCopyList = new ArrayList<String>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ImageSQLHandler.getImages();
            TagComboBox.getItems().removeAll(true);
            selectionComboBox.getItems().removeAll(true);
            BLselectionComboBox.getItems().removeAll(true);
            ImageSQLHandler.getTags();
            ImageSQLHandler.getTagQuery().beforeFirst();
            while (ImageSQLHandler.getTagQuery().next()) {
                TagComboBox.getItems().add(ImageSQLHandler.getTagQuery().getString("name"));
                selectionComboBox.getItems().add(ImageSQLHandler.getTagQuery().getString("name"));
                BLselectionComboBox.getItems().add(ImageSQLHandler.getTagQuery().getString("name"));
            }
            ImageSQLHandler.getTagQuery().first();
            //SimpleListProperty tags = new SimpleListProperty();
//            TagComboBoxBinding.dispose();
//            selectionComboBoxBinding.dispose();
//            BLselectionComboBoxBinding.dispose();
            TagComboBoxBinding = TextFields.bindAutoCompletion(TagComboBox.getEditor(), TagComboBox.getItems().sorted());
            selectionComboBoxBinding = TextFields.bindAutoCompletion(selectionComboBox.getEditor(), selectionComboBox.getItems());
            BLselectionComboBoxBinding = TextFields.bindAutoCompletion(BLselectionComboBox.getEditor(), BLselectionComboBox.getItems());
            Main.getImageViewer().drawSpecific(0);
            createAndSetImagePanel();

            ThumbList.setPadding(new Insets(15, 15, 15, 15));
            ThumbList.setHgap(15);
            ThumbList.setVgap(15);
            TagComboBox.addEventFilter(KeyEvent.KEY_TYPED, e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    if (TagComboBox.getValue() != null) {
                        String tag = (String) TagComboBox.getValue();
                        System.out.println(tag);
                        tagList.getItems().add(tag);
                    }
                }
            });
            suggestionList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            xivServerField.getItems().addAll(Arrays.asList(Server.values()));
            xivRaceField.getItems().addAll(Arrays.asList(Race.values()));
            xivSubraceField.getItems().addAll(Arrays.asList(Subrace.values()));
            xivSexField.getItems().addAll(Arrays.asList(Sex.values()));
            xivMainJobField.getItems().addAll(Arrays.asList(Job.values()));

            xivServerField.getSelectionModel().selectFirst();
            xivRaceField.getSelectionModel().selectFirst();
            xivSubraceField.getSelectionModel().selectFirst();
            xivSexField.getSelectionModel().selectFirst();
            xivMainJobField.getSelectionModel().selectFirst();

            TextFields.bindAutoCompletion(xivServerField.getEditor(), xivServerField.getItems());
            TextFields.bindAutoCompletion(xivRaceField.getEditor(), xivRaceField.getItems());
            TextFields.bindAutoCompletion(xivSubraceField.getEditor(), xivSubraceField.getItems());
            TextFields.bindAutoCompletion(xivSexField.getEditor(), xivSexField.getItems());
            TextFields.bindAutoCompletion(xivMainJobField.getEditor(), xivMainJobField.getItems());

            setImageContextMenu();
            setTagContextMenu();
            loadGeneralNotes();
            loadAltNotes();
            setGeneralTreeListContextMenu();
            setAltTreeListContextMenu();

            setXivRacesChart();
            setXivSubracesChart();
            setXivJobsChart();

//            SuggestionSlider.getTooltip().setText(SuggestionSlider.getValue()+"");
        } catch (SQLException ex) {
            Logger.getLogger(InfoWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGeneralTreeListContextMenu() {
        generalNoteTreeList.setCellFactory(lv -> {
            TreeCell<String> cell = new TreeCell<>();

            ContextMenu cellMenu = new ContextMenu();

            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                if (me.getButton() == MouseButton.SECONDARY) {
                    cellMenu.show(generalNoteTreeList, me.getScreenX(), me.getScreenX());
                } else {
                    cellMenu.hide();
                }
                loadCurrentGeneralNote();
            });

            MenuItem addChild = new MenuItem();
            MenuItem delChild = new MenuItem();
            addChild.textProperty().bind(Bindings.format("Add Child"));
            addChild.setOnAction(e -> {
                TreeItem item = cell.getTreeItem();
                TreeItem leaf = new TreeItem();
                leaf.setValue("Unnamed");
                item.getChildren().add(leaf);
            });
            delChild.textProperty().bind(Bindings.format("Remove Child"));
            delChild.setOnAction(e -> {
                TreeItem item = cell.getTreeItem();
                Integer get = noteLeafSQLID.get(item);
                noteSQLID.remove(get);
                NotesSQLDataHandler.deleteNote(get);
                loadGeneralNotes();
            });

            cellMenu.getItems().addAll(addChild, delChild);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                } else {
                    cell.setContextMenu(cellMenu);
                }
            });
            return cell;
        });
    }

    public void setAltTreeListContextMenu() {
        altNoteTreeList.setCellFactory(lv -> {
            TreeCell<String> cell = new TreeCell<>();

            ContextMenu cellMenu = new ContextMenu();

            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                if (me.getButton() == MouseButton.SECONDARY) {
                    cellMenu.show(altNoteTreeList, me.getScreenX(), me.getScreenX());
                } else {
                    cellMenu.hide();
                }
                loadCurrentAltNote();
            });

            MenuItem addChild = new MenuItem();
            MenuItem delChild = new MenuItem();
            addChild.textProperty().bind(Bindings.format("Add Child"));
            addChild.setOnAction(e -> {
                TreeItem item = cell.getTreeItem();
                TreeItem leaf = new TreeItem();
                leaf.setValue("Unnamed");
                item.getChildren().add(leaf);
            });
            delChild.textProperty().bind(Bindings.format("Remove Child"));
            delChild.setOnAction(e -> {
                TreeItem item = cell.getTreeItem();
                Integer get = noteLeafSQLID.get(item);
                noteSQLID.remove(get);
                NotesSQLDataHandler.deleteNote(get);
                loadGeneralNotes();
            });

            cellMenu.getItems().addAll(addChild, delChild);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                } else {
                    cell.setContextMenu(cellMenu);
                }
            });
            return cell;
        });
    }

    public void setTagContextMenu() {
        tagList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            cell.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                if (me.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(applicationStackPane, me.getSceneX(), me.getSceneY());
                } else {
                    contextMenu.hide();
                }
            });
            Menu editItem = new Menu();
            editItem.textProperty().bind(Bindings.format("Add implication from \"%s\" to ->", cell.itemProperty()));

            for (Object s : tagList.getItems()) {
                MenuItem it = new MenuItem();
                it.textProperty().bind(Bindings.format("\"%s\"", s));
                editItem.getItems().add(it);

                it.setOnAction(e -> {
                    String item = cell.getItem();
                    ImageSQLHandler.addImplication(item, s.toString());
                });
            }

            contextMenu.getItems().addAll(editItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }

    public void setImageContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        swingNodeImagePanel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if (me.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(swingNodeImagePanel, me.getScreenX(), me.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
        Menu histo = new Menu();
        MenuItem features = new Menu();
        MenuItem ninetyfive = new MenuItem();
        MenuItem ninety = new MenuItem();
        MenuItem eighty = new MenuItem();
        MenuItem seventy = new MenuItem();
        histo.textProperty().bind(Bindings.format("Histogram"));
        ninetyfive.textProperty().bind(Bindings.format("95"));
        ninety.textProperty().bind(Bindings.format("90"));
        eighty.textProperty().bind(Bindings.format("80"));
        seventy.textProperty().bind(Bindings.format("70"));

        ninetyfive.setOnAction(e -> {
            ImageWrapper iw = new ImageWrapper(Main.getImageViewer().imageFile.getPath(), null);
            ImageSQLHandler.getHistogrammticallySimilarImagesToResultSet(iw, 0.05);
            thumbs();
        });
        ninety.setOnAction(e -> {
            ImageWrapper iw = new ImageWrapper(Main.getImageViewer().imageFile.getPath(), null);
            ImageSQLHandler.getHistogrammticallySimilarImagesToResultSet(iw, 0.1);
            thumbs();
        });
        eighty.setOnAction(e -> {
            ImageWrapper iw = new ImageWrapper(Main.getImageViewer().imageFile.getPath(), null);
            ImageSQLHandler.getHistogrammticallySimilarImagesToResultSet(iw, 0.2);
            thumbs();
        });
        seventy.setOnAction(e -> {
            ImageWrapper iw = new ImageWrapper(Main.getImageViewer().imageFile.getPath(), null);
            ImageSQLHandler.getHistogrammticallySimilarImagesToResultSet(iw, 0.3);
            thumbs();
        });
        features.setOnAction(e -> {
            ImageWrapper iw = new ImageWrapper(Main.getImageViewer().imageFile.getPath(), null);
            Main.imageProcessing.extractFeatures(iw);
            //thumbs();
        });
        contextMenu.getItems().addAll(histo, features);
        histo.getItems().addAll(ninety, eighty, seventy);
    }

    public void setXivRacesChart() {
        try {
            HashMap<String, Integer> racesex = new HashMap<>();
            XYChart.Series male = new XYChart.Series();
            male.setName("Males");
            XYChart.Series female = new XYChart.Series();
            female.setName("Female");
            for (Race sr : Race.values()) {
                racesex.put(sr.toString() + "Male", 0);
                racesex.put(sr.toString() + "Female", 0);
                female.getData().add(new XYChart.Data(sr.toString(), 0));
                male.getData().add(new XYChart.Data(sr.toString(), 0));
            }
            XivSQLDataHandler.getXivCharacters();
            XivSQLDataHandler.getXivCharactersResultSet().beforeFirst();
            while (XivSQLDataHandler.getXivCharactersResultSet().next()) {
                String rs = XivSQLDataHandler.getXivCharactersResultSet().getString("Race") + XivSQLDataHandler.getXivCharactersResultSet().getString("Sex");
                racesex.put(rs, racesex.getOrDefault(rs, 0) + 1);
            }

            for (String s : racesex.keySet()) {
                if (s.endsWith("Female")) {
                    CharSequence subSequence = s.subSequence(0, s.length() - 6);
                    female.getData().add(new XYChart.Data(subSequence, racesex.get(s)));
                } else if (s.endsWith("Male")) {
                    CharSequence subSequence = s.subSequence(0, s.length() - 4);
                    male.getData().add(new XYChart.Data(subSequence, racesex.get(s)));

                }
            }

            xivRaceChart.getData().addAll(male, female);
            xivRaceChart.setAnimated(true);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setXivSubracesChart() {
        try {
            XYChart.Series male = new XYChart.Series();
            male.setName("Males");
            XYChart.Series female = new XYChart.Series();
            female.setName("Female");

            HashMap<String, Integer> racesex = new HashMap<>();
            for (Subrace sr : Subrace.values()) {
                racesex.put(sr.toString() + "Male", 0);
                racesex.put(sr.toString() + "Female", 0);
                female.getData().add(new XYChart.Data(sr.toString(), 0));
                female.getData().add(new XYChart.Data(sr.toString(), 0));
                male.getData().add(new XYChart.Data(sr.toString(), 0));
            }
            XivSQLDataHandler.getXivCharacters();
            XivSQLDataHandler.getXivCharactersResultSet().beforeFirst();
            while (XivSQLDataHandler.getXivCharactersResultSet().next()) {
                String rs = XivSQLDataHandler.getXivCharactersResultSet().getString("subrace") + XivSQLDataHandler.getXivCharactersResultSet().getString("Sex");
                racesex.put(rs, racesex.getOrDefault(rs, 0) + 1);
            }

            for (String s : racesex.keySet()) {
                if (s.endsWith("Female")) {
                    CharSequence subSequence = s.subSequence(0, s.length() - 6);
                    female.getData().add(new XYChart.Data(subSequence, racesex.get(s)));
                } else if (s.endsWith("Male")) {
                    CharSequence subSequence = s.subSequence(0, s.length() - 4);
                    male.getData().add(new XYChart.Data(subSequence, racesex.get(s)));

                }
            }

            xivSubraceChart.getData().addAll(male, female);
            xivSubraceChart.setAnimated(true);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setXivJobsChart() {
        try {
            XYChart.Series counts = new XYChart.Series();
            counts.setName("Count");

            HashMap<String, Integer> jobCounts = new HashMap<>();
            for (Job sr : Job.values()) {
                jobCounts.put(sr.toString(), 0);
                counts.getData().add(new XYChart.Data(sr.toString(), 0));
            }
            XivSQLDataHandler.getXivCharacters();
            XivSQLDataHandler.getXivCharactersResultSet().beforeFirst();
            while (XivSQLDataHandler.getXivCharactersResultSet().next()) {
                String rs = XivSQLDataHandler.getXivCharactersResultSet().getString("MainJob");
                jobCounts.put(rs, jobCounts.getOrDefault(rs, 0) + 1);
            }

            for (String s : jobCounts.keySet()) {
                counts.getData().add(new XYChart.Data(s, jobCounts.get(s)));
            }

            xivJobsChart.getData().addAll(counts);
            xivJobsChart.setAnimated(true);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setImagePanel() {
        FXMain.getController().createAndSetImagePanel();
    }

    public static boolean confirmationDialog(String text) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Confirmation"));
        content.setBody(new Text(text));
        JFXAlert alert = new JFXAlert(FXMain.stage);
        alert.setContent(content);
        JFXButton yes = new JFXButton("Confirm");
        JFXButton no = new JFXButton("Cancel");
        FXMain.getController().confirmation = false;
        yes.setOnAction((ActionEvent event) -> {
            FXMain.getController().confirmation = true;
            alert.close();
        });
        no.setOnAction((ActionEvent event) -> {
            FXMain.getController().confirmation = false;
            alert.close();
        });
        content.setActions(yes, no);
        alert.showAndWait();
        return FXMain.getController().confirmation;
    }

    public static void noticeSnackbar(String text) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Confirmation"));
        content.setBody(new Text(text));
        JFXSnackbar alert = new JFXSnackbar(FXMain.getController().applicationStackPane);
        alert.show(text, 2000);
    }

    public void setListAsCharacterList() {
        try {
            setXivList(XivSQLDataHandler.class
                    .getMethod("getXivCharacters"));

        } catch (IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setListAsGroupList() {
        try {
            setXivList(XivSQLDataHandler.class
                    .getMethod("getXivGroups"));

        } catch (IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setXivList(Method m) throws IllegalAccessException {
        if (xivCharList != null && xivCharList.getItems() != null) {
            xivCharListComboBox.getItems().clear();
            xivCharList.getItems().clear();
            xivOwnerNameComboBox.getItems().clear();
            xivConnectionTargetComboBox.getItems().clear();
        }
        if (xivCharList == null) {
            return;

        }
        if (m.getReturnType() == ResultSet.class) {
            try {
                ResultSet result = (ResultSet) m.invoke(this);
                while (result.next()) {
                    String name = result.getString("Name");
                    String combo = "";
                    switch (m.getName()) {
                        case "getXivGroups":
                            charIds.put(name, result.getRow());
                            combo = name;
                            break;
                        case "getXivCharacters":
                            String server = result.getString("Server");
                            combo = name + ", " + server;
                            if (!xivGroupCharacterComboBox.getItems().contains(combo)) {
                                xivGroupCharacterComboBox.getItems().add(combo);
                            }
                            break;

                    }
                    xivCharList.getItems().add(combo);
                    xivCharListComboBox.getItems().add(combo);
                    xivOwnerNameComboBox.getItems().add(combo);
                    xivConnectionTargetComboBox.getItems().add(combo);
                    charIds.put(combo, result.getRow());
                    charSQLID.put(combo, result.getInt("ID"));
                }
                xivCharList.getItems().sort((Object o1, Object o2) -> {
                    String string1 = (String) o1;
                    String string2 = (String) o2;
                    return string1.compareTo(string2);
                });
                xivCharListComboBox.getItems().sort((Object o1, Object o2) -> {
                    String string1 = (String) o1;
                    String string2 = (String) o2;
                    return string1.compareTo(string2);
                });
                xivConnectionTargetComboBox.getItems().sort((Object o1, Object o2) -> {
                    String string1 = (String) o1;
                    String string2 = (String) o2;
                    return string1.compareTo(string2);
                });
                if (xivCharListComboBoxBinding != null) {
                    xivCharListComboBoxBinding.dispose();
                }
                if (xivGroupCharacterComboBoxBinding != null) {
                    xivGroupCharacterComboBoxBinding.dispose();
                }
                if (xivConnectionTargetComboBoxBinding != null) {
                    xivConnectionTargetComboBoxBinding.dispose();
                }
                xivConnectionTargetComboBoxBinding = TextFields.bindAutoCompletion(xivConnectionTargetComboBox.getEditor(), xivConnectionTargetComboBox.getItems());
                xivCharListComboBoxBinding = TextFields.bindAutoCompletion(xivCharListComboBox.getEditor(), xivCharListComboBox.getItems());
                xivGroupCharacterComboBoxBinding = TextFields.bindAutoCompletion(xivGroupCharacterComboBox.getEditor(), xivGroupCharacterComboBox.getItems());
                result.beforeFirst();

            } catch (SQLException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(FXMLController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void thumbs() {
        if (ThumbList.getChildren() != null) {
            ThumbList.getChildren().clear();
            thumbMap.clear();
            selectedThumbs.clear();
        }
        ResultSet imageQuery = ImageSQLHandler.getImageQuery();
        try {
            imageQuery.beforeFirst();
            while (imageQuery.next()) {

                String thumbPath = imageQuery.getString("ThumbPath");
                File f = new File(thumbPath);
                File n = new File(getClass().getClassLoader().getResource("noThumb.png").getPath());
                ImageView imageView;
                if (f.getPath().equals("")) {
                    imageView = createImageView(n);
                } else {
                    imageView = createImageView(f);
                }
                ThumbList.getChildren().addAll(imageView);
                thumbMap.put(f, imageView);
            }
            imageQuery.first();

        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void SimilarThumbs() {
//        try {
//            if (SimilarThumbList.getChildren() != null) {
//                SimilarThumbList.getChildren().clear();
//                similarThumbMap.clear();
//                selectedSimilarThumbs.clear();
//            }
//            double treshold = FXMain.getController().SuggestionSlider.getValue() / 100;
//            //ResultSet imageQuery = ImageSQLHandler.getImageQuery();
//            ImageWrapper wrapper = new ImageWrapper(Main.imageViewer.imageFile.getPath(), null);
//            ResultSet allImages = ImageSQLHandler.getHistogrammticallySimilarImagesToNewResultSet(wrapper, treshold);
//            System.out.println("found");
//            
//            try {
//                while (allImages.next()) {
//                    ImageWrapper tempWrapper = new ImageWrapper(allImages.getString(("Path")), null);
//                    ArrayList<String> imageTags1 = ImageSQLHandler.getImageTags(tempWrapper.getPath());
//                    if (imageTags1.size() < 1) {
//                        continue;
//                    }
//
//                    String thumbPath = allImages.getString("ThumbPath");
//                    File f = new File(thumbPath);
//                    File n = new File(getClass().getClassLoader().getResource("noThumb.png").getPath());
//                    ImageView imageView;
//                    if (f.getPath().equals("")) {
//                        imageView = createImageView(n);
//                    } else {
//                        imageView = createImageView(f);
//                    }
//                    SimilarThumbList.getChildren().addAll(imageView);
//                    similarThumbMap.put(f, imageView);
//                }
//                allImages.first();
//
//            } catch (SQLException ex) {
//                Logger.getLogger(FXMLController.class
//                        .getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//    }

    private void createAndSetImagePanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNodeImagePanel.setContent(Main.getImageViewer().imgp);

                if (Main.getImageViewer().current == 0) {
                    Main.getImageViewer().drawImageFromFolderBasedOnNumber();
                }
            }
        });
    }

    @FXML
    protected void xivCharComboBoxKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!held) {
                held = true;
                String xivChar = (String) xivCharListComboBox.getValue();
                if (xivCharListComboBox.getItems().contains(xivChar)) {
                    xivCharList.getSelectionModel().selectIndices(xivCharList.getItems().indexOf(xivChar));
                    loadXivCharacterData(charIds.get(xivChar));

                    if (xivCharTab.isSelected()) {
                        loadXivCharacterData(charIds.get(xivChar));
                    } else if (xivGroupTab.isSelected()) {
                        loadXivGroupData(charIds.get(xivChar));
                    }
                }
            }
        }
    }

    @FXML
    protected void xivCharListDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ObservableList xivChar = xivCharList.getSelectionModel().getSelectedItems();
            if (xivCharTab.isSelected()) {
                loadXivCharacterData(charIds.get(xivChar.get(0)));
            } else if (xivGroupTab.isSelected()) {
                loadXivGroupData(charIds.get(xivChar.get(0)));
            }
        }
    }

    @FXML
    protected void xivGroupMemberListDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ObservableList xivChar = xivGroupCharactersList.getSelectionModel().getSelectedItems();
            if (xivGroupTab.isSelected()) {
                xivCategoryTabPane.getSelectionModel().select(xivCharTab);
                loadXivCharacterData(charIds.get(xivChar.get(0)));
            }
        }
    }

    private void loadXivCharacterData(int index) {
        try {
            xivNoteList.getItems().clear();
            noteMap.clear();
            xivNoteTextFlow.clear();

            XivSQLDataHandler.getXivCharactersResultSet().absolute(index);
            xivCharacterSqlIDLabel.setText(XivSQLDataHandler.getXivCharactersResultSet().getString("ID"));
            xivLodestoneField.setText(XivSQLDataHandler.getXivCharactersResultSet().getString("lodestone_url"));
            xivServerField.getSelectionModel().select(Server.valueOf(XivSQLDataHandler.getXivCharactersResultSet().getString("Server")));
            xivNameField.setText(XivSQLDataHandler.getXivCharactersResultSet().getString("name"));

            xivRaceField.getSelectionModel().select((XivSQLDataHandler.getXivCharactersResultSet().getString("Race")));
            xivSubraceField.getSelectionModel().select((XivSQLDataHandler.getXivCharactersResultSet().getString("Subrace")));
            xivSexField.getSelectionModel().select((XivSQLDataHandler.getXivCharactersResultSet().getString("Sex")));
            xivMainJobField.getSelectionModel().select((XivSQLDataHandler.getXivCharactersResultSet().getString("MainJob")));
            xivFcField.setText(XivSQLDataHandler.getXivCharactersResultSet().getString("FC"));

            xivCharLastUpdatedField.setText(XivSQLDataHandler.getXivCharactersResultSet().getString("Last_updated"));

            loadXivCharacterNotes();
            loadXivCharacterConnections(index);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadXivGroupData(int index) {
        try {
            xivNoteList.getItems().clear();
            xivGroupCharactersList.getItems().clear();
            xivNoteTextFlow.clear();

            XivSQLDataHandler.getXivGroupsResultSet().absolute(index);
            xivGroupSqlIDLabel.setText(XivSQLDataHandler.getXivGroupsResultSet().getString("ID"));
            xivGroupNameField.setText(XivSQLDataHandler.getXivGroupsResultSet().getString("name"));
            xivGroupLinkField.setText(XivSQLDataHandler.getXivGroupsResultSet().getString("link"));

            xivGroupLastUpdatedField.setText(XivSQLDataHandler.getXivGroupsResultSet().getString("Last_updated"));

            loadXivGroupNotes();
            loadXivGroupMembers(index);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadXivCharacterNotes() {
        try {
            noteMap.clear();
            ResultSet xivCharacterNotes = XivSQLDataHandler.getXivCharacterNotes(Integer.parseInt(xivCharacterSqlIDLabel.getText()));

            while (xivCharacterNotes.next()) {
                if (!xivNoteList.getItems().contains(xivCharacterNotes.getString("type"))) {
                    xivNoteList.getItems().add(xivCharacterNotes.getString("type"));
                }
                noteMap.put(xivCharacterNotes.getString("type"), xivCharacterNotes.getString("Note"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadXivGroupNotes() {
        try {
            noteMap.clear();
            ResultSet xivGroupNotes = XivSQLDataHandler.getXivGroupNotes(Integer.parseInt(xivGroupSqlIDLabel.getText()));

            while (xivGroupNotes.next()) {
                if (!xivNoteList.getItems().contains(xivGroupNotes.getString("type"))) {
                    xivNoteList.getItems().add(xivGroupNotes.getString("type"));
                }
                noteMap.put(xivGroupNotes.getString("type"), xivGroupNotes.getString("Note"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadGeneralNotes() {
        try {
            ArrayList<Integer> expanded = new ArrayList<>();
            if (noteLeafSQLID.keySet().size() > 0) {
                for (Object o : noteLeafSQLID.keySet()) {
                    TreeItem leaf = (TreeItem) o;
                    if (leaf.isExpanded()) {
                        expanded.add(noteLeafSQLID.get(leaf));
                    }
                }
            }
            noteSQLID.clear();
            ResultSet generalRootNotes = NotesSQLDataHandler.getRootNotes("G");
            while (generalRootNotes.next()) {
                noteSQLID.put(generalRootNotes.getRow(), generalRootNotes.getInt("ID"));
            }
            TreeItem root = new TreeItem();
            root.setValue("Root");
            root.setExpanded(true);
            generalNoteTreeList.setRoot(root);
            for (int i : noteSQLID.keySet()) {
                TreeItem leaf = new TreeItem();
                generalRootNotes.absolute(i);
                leaf.setValue(generalRootNotes.getString("Type"));
                generalNoteTreeList.getRoot().getChildren().add(leaf);
                noteLeafSQLID.put(leaf, noteSQLID.get(i));
            }
            for (Object o : generalNoteTreeList.getRoot().getChildren().toArray()) {
                TreeItem leaf = (TreeItem) o;
                addLeavesToNode(leaf);
            }
            for (Object o : noteLeafSQLID.keySet()) {
                TreeItem leaf = (TreeItem) o;
                if (expanded.contains(noteLeafSQLID.get(leaf))) {
                    leaf.setExpanded(true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAltNotes() {
        try {
            ArrayList<Integer> expanded = new ArrayList<>();
            if (altNoteLeafSQLID.keySet().size() > 0) {
                for (Object o : altNoteLeafSQLID.keySet()) {
                    TreeItem leaf = (TreeItem) o;
                    if (leaf.isExpanded()) {
                        expanded.add(altNoteLeafSQLID.get(leaf));
                    }
                }
            }
            altNoteSQLID.clear();
            ResultSet altRootNodes = NotesSQLDataHandler.getRootNotes("A");
            while (altRootNodes.next()) {
                altNoteSQLID.put(altRootNodes.getRow(), altRootNodes.getInt("ID"));
            }
            TreeItem root = new TreeItem();
            root.setValue("Root");
            root.setExpanded(true);
            altNoteTreeList.setRoot(root);
            for (int i : altNoteSQLID.keySet()) {
                TreeItem leaf = new TreeItem();
                altRootNodes.absolute(i);
                leaf.setValue(altRootNodes.getString("Type"));
                altNoteTreeList.getRoot().getChildren().add(leaf);
                altNoteLeafSQLID.put(leaf, altNoteSQLID.get(i));
            }
            for (Object o : altNoteTreeList.getRoot().getChildren().toArray()) {
                TreeItem leaf = (TreeItem) o;
                addLeavesToAltNode(leaf);
            }
            for (Object o : altNoteLeafSQLID.keySet()) {
                TreeItem leaf = (TreeItem) o;
                if (expanded.contains(altNoteLeafSQLID.get(leaf))) {
                    leaf.setExpanded(true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addLeavesToNode(TreeItem parent) {
        try {
            try (ResultSet generalChildNotes = NotesSQLDataHandler.getChildNotes(noteLeafSQLID.get(parent), "G")) {
                while (generalChildNotes.next()) {
                    TreeItem leaf = new TreeItem();
                    leaf.setValue(generalChildNotes.getString("Type"));
                    parent.getChildren().add(leaf);
                    noteLeafSQLID.put(leaf, generalChildNotes.getInt("ID"));

                    addLeavesToNode(leaf);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addLeavesToAltNode(TreeItem parent) {
        try {
            try (ResultSet altChildNodes = NotesSQLDataHandler.getChildNotes(altNoteLeafSQLID.get(parent), "A")) {
                while (altChildNodes.next()) {
                    TreeItem leaf = new TreeItem();
                    leaf.setValue(altChildNodes.getString("Type"));
                    parent.getChildren().add(leaf);
                    altNoteLeafSQLID.put(leaf, altChildNodes.getInt("ID"));

                    addLeavesToAltNode(leaf);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void xivSaveNotesButtonAction(Event event) {
        String note = xivNoteTextFlow.getText();
        String type = (String) xivNoteTypeField.getText();
        if (xivConnectionsTab.isSelected()) {
            ObservableList selectedItems = xivConnectionsList.getSelectionModel().getSelectedItems();
            String item = (String) selectedItems.get(0);
            if (item.subSequence(0, 6).toString().contains("Group:")) {

            } else {

            }
        } else if (xivNotesTab.isSelected()) {
            if (xivGroupTab.isSelected()) {
                int charID = Integer.parseInt(xivGroupSqlIDLabel.getText());
                XivSQLDataHandler.setXivGroupNote(charID, type, note);
                loadXivGroupData(charIds.get(xivGroupNameField.getText()));
                xivNoteList.getSelectionModel().select(type);
                xivOpenSelectedNote();
                noticeSnackbar("Saved");

            } else if (xivCharTab.isSelected()) {
                int charID = Integer.parseInt(xivCharacterSqlIDLabel.getText());
                XivSQLDataHandler.setCharacterNote(charID, type, note);
                loadXivCharacterData(charIds.get((String) xivOwnerNameComboBox.getSelectionModel().getSelectedItem()));
                xivNoteList.getSelectionModel().select(type);
                xivOpenSelectedNote();
                noticeSnackbar("Saved");

            }

        }
    }

    @FXML
    protected void xivCharNotesAddNew() {
        String type = (String) xivCharNotesAddNewType.getText();
        if (type.length() > 1) {
            if (xivCharTab.isSelected()) {
                int charID = Integer.parseInt(xivCharacterSqlIDLabel.getText());
                if (xivNoteList.getItems().contains(type)) {
                    noticeSnackbar("Note of type " + type + " is already registered");
                } else {
                    XivSQLDataHandler.setCharacterNote(charID, type, "");
                    loadXivCharacterData(charIds.get((String) xivNameField.getText() + ", " + xivServerField.getSelectionModel().getSelectedItem().toString()));
                    xivNoteList.getSelectionModel().select(type);
                    xivOpenSelectedNote();
                    noticeSnackbar("Saved");

                }
            } else if (xivGroupTab.isSelected()) {
                int grpID = Integer.parseInt(xivGroupSqlIDLabel.getText());
                if (xivNoteList.getItems().contains(type)) {
                    noticeSnackbar("Note of type " + type + " is already registered");
                } else {
                    XivSQLDataHandler.setXivGroupNote(grpID, type, "");
                    loadXivGroupData(charIds.get(xivGroupNameField.getText()));
                    xivNoteList.getSelectionModel().select(type);
                    xivOpenSelectedNote();
                    noticeSnackbar("Saved");

                }
            }
        } else {
            noticeSnackbar("Type too short.");
        }
    }

    @FXML
    protected void xivNoteListKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            if (!held) {
                held = true;
                if (xivCharTab.isSelected()) {
                    int charID = Integer.parseInt(xivCharacterSqlIDLabel.getText());
                    String type = (String) xivNoteList.getSelectionModel().getSelectedItem();
                    if (confirmationDialog("Sure you want to delete the note '" + type + "' from " + xivNameField.getText() + "?")) {
                        XivSQLDataHandler.deleteCharacterNote(charID, type);
                        loadXivCharacterData(charIds.get((String) xivNameField.getText() + ", " + xivServerField.getSelectionModel().getSelectedItem().toString()));
                        noticeSnackbar("Deleted");

                    }
                } else if (xivGroupTab.isSelected()) {
                    int grpID = Integer.parseInt(xivGroupSqlIDLabel.getText());
                    String type = (String) xivNoteList.getSelectionModel().getSelectedItem();
                    if (confirmationDialog("Sure you want to delete the note '" + type + "' from " + xivGroupNameField.getText() + "?")) {
                        XivSQLDataHandler.deleteGroupNote(grpID, type);
                        loadXivGroupData(charIds.get(xivGroupNameField.getText()));
                        noticeSnackbar("Deleted");

                    }
                }
            }
        }
    }

    private void loadXivGroupMembers(int index) {
        try {
            //noteMap.clear();
            XivSQLDataHandler.getXivGroupsResultSet().absolute(index);
            int groupID = XivSQLDataHandler.getXivGroupsResultSet().getInt("ID");

            ResultSet xivGroupMembers = XivSQLDataHandler.getXivGroupMembers(groupID);

            while (xivGroupMembers.next()) {
                String name = xivGroupMembers.getString("Name");
                String server = xivGroupMembers.getString("Server");
                String combo = name + ", " + server;
                xivGroupCharactersList.getItems().add(combo);
            }
            xivGroupCharactersList.refresh();

        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadXivCharacterConnections(int index) {
        try {
            xivConnectionsList.getItems().clear();
            cCConNoteMap.clear();
            XivSQLDataHandler.getXivCharactersResultSet().absolute(index);
            int charID = XivSQLDataHandler.getXivCharactersResultSet().getInt("ID");
            ResultSet xivCCConnections = XivSQLDataHandler.getXivCharacterCharacterConnections(charID);
            ResultSet xivCCNote = XivSQLDataHandler.getXivCharacterCharacterConnectionNotes(charID);
            while (xivCCConnections.next()) {
                String name = xivCCConnections.getString("Name");
                String server = xivCCConnections.getString("Server");
                String label = xivCCConnections.getString("label");
                String combo = name + ", " + server + " => " + label;

                xivConnectionsList.getItems().add(combo);
                cConMap.put(combo, xivCCConnections.getRow());
                if (xivCCNote.absolute(xivCCConnections.getRow())) {
                    cCConNoteMap.put(combo, xivCCNote.getString("note"));
                }
            }
            ResultSet xivCGConnections = XivSQLDataHandler.getXivCharacterGroupConnections(charID);
            while (xivCGConnections.next()) {
                String name = xivCGConnections.getString("Name");
                String combo = "Group: " + name;

                ResultSet xivCGNote = XivSQLDataHandler.getXivCharacterGroupConnectionNotes(charID);

                xivConnectionsList.getItems().add(combo);

                cConMap.put(combo, xivCGConnections.getRow());
                if (xivCGNote != null && xivCGNote.next()) {
                    cCGonNoteMap.put(combo, xivCGNote.getString("note"));

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void xivNoteListClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            xivOpenSelectedNote();
        }
    }

    private void xivOpenSelectedNote() {
        ObservableList selectedItems = xivNoteList.getSelectionModel().getSelectedItems();
        String type = (String) selectedItems.get(0);
        xivNoteTextFlow.clear();
        xivNoteTextFlow.setText(noteMap.get(type));

        if (xivCharTab.isSelected()) {
            xivOwnerNameComboBox.getSelectionModel().select(xivNameField.getText() + ", " + xivServerField.getSelectionModel().getSelectedItem());
        } else if (xivGroupTab.isSelected()) {
            xivOwnerNameComboBox.getSelectionModel().select(xivGroupNameField.getText());
        }
        xivNoteTypeField.setText(type);
    }

    @FXML
    protected void xivCharacterSave(Event event) {
        try {
            int charID = -1;
            if (xivCharacterSqlIDLabel.getText().equals("NO ID")) {
                charID = -1;
            } else {
                try {
                    charID = Integer.parseInt(xivCharacterSqlIDLabel.getText());
                } catch (Exception ex) {
                    noticeSnackbar(ex.toString());
                    charID = -1;
                }
            }
            String[] xivCharacterValuesFromGUI = xivCharacterValuesFromGUI();

            String lodestone = xivCharacterValuesFromGUI[0];
            String server = xivCharacterValuesFromGUI[1];
            String name = xivCharacterValuesFromGUI[2];
            String race = xivCharacterValuesFromGUI[3];
            String subrace = xivCharacterValuesFromGUI[4];
            String sex = xivCharacterValuesFromGUI[5];
            String job = xivCharacterValuesFromGUI[6];
            String fc = xivCharacterValuesFromGUI[7];

            if (charID > 0) {
                if (confirmationDialog("Character exists, overwrite?")) {
                    XivSQLDataHandler.saveCharacter(charID, lodestone, server, name, race, subrace, sex, job, fc);
                    noticeSnackbar("Saved");

                }
            } else {
                XivSQLDataHandler.saveCharacter(charID, lodestone, server, name, race, subrace, sex, job, fc);
                noticeSnackbar("Saved");
                setListAsCharacterList();
            }
            setListAsCharacterList();
            loadXivCharacterData(charIds.get(name + ", " + server));

        } catch (SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void xivGroupSaveButton(Event event) {
        try {
            int GrpID = -1;
            if (xivGroupSqlIDLabel.getText().equals("NO ID")) {
                GrpID = -1;
            } else {
                try {
                    GrpID = Integer.parseInt(xivGroupSqlIDLabel.getText());
                } catch (Exception ex) {
                    noticeSnackbar(ex.toString());
                    GrpID = -1;
                }
            }
            String name = xivGroupNameField.getText();
            String link = xivGroupLinkField.getText();

            if (GrpID > 0) {
                if (confirmationDialog("Group exists, overwrite?")) {
                    XivSQLDataHandler.saveGroup(GrpID, name, link);
                    noticeSnackbar("Saved");
                }
            } else {
                XivSQLDataHandler.saveGroup(GrpID, name, link);
                noticeSnackbar("Saved");
                setListAsGroupList();

            }
            setListAsGroupList();
            loadXivGroupData(charIds.get(name));

        } catch (SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void generalNotesSaveButtonAction(Event event) {
        try {
            int id = -1;
            int parentID = -1;
            String noteName = generalNoteNameField.getText();
            String note = generalNoteTextArea.getText();
            TreeItem node = (TreeItem) generalNoteTreeList.getSelectionModel().getSelectedItem();
            TreeItem parent = node.getParent();
            if (noteLeafSQLID.get(node) != null) {
                id = noteLeafSQLID.get(node);
            }
            if (noteLeafSQLID.get(parent) != null) {
                parentID = noteLeafSQLID.get(parent);
            }
            int selectedIndex = generalNoteTreeList.getSelectionModel().getSelectedIndex();

            NotesSQLDataHandler.saveNote(id, noteName, note, parentID, "G");
            loadGeneralNotes();

            generalNoteTreeList.getSelectionModel().select(selectedIndex);

        } catch (SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCurrentGeneralNote() {
        try {
            int get = -1;
            String parentName = "root";
            TreeItem node = (TreeItem) generalNoteTreeList.getSelectionModel().getSelectedItem();
            if (noteLeafSQLID.get(node) != null) {
                get = noteLeafSQLID.get(node);
            }
            ResultSet note = NotesSQLDataHandler.getNote(get);
            if (note.absolute(1) && get > -1) {
                generalNoteSQLID.setText(get + "");
                generalNoteNameField.setText(note.getString("Type"));
                if (note.getString("TreeParent") != null) {
                    parentName = note.getString("TreeParent");
                }
                generalNoteParentNameField.setText(parentName);
                generalNoteTextArea.setText(note.getString("Note"));
            } else {
                generalNoteSQLID.setText("None");
                generalNoteNameField.setText("Unnamed");
                generalNoteParentNameField.setText("Unnamed");
                generalNoteTextArea.clear();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void altNotesSaveButtonAction(Event event) {
        try {
            int id = -1;
            int parentID = -1;
            String noteName = altNoteNameField.getText();
            String note = altNoteTextArea.getText();
            TreeItem node = (TreeItem) altNoteTreeList.getSelectionModel().getSelectedItem();
            TreeItem parent = node.getParent();
            if (altNoteLeafSQLID.get(node) != null) {
                id = altNoteLeafSQLID.get(node);
            }
            if (altNoteLeafSQLID.get(parent) != null) {
                parentID = altNoteLeafSQLID.get(parent);
            }
            int selectedIndex = altNoteTreeList.getSelectionModel().getSelectedIndex();

            NotesSQLDataHandler.saveNote(id, noteName, note, parentID, "A");
            loadAltNotes();

            altNoteTreeList.getSelectionModel().select(selectedIndex);

        } catch (SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCurrentAltNote() {
        try {
            int get = -1;
            String parentName = "root";
            TreeItem node = (TreeItem) altNoteTreeList.getSelectionModel().getSelectedItem();
            if (altNoteLeafSQLID.get(node) != null) {
                get = altNoteLeafSQLID.get(node);
            }
            ResultSet note = NotesSQLDataHandler.getNote(get);
            if (note.absolute(1) && get > -1) {
                altNoteSQLIDLabel.setText(get + "");
                altNoteNameField.setText(note.getString("Type"));
                if (note.getString("TreeParent") != null) {
                    parentName = note.getString("TreeParent");
                }
                altNoteParentNameField.setText(parentName);
                altNoteTextArea.setText(note.getString("Note"));
            } else {
                altNoteSQLIDLabel.setText("None");
                altNoteNameField.setText("Unnamed");
                altNoteParentNameField.setText("Unnamed");
                altNoteTextArea.clear();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void xivDeleteGroupAction(Event event) {
        try {
            int GrpID = -1;
            if (xivGroupSqlIDLabel.getText().equals("NO ID")) {
                GrpID = -1;
            } else {
                try {
                    GrpID = Integer.parseInt(xivGroupSqlIDLabel.getText());
                } catch (Exception ex) {
                    noticeSnackbar(ex.toString());
                    GrpID = -1;
                }
            }
            if (GrpID > 0) {
                if (confirmationDialog("Absolutely certain you intend on deleting the group '" + xivGroupNameField.getText() + "'?")) {
                    XivSQLDataHandler.deleteGroup(GrpID);
                    noticeSnackbar("Deleted");
                }
            }
            setListAsGroupList();

        } catch (SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void xivDeleteCharacterAction(Event event) {
        try {
            int charID = -1;
            if (xivCharacterSqlIDLabel.getText().equals("NO ID")) {
                charID = -1;
            } else {
                try {
                    charID = Integer.parseInt(xivCharacterSqlIDLabel.getText());
                } catch (Exception ex) {
                    noticeSnackbar(ex.toString());
                    charID = -1;
                }
            }
            if (charID > 0) {
                if (confirmationDialog("Absolutely certain you intend on deleting the character '" + xivNameField.getText() + "'?")) {
                    XivSQLDataHandler.deleteCharcater(charID);
                    noticeSnackbar("Deleted");
                }
            }
            setListAsCharacterList();

        } catch (SecurityException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] xivCharacterValuesFromGUI() {
        String lodestone = xivLodestoneField.getText();
        String server = xivServerField.getSelectionModel().getSelectedItem().toString();
        String name = xivNameField.getText();
        String race = xivRaceField.getSelectionModel().getSelectedItem().toString();
        String subrace = xivSubraceField.getSelectionModel().getSelectedItem().toString();
        String sex = xivSexField.getSelectionModel().getSelectedItem().toString();
        String job = xivMainJobField.getSelectionModel().getSelectedItem().toString();
        String fc = xivFcField.getText();

        return new String[]{lodestone, server, name, race, subrace, sex, job, fc};
    }

    @FXML
    protected void xivCharacterClearFieldsAction(Event event) {
        xivCharacterSqlIDLabel.setText("NO ID");
        xivLodestoneField.setText("");
        xivNameField.setText("");
        xivFcField.setText("");
        xivNoteList.getItems().clear();
        xivNoteTextFlow.clear();
        xivConnectionsList.getItems().clear();
    }

    @FXML
    protected void xivGroupClearFieldsAction(Event event) {
        xivGroupSqlIDLabel.setText("NO ID");
        xivGroupNameField.setText("");
        xivGroupLinkField.setText("");

        xivGroupCharactersList.getItems().clear();
        xivNoteList.getItems().clear();
        xivNoteTextFlow.clear();
        xivConnectionsList.getItems().clear();
    }

    @FXML
    protected void imagesSelectionChanged(Event event) {
        FXMain.imageEventFilter(imagesTab.isSelected());
    }

    @FXML
    protected void xivTabSelectionChanged(Event event) {
        if (xivNoteTextFlow != null) {
            xivNoteTextFlow.clear();
        }
        try {
            if (xivGroupTab.isSelected() && xivCharList != null) {
                setListAsGroupList();
            } else if (xivCharTab.isSelected() && xivCharList != null) {
                setListAsCharacterList();
            }
        } catch (Exception ex) {
        }
    }

    @FXML
    protected void tagBoxAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!held) {
                held = true;
                String tag = (String) TagComboBox.getValue();
                if (!TagComboBox.getItems().contains(tag)) {
                    if (!confirmationDialog("Tag '" + tag + "' doesn't exist yet, add anyway?")) {
                        return;
                    }
                }
                if (!tagList.getItems().contains(tag)) {
                    tagList.getItems().add(tag);
                }
                ArrayList<String> tagsArray = new ArrayList<>();
                for (Object o : tagList.getItems().toArray()) {
                    tagsArray.add(o.toString());
                }
                tagsArray = tags.addImplications(tag, tagsArray);

                for (String t : tagsArray) {
                    if (!tagList.getItems().contains(t)) {
                        tagList.getItems().add(t);
                    }
                }
            }

            TagComboBox.setValue("");

            tagList.refresh();
            setTagContextMenu();
            suggestionList.refresh();
            tags.saveTags(this);
            tags.suggestions(this);
        }
    }

    @FXML
    protected void whiteListBoxAction(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!held) {
                held = true;
                String tag = (String) selectionComboBox.getValue();
                if (!tagBox.getItems().contains(tag)) {
                    tagBox.getItems().add(tag);
                }
                tagBox.refresh();
            }
        }
    }

    @FXML
    protected void blackListBoxAction(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!held) {
                held = true;
                String tag = (String) BLselectionComboBox.getValue();
                if (!BLtagBox.getItems().contains(tag)) {
                    BLtagBox.getItems().add(tag);
                }
                BLtagBox.refresh();
            }
        }
    }

    @FXML
    protected void xivGroupCharComboBoxKeyPressed(KeyEvent e
    ) {
        if (e.getCode() == KeyCode.ENTER) {
            if (!held) {
                held = true;
                if (!xivGroupCharactersList.getItems().contains(xivGroupCharacterComboBox.getSelectionModel().getSelectedItem())) {
                    XivSQLDataHandler.addCharacterToGroup(Integer.parseInt(xivGroupSqlIDLabel.getText()), charSQLID.get(xivGroupCharacterComboBox.getSelectionModel().getSelectedItem().toString()));
                    loadXivGroupData(charIds.get(xivGroupNameField.getText()));
                }
            }
        }
    }

    @FXML
    protected void tagListKeyPressed(KeyEvent event) {
        ImageWrapper image;
        try {
            image = new ImageWrapper(ImageSQLHandler.getImageQuery().getString("path"), null);
            if (event.getCode() == KeyCode.DELETE) {
                if (!held) {
                    held = true;
                    ObservableList selectedItems = tagList.getSelectionModel().getSelectedItems();
                    ListIterator listIterator = selectedItems.listIterator();
                    while (listIterator.hasNext()) {
                        String tag = (String) listIterator.next();
                        ImageSQLHandler.removeImageTag(image, tag);
                        noticeSnackbar(tag);
                    }
                    tagList.getItems().removeAll(selectedItems);
                    tagList.refresh();
                    setTagContextMenu();
                    ImageSQLHandler.removeOrphnaedTags();

                }
            }
            if (event.getCode() == KeyCode.C && event.isControlDown()) {
                tagCopyList.addAll(tagList.getItems());
            }
            if (event.getCode() == KeyCode.V && event.isControlDown()) {
                tagList.getItems().addAll(tagCopyList);

            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        suggestionList.refresh();
        tags.saveTags(this);
        tags.suggestions(this);
    }

    @FXML
    protected void xivGroupCharactersListKeyPressed(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.DELETE) {
            if (!held) {
                held = true;
                String selectedItems = xivGroupCharactersList.getSelectionModel().getSelectedItem().toString();
                Integer get = charSQLID.get(selectedItems);
                XivSQLDataHandler.removeCharacterFromGroup(Integer.parseInt(xivGroupSqlIDLabel.getText()), get);
                xivGroupCharactersList.getItems().removeAll(selectedItems);
                xivGroupCharactersList.refresh();
            }
        }
    }

    @FXML
    protected void whiteListKeyPressed(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.DELETE) {
            if (!held) {
                held = true;
                ObservableList selectedItems = tagBox.getSelectionModel().getSelectedItems();
                tagBox.getItems().removeAll(selectedItems);
                tagBox.refresh();
            }
        }
    }

    @FXML
    protected void blackListKeyPressed(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.DELETE) {
            if (!held) {
                held = true;
                ObservableList selectedItems = BLtagBox.getSelectionModel().getSelectedItems();
                BLtagBox.getItems().removeAll(selectedItems);
                BLtagBox.refresh();
            }
        }
    }

    @FXML
    protected void imageKeyPress(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.O && event.isControlDown()) {
            if (!held) {
                held = true;
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + ImageSQLHandler.getImageQuery().getString("Path"));

                } catch (IOException ex) {
                    Logger.getLogger(ImageViewer.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (SQLException ex) {
                    Logger.getLogger(FXMLController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    protected void scrollStart(KeyEvent event
    ) {
        ThumbList.setPrefColumns(10);
    }

    @FXML
    protected void filterButton() {
        tags.filter(this);
    }

    @FXML
    protected void keyReleased(KeyEvent event
    ) {
        held = false;
    }

    @FXML
    protected void xivOpenLodestonePage(Event event
    ) {
        xivUtils.openWebpage(xivLodestoneField.getText());
    }

    @FXML
    protected void xivOpenGroupLink(Event event
    ) {
        xivUtils.openWebpage(xivGroupLinkField.getText());
    }

    @FXML
    protected void xivImportCharacterDataFromLodestone(Event event
    ) {
        if (xivLodestoneField.getText().length() > 2) {
            String[] parse = xivUtils.parseCharacterFromLodestone(xivLodestoneField.getText(), null, null);
            xivServerField.getSelectionModel().select(parse[0]);
            xivNameField.setText(parse[1]);
            xivRaceField.getSelectionModel().select(parse[2]);
            xivSubraceField.getSelectionModel().select(parse[3]);
            xivSexField.getSelectionModel().select(parse[4]);
            xivFcField.setText(parse[5]);
        } else if (xivNameField.getText().length() > 1) {
            String[] parse = xivUtils.parseCharacterFromLodestone(null, xivNameField.getText(), xivServerField.getSelectionModel().getSelectedItem().toString());
            xivServerField.getSelectionModel().select(parse[0]);
            xivNameField.setText(parse[1]);
            xivRaceField.getSelectionModel().select(parse[2]);
            xivSubraceField.getSelectionModel().select(parse[3]);
            xivSexField.getSelectionModel().select(parse[4]);
            xivFcField.setText(parse[5]);
            xivLodestoneField.setText(parse[6]);
        }
    }

    @FXML
    protected void SuggestionClick(MouseEvent event
    ) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ObservableList selectedItems = suggestionList.getSelectionModel().getSelectedItems();
            selectedItems.stream().forEach((s) -> {
                String tag = (String) s;
                tag = tag.subSequence(0, tag.lastIndexOf(",")).toString();
                tag = tag.trim();
                tagList.getItems().add(tag);
                suggestionList.getItems().remove(s);
            });
            tagList.refresh();
            setTagContextMenu();
            suggestionList.refresh();
            tags.saveTags(this);
            tags.suggestions(this);
        }
    }

    @FXML
    protected void loadSuggestionsCheckBoxAction(Event e) {
        Main.computeSuggestions = loadSuggestionsCheckBox.isSelected();
    }

    @FXML
    protected void xivConnectionsListClicked(MouseEvent event
    ) {
        xivNoteTextFlow.clear();
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
            ObservableList selectedItems = xivConnectionsList.getSelectionModel().getSelectedItems();

            String item = (String) selectedItems.get(0);
            if (item != null && !item.equals("") && item.length() > 6) {
                if (item.subSequence(0, 6).toString().contains("Group:")) {
                    String get = cCGonNoteMap.get(item);
                    xivNoteTextFlow.setText(get);
                    xivOwnerNameComboBox.getSelectionModel().select(xivGroupNameField.getText());
                } else {
                    String get = cCConNoteMap.get(item);
                    xivNoteTextFlow.setText(get);
                    xivOwnerNameComboBox.getSelectionModel().select(xivNameField.getText() + ", " + xivServerField.getSelectionModel().getSelectedItem().toString());
                }
            }
        }
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ObservableList selectedItems = xivConnectionsList.getSelectionModel().getSelectedItems();
            String item = (String) selectedItems.get(0);
            //System.out.println(item.subSequence(0, 6));

            if (item != null && !item.equals("") && item.length() > 6) {
                if (item.subSequence(0, 6).toString().contains("Group:")) {
                    xivCategoryTabPane.getSelectionModel().select(xivGroupTab);
                    loadXivGroupData(charIds.get(item.split(":", 2)[1].trim()));
                } else if (!item.equals("") && item.length() > 1) {
                    xivCategoryTabPane.getSelectionModel().select(xivCharTab);
                    loadXivCharacterData(charIds.get(item.split("=", 2)[0].trim()));
                }
            }
        }
    }

    @FXML
    protected void xivAddConnectionButtonAction(Event event
    ) {
        if (xivCharTab.isSelected()) {
            boolean directed = xivConnectionDirectedCheckBox.isSelected();
            int char_1 = Integer.parseInt(xivCharacterSqlIDLabel.getText());
            String target = xivConnectionTargetComboBox.getSelectionModel().getSelectedItem().toString();
            int char_2 = charSQLID.get(target);
            String label = xivConnectionLabelField.getText();

            XivSQLDataHandler.addCharacterToCharacterConnection(char_1, char_2, label, directed);
            loadXivCharacterData(charIds.get(xivNameField.getText() + ", " + xivServerField.getSelectionModel().getSelectedItem().toString()));
        } else if (xivGroupTab.isSelected()) {

        }
    }

    @FXML
    protected void xivConnectionsListKeyPressed(KeyEvent event
    ) {
        if (xivCharTab.isSelected()) {
            if (event.getCode() == KeyCode.DELETE) {
                if (!held) {
                    held = true;
                    int char_1 = Integer.parseInt(xivCharacterSqlIDLabel.getText());
                    String target = xivConnectionsList.getSelectionModel().getSelectedItem().toString();
                    int char_2 = charSQLID.get(target.split("=", 2)[0].trim());
                    String label = xivConnectionsList.getSelectionModel().getSelectedItem().toString().split(">", 2)[1].trim();
                    XivSQLDataHandler.removeCharacterToCharacterConnection(char_1, char_2, label);
                    loadXivCharacterData(charIds.get(xivNameField.getText() + ", " + xivServerField.getSelectionModel().getSelectedItem().toString()));
                    noticeSnackbar("Deleted");
                }
            }
        } else if (xivGroupTab.isSelected()) {

        }
    }

    public void selectify(ImageView get, boolean select) {
        if (select) {
            Image original = get.getImage();
            Rectangle clip = new Rectangle(
                    get.getFitWidth(),
                    get.getFitHeight()
            );
            clip.setArcWidth(40);
            clip.setArcHeight(40);
            get.setClip(clip);

            // snapshot the rounded image.
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.GREEN);
            WritableImage image2 = get.snapshot(parameters, null);
            get.setClip(null);
            get.setEffect(new DropShadow(20, Color.TRANSPARENT));
            get.setImage(image2);
            selectedThumbs.put(get, original);
        }
    }

    public void unSelectAllThumbs() {
        selectedThumbs.keySet().stream().forEach((i) -> {
            i.setImage(selectedThumbs.get(i));
        });
        selectedThumbs.clear();
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private ImageView createImageView(final File imageFile) {
        ImageView imageView = null;
        try {
            Image image = new Image(new FileInputStream(imageFile));

            imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            imageView.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    ImageView get = thumbMap.get(imageFile);
                    if (mouseEvent.getClickCount() == 1) {
                        unSelectAllThumbs();
                        selectify(get, true);
                    }
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            int indexOf = ThumbList.getChildren().indexOf(get);
                            Main.getImageViewer().drawSpecific(indexOf);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        return imageView;
    }
}
