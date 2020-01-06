/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import Algorithms.TagAlgorithms;
import DataStructures.ImageWrapper;
import Gui.Utils.Image.tags;
import IO.ImageSQLHandler;
import Main.Main;
import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 *
 * @author Peter
 */
public class ImageViewer2 extends javax.swing.JPanel {

    ImagePanel imgp;
    ThumbListPanel tlp;
    ThumbForm tf;
    MediaFrame mframe;
    public File imageFile;
    int current = 0;
    int size = 0;
    Main parent;
    //EmbeddedMediaPlayerComponent mediaPlayerComponent;
    MediaPanel mediapanel;
    private static final String NATIVE_LIBRARY_SEARCH_PATH = "C:\\Program Files\\VideoLAN\\VLC\\";
    boolean imagePanel = false;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int i) {
        current = i;
    }

    public void next() {
        if (current >= (size)) {
            current = (size);
            try {
                ImageSQLHandler.getImageQuery().last();

            } catch (SQLException ex1) {
                Logger.getLogger(ImageViewer.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } else {
            try {
                ImageSQLHandler.getImageQuery().next();
            } catch (SQLException ex) {
                // Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }
            current++;
            drawImageFromFolderBasedOnNumber();
        }
    }

    public void prev() {
        if (current < 0) {
            current = 0;
            try {
                ImageSQLHandler.getImageQuery().first();

            } catch (SQLException ex1) {
                Logger.getLogger(ImageViewer.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        } else {
            try {
                ImageSQLHandler.getImageQuery().previous();
            } catch (SQLException ex) {
                //Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }
            current--;
            drawImageFromFolderBasedOnNumber();

        }
    }

    public ImageViewer2() {
      //  new NativeDiscovery().discover();
        //NATIVE_LIBRARY_SEARCH_PATH = "C:\\Program Files (x86)\\VideoLAN\\VLC";

      //  NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        String[] args = {""};
//        NativeLog log = new NativeLog(LibVlc.INSTANCE, LibVlc.INSTANCE.libvlc_new(0, args));

       // log.setLevel(libvlc_log_level_e.WARNING);

        this.size = ImageSQLHandler.imageCount();

        initComponents();
        imgp = new ImagePanel();
//        mediapanel = new MediaPanel();
     //   mframe = new MediaFrame();
       // mframe.setAlwaysOnTop(true);

      //  mframe.setLayout(
       //         new BorderLayout());
      //  mframe.add(mediapanel, BorderLayout.CENTER);

        //mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        //mediaPlayer = new MediaPlayerFactory().newEmbeddedMediaPlayer();
        //this.add(imgp);
        // this.IWtabbedPane.setTitleAt(0, "Image Panel");
        //tlp.setVisible(true);
        //this.IWtabbedPane.setTitleAt(1, "Thumbs");
        //System.out.println("imgp visible");
        //tlp.reloadListDataFromArray();
        //setKeybinds();
    }

    public void initThumbList() {
        ArrayList<ImageWrapper> loadThumbs = ImageSQLHandler.loadThumbs();
        for (ImageWrapper f : loadThumbs) {
            Main.tf.tlp.addImageToModel(f);
        }
        Main.tf.tlp.reloadListDataFromArray();
    }

    private void draw() {
        long startDraw = System.currentTimeMillis();
        long startPanelDraw = 0;
        long endPanelDraw = 0;
        long startRepaint = 0;
        long endRepaint = 0;
        //System.gc();
        try {
            if (mframe != null && mframe.isVisible()) {
                mediapanel.stop();
                mframe.setVisible(false);
            }
            if (imageFile.getName().trim().endsWith(".webm") || imageFile.getName().trim().endsWith(".mp4")) {

//                if (imagePanel) {
                //imagePanel = !imagePanel;
                mframe.setFocusableWindowState(false);
                mframe.setVisible(true);
                mediapanel.poke();
                imgp.setVisible(false);
                startRepaint = System.currentTimeMillis();
                imgp.repaint();
                endRepaint = System.currentTimeMillis();
//                } else {     
                this.repaint();
//                }
            } else {
//                if (!imagePanel) {
//                    FXMLController.setImagePanel();
//                    imagePanel = !imagePanel;
//
//                }

                imgp.setVisible(true);
                startPanelDraw = System.currentTimeMillis();
                imgp.draw(imageFile);
//                FXMain.getController().setImage(imageFile);
                endPanelDraw = System.currentTimeMillis();

                // System.out.println("draw mpc Else");
//                if (mediaPlayerComponent != null) {
//                    mediaPlayerComponent.getMediaPlayer().mute(true);
//                    mediaPlayerComponent.getMediaPlayer().stop();
//                    mediaPlayerComponent.getMediaPlayer().enableOverlay(false);
//                    mediaPlayerComponent.setVisible(false);
//                    this.remove(mediaPlayerComponent);
//                }
//                if (imgp == null){
//                    imgp = new ImagePanel();
//                }
//                System.out.println("draw image");
//                if (!imgp.isVisible()) {
//                    imgp.setVisible(true);
//                    add(imgp, BorderLayout.CENTER);
//                }
//                startPanelDraw = System.currentTimeMillis();
//                imgp.draw(imageFile);
//                endPanelDraw = System.currentTimeMillis();
                try {
                    Node get = FXMain.getController().ThumbList.getChildren().get(current);
                    FXMain.getController().unSelectAllThumbs();
                    FXMain.getController().selectify((ImageView) get, true);
                } catch (NullPointerException ex) {

                }
                startRepaint = System.currentTimeMillis();
                imgp.repaint();
                endRepaint = System.currentTimeMillis();

                //tlp.addImageToModel(imageFile);
                //this.setTitle("IMG: " + imageFile.getName() + " [" + current + " / " + (ImageSQLHandler.getTableSize()) + "]");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            tags.suggestions(FXMain.controller);
            tags.loadTags(FXMain.controller);
            //FXMain.getController().SimilarThumbs();
        }
        if (Main.debugMode) {
            System.out.println("Drawing image took total of: " + (System.currentTimeMillis() - startDraw) + " ms");
            System.out.println("-> Panel Drawing took: " + (endPanelDraw - startPanelDraw) + " ms");
            System.out.println("-> Repaint took: " + (endRepaint - startRepaint) + " ms");
        }
    }

    public void drawSpecific(int i) {
        try {
            this.current = i;
            ImageSQLHandler.getImageQuery().absolute(i + 1);
            System.out.println("drawing index: " + i);
            drawImageFromFolderBasedOnNumber();
        } catch (SQLException ex) {
            Logger.getLogger(ImageViewer.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void drawImageFromFolderBasedOnNumber() {
        try {
            imageFile = null;
            imageFile = new File(ImageSQLHandler.getImageQuery().getString("path"));
            draw();
        } catch (Exception ex) {
            System.out.println(ex);
            if (current < 0) {
                current = 0;
                try {
                    ImageSQLHandler.getImageQuery().first();

                } catch (SQLException ex1) {
                    Logger.getLogger(ImageViewer.class
                            .getName()).log(Level.SEVERE, null, ex1);
                }
            } else if (current >= (size)) {
                current = (size);
                try {
                    ImageSQLHandler.getImageQuery().last();

                } catch (SQLException ex1) {
                    Logger.getLogger(ImageViewer.class
                            .getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(40, 40, 40));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
