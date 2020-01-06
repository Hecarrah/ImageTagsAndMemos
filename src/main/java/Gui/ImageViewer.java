package Gui;

import DataStructures.ImageWrapper;
import IO.ImageSQLHandler;
import Main.Main;
import com.sun.jna.NativeLibrary;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.simple.SimpleMedia;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class ImageViewer extends javax.swing.JFrame {

    ImagePanel imgp;
    ThumbListPanel tlp;
    ThumbForm tf;
    File imageFile;
    int current = 0;
    int size = 0;
    Main parent;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private static final String NATIVE_LIBRARY_SEARCH_PATH = "C:\\Program Files\\VideoLAN\\VLC";

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int i) {
        current = i;
    }

    public void next() {
        try {
            ImageSQLHandler.getImageQuery().next();
        } catch (SQLException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        current++;
        drawImageFromFolderBasedOnNumber();

    }

    public void prev() {
        try {
            ImageSQLHandler.getImageQuery().previous();
        } catch (SQLException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        current--;
        drawImageFromFolderBasedOnNumber();

    }
//<editor-fold defaultstate="collapsed" desc="comment">

    /*
    private final String LEFT = "Left";
    private Action left = new AbstractAction(LEFT) {
    @Override
    public void actionPerformed(ActionEvent e) {
    if (Main.debugMode) {
    System.out.println("PREV IMAGE");
    }
    long currentTimeMillis = System.currentTimeMillis();
    long parentUpdateStart = 0;
    long parentUpdateEnd = 0;
    //long currentTimeMillisTags = System.currentTimeMillis();
    Gui.saveTags();
    //System.out.println("Time to saveTags()  was: " + (System.currentTimeMillis() - currentTimeMillisTags) + " ms");
    try {
    //long currentTimeMillisSQL = System.currentTimeMillis();
    
    if (current > 0) {
    ImageSQLHandler.getImageQuery().previous();
    //System.out.println("Time to getImageQuery.previous()  was: " + (System.currentTimeMillis() - currentTimeMillisSQL) + " ms");
    
    current--;
    parentUpdateStart = System.currentTimeMillis();
    parent.update();
    //parent.getInfoWindow().suggestions();
    parentUpdateEnd = System.currentTimeMillis();
    
    } else {
    }
    } catch (SQLException ex) {
    }
    if (Main.debugMode) {
    System.out.println("Total Time to Previous image was: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
    System.out.println("-> Parent Update took: " + (parentUpdateEnd - parentUpdateStart) + " ms");
    }
    
    //            drawImageFromFolderBasedOnNumber();
    //            parent.update();
    }
    };
    private final String RIGHT = "Right";
    private Action right = new AbstractAction(RIGHT) {
    @Override
    public void actionPerformed(ActionEvent e) {
    if (Main.debugMode) {
    System.out.println("NEXT IMAGE");
    }
    long currentTimeMillis = System.currentTimeMillis();
    long parentUpdateStart = 0;
    long parentUpdateEnd = 0;
    
    //long currentTimeMillisTags = System.currentTimeMillis();
    Gui.saveTags();
    //System.out.println("Time to saveTags()  was: " + (System.currentTimeMillis() - currentTimeMillisTags) + " ms");
    try {
    //long currentTimeMillisSQL = System.currentTimeMillis();
    if (current < ImageSQLHandler.getTableSize() - 1) {
    ImageSQLHandler.getImageQuery().next();
    //System.out.println("Time to getImageQuery.next()  was: " + (System.currentTimeMillis() - currentTimeMillisSQL) + " ms");
    
    current++;
    parentUpdateStart = System.currentTimeMillis();
    parent.update();
    //parent.getInfoWindow().suggestions();
    parentUpdateEnd = System.currentTimeMillis();
    
    } else {
    }
    } catch (SQLException ex) {
    }
    if (Main.debugMode) {
    System.out.println("Total Time to Next image was: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
    System.out.println("-> Parent Update took: " + (parentUpdateEnd - parentUpdateStart) + " ms");
    }
    
    }
    };
    private final String OPEN = "Open";
    private Action open = new AbstractAction(OPEN) {
    @Override
    public void actionPerformed(ActionEvent e) {
    Gui.saveTags();
    int backup = current;
    String showInputDialog = JOptionPane.showInputDialog("Image Number: ");
    try {
    current = Integer.parseInt(showInputDialog);
    ImageSQLHandler.getImageQuery().absolute(current + 1);
    } catch (Exception ex) {
    System.out.println("Could not parse image number.");
    current = backup;
    }
    // drawImageFromFolderBasedOnNumber();
    parent.update();
    }
    };
    private final String LINK = "Link";
    private Action link = new AbstractAction(LINK) {
    @Override
    public void actionPerformed(ActionEvent e) {
    try {
    Runtime.getRuntime().exec("explorer.exe /select," + imageFile.getPath());
    } catch (IOException ex) {
    Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    };
    
        public void setKeybinds() {
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
        getActionMap().put(LEFT, left);
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
        getActionMap().put(RIGHT, right);
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), OPEN);
        getActionMap().put(OPEN, open);
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), LINK);
        getActionMap().put(LINK, link);
    }
     */
//</editor-fold>
    public ImageViewer() {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        String[] args = {""};
        NativeLog log = new NativeLog(LibVlc.INSTANCE, LibVlc.INSTANCE.libvlc_new(0, args));
        log.setLevel(libvlc_log_level_e.ERROR);
        this.size = ImageSQLHandler.imageCount();
        initComponents();
        //this.add(jPanel1);
        imgp = new ImagePanel();
        //mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
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
            if (imageFile.getName().trim().endsWith(".webm") || imageFile.getName().trim().endsWith(".mp4")) {
                //System.out.println("draw mpc");
                if (mediaPlayerComponent != null) {
                    //mediaPlayerComponent.getMediaPlayer().release();
                    mediaPlayerComponent.getMediaPlayer().mute(true);
                    mediaPlayerComponent.getMediaPlayer().stop();
                    mediaPlayerComponent.getMediaPlayer().enableOverlay(false);
                    mediaPlayerComponent.setVisible(false);
                    this.remove(mediaPlayerComponent);
                }
                if (mediaPlayerComponent == null) {
                    mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
                }
                this.add(mediaPlayerComponent);
                imgp.setVisible(false);
                this.remove(imgp);
                Media m = new SimpleMedia(imageFile.getPath(), "");
                mediaPlayerComponent.getMediaPlayer().attachVideoSurface();
                mediaPlayerComponent.getMediaPlayer().mute(false);
                mediaPlayerComponent.getMediaPlayer().setRepeat(true);
                mediaPlayerComponent.getMediaPlayer().enableOverlay(true);
                mediaPlayerComponent.getMediaPlayer().playMedia(m);
                mediaPlayerComponent.setVisible(true);
                this.repaint();

                this.setTitle("IMG: " + imageFile.getName() + " [" + current + " / " + (ImageSQLHandler.getTableSize()) + "]");
            } else {
                // System.out.println("draw mpc Else");
                if (mediaPlayerComponent != null) {
                    mediaPlayerComponent.getMediaPlayer().mute(true);
                    mediaPlayerComponent.getMediaPlayer().stop();
                    mediaPlayerComponent.getMediaPlayer().enableOverlay(false);
                    mediaPlayerComponent.setVisible(false);
                    this.remove(mediaPlayerComponent);
                }
                //System.out.println("draw image");
                if (!imgp.isVisible()) {
                    imgp.setVisible(true);
                    this.add(imgp);
                }
                startPanelDraw = System.currentTimeMillis();
                imgp.draw(imageFile);
                endPanelDraw = System.currentTimeMillis();

                try {
                    Main.tf.tlp.selectImage(current);
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
            //setKeybinds();
        }
        if (Main.debugMode) {
            System.out.println("Drawing image took total of: " + (System.currentTimeMillis() - startDraw) + " ms");
            System.out.println("-> Panel Drawing took: " + (endPanelDraw - startPanelDraw) + " ms");
            System.out.println("-> Repaint took: " + (endRepaint - startRepaint) + " ms");
        }
    }

    public void drawSpecific(int i) {
        try {
            ImageSQLHandler.getImageQuery().absolute(i + 1);
            System.out.println("drawing index: " + i);
            //parent.update();
        } catch (SQLException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void drawImageFromFolderBasedOnNumber() {
//        this.jPanel1.add(mediaPlayerComponent);
//        try {
//            imageFile = null;
//            imageFile = new File(ImageSQLHandler.getImageQuery().getString("path"));
//            draw();
//        } catch (Exception ex) {
//            System.out.println(ex);
//            if (current < 0) {
//                current = 0;
//                try {
//                    ImageSQLHandler.getImageQuery().first();
//
//                } catch (SQLException ex1) {
//                    Logger.getLogger(ImageViewer.class
//                            .getName()).log(Level.SEVERE, null, ex1);
//                }
//            } else if (current >= (size)) {
//                current = (size);
//                try {
//                    ImageSQLHandler.getImageQuery().last();
//
//                } catch (SQLException ex1) {
//                    Logger.getLogger(ImageViewer.class
//                            .getName()).log(Level.SEVERE, null, ex1);
//                }
//            }
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(100, 100));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
