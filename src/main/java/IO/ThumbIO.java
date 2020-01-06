/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Gui.ImageViewer;
import Gui.ThumbListPanel;
import static Utils.Utils.thumbRename;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Peter
 */
public class ThumbIO {

    public static void createThumbFile(String filename, boolean replace) {
        int WIDTH = 100;
        int HEIGHT = 100;
        //SQLHandler.getImages();
        try {
            //String filename = imageQuery.getString("path");
            File originalFile = new File(filename);
            BufferedImage bi;
            String renamed = Utils.Utils.thumbRename(originalFile.getName());
            File thumbFile = new File(IO.getThumbFolder() + "/" + renamed);
            if (thumbFile.exists() && !replace) {
                return;
            }
            if (!originalFile.getName().endsWith(".webm") && !originalFile.getName().endsWith(".mp4")) {
                try {
                    BufferedImage read = ImageIO.read(originalFile);
                    bi = new BufferedImage(WIDTH, HEIGHT, 2);
                    Graphics2D g = bi.createGraphics();
                    g.drawImage(read, 0, 0, WIDTH, HEIGHT, null);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.dispose();
                    ImageIO.write(bi, "png", thumbFile);
                } catch (IOException ex) {
                    System.out.println("error on: " + originalFile.getName());
                    Logger.getLogger(ThumbListPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                BufferedImage read = ImageIO.read(ThumbIO.class.getClass().getResource("/noThumb.jpg"));
                bi = new BufferedImage(WIDTH, HEIGHT, 2);
                Graphics2D g = bi.createGraphics();
                g.drawImage(read, 0, 0, WIDTH, HEIGHT, null);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.dispose();
                ImageIO.write(bi, "png", thumbFile);
                try {
                    ImageIO.write(read, "png", thumbFile);
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ImageSQLHandler.addThumbnailPath(originalFile.toString(), thumbFile.toString());
        } catch (IOException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void recreateThumbFiles(boolean replace) {
        int WIDTH = 100;
        int HEIGHT = 100;
        ImageSQLHandler.getImages();
        ResultSet imageQuery = ImageSQLHandler.getImageQuery();
        try {
            imageQuery.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(ThumbIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (imageQuery.next()) {
                String filename = imageQuery.getString("path");
                File originalFile = new File(filename);
                BufferedImage bi;
                String renamed = thumbRename(originalFile.getName());
                File thumbFile = new File(IO.getThumbFolder() + "/" + renamed);
                if (thumbFile.exists() && !replace) {
                    if (imageQuery.getString("thumbPath") == null || imageQuery.getString("thumbPath").equals("")) {
                        ImageSQLHandler.addThumbnailPath(originalFile.toString(), thumbFile.toString());
                    }
                    continue;
                }
                if (!originalFile.getName().endsWith(".webm") && !originalFile.getName().endsWith(".mp4") && !originalFile.getName().endsWith(".gif")) {
                    try {
                        System.out.println(originalFile.getName());
                        BufferedImage read = ImageIO.read(originalFile);
                        bi = new BufferedImage(WIDTH, HEIGHT, 2);
                        Graphics2D g = bi.createGraphics();
                        g.drawImage(read, 0, 0, WIDTH, HEIGHT, null);
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g.dispose();
                        ImageIO.write(bi, "png", thumbFile);
                    } catch (IOException ex) {
                        System.out.println("error on: " + originalFile.getName());
                        // Logger.getLogger(ThumbListPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (originalFile.getName().endsWith(".gif")) {
                    BufferedImage read;
                    try {
                        System.out.println(originalFile.getName());
                        try {
                            read = ImageIO.read(originalFile);
                        } catch (Exception ex) {
                            read = ImageIO.read(ThumbIO.class.getClass().getResource("/noThumb.jpg"));
                        }
                        bi = new BufferedImage(WIDTH, HEIGHT, 2);
                        Graphics2D g = bi.createGraphics();
                        g.drawImage(read, 0, 0, WIDTH, HEIGHT, null);
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g.dispose();
                        ImageIO.write(bi, "png", thumbFile);
                    } catch (IOException ex) {
                        System.out.println("error on: " + originalFile.getName());
                        // Logger.getLogger(ThumbListPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //System.out.println(new File(ImageViewer.class.getClass().getResource("/noThumb.jpg").getFile()).getName());

                    BufferedImage read = ImageIO.read(ThumbIO.class.getClass().getResource("/noThumb.jpg"));

                    bi = new BufferedImage(WIDTH, HEIGHT, 2);
                    Graphics2D g = bi.createGraphics();
                    g.drawImage(read, 0, 0, WIDTH, HEIGHT, null);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.dispose();
                    ImageIO.write(bi, "png", thumbFile);
                    try {
                        ImageIO.write(read, "png", thumbFile);
                    } catch (IOException ex) {
                        System.out.println("error on: " + originalFile.getName());
                        //Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                ImageSQLHandler.addThumbnailPath(originalFile.toString(), thumbFile.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
