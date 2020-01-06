package Gui;

import Main.*;
import Algorithms.ImageProcessing;
import DataStructures.ImageWrapper;
import IO.ImageSQLHandler;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter
 */
public class Gui extends javax.swing.JFrame {

    /**
     * Creates new form Gui
     */
    static ImageViewer img;
    static InfoWindow ip;
    static ThumbForm tf;
    Point Imagelocation;
    Dimension Imagesize;
    Point Infolocation;
    Dimension Infosize;
    static ArrayList<String> tagSlot1 = new ArrayList<>();
    static ArrayList<String> tagSlot2 = new ArrayList<>();
    static ArrayList<String> tagSlot3 = new ArrayList<>();

    public Gui() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        init();

    }

    public void init() {
        /* Create and display the form */
        initComponents();
        //img = new ImageViewer(this);

        ip = new InfoWindow(this);
        img.setVisible(true);
        ip.setVisible(true);

        this.jDesktopPane1.add(img);
        this.jDesktopPane1.add(ip);

        //img.recreateThumbFiles(false);
        //SQLHandler.getImages();
        pack();
        loadLayout();
    }

    public static void update() {
        long setImageStart = 0;
        long setImageEnd = 0;
        long drawStart = 0;
        long drawEnd = 0;
        try {
            setImageStart = System.currentTimeMillis();
            ip.setImage(img.current);
            setImageEnd = System.currentTimeMillis();

            drawStart = System.currentTimeMillis();
            img.drawImageFromFolderBasedOnNumber();
            drawEnd = System.currentTimeMillis();

        } catch (Exception ex) {
            System.out.println("Info window not visible or something went really wrong. (1)" + ex);
        }
        if (Main.debugMode) {
            System.out.println("Parent update: ");
            System.out.println("-> setImage took: " + (setImageEnd - setImageStart) + " ms");
            System.out.println("-> img.drawImageFromFolderBasedOnNumber: " + (drawEnd - drawStart) + " ms");
        }
    }

    public ImageViewer getImagePanel() {
        return img;
    }
    
    public InfoWindow getInfoWindow(){
        return ip;
    }

    public ThumbForm getThumbImageForm() {
        return tf;
    }

    public static void saveTags() {
        try {
            ip.saveTags();
        } catch (Exception ex) {
            System.out.println("Info window not visible or something went really wrong.(SaveTags)" + ex);
        }

    }

    public void saveLayout() {
        FileWriter fw = null;
        Imagelocation = img.getLocation();
        Imagesize = img.getSize();
        Infolocation = ip.getLocation();
        Infosize = ip.getSize();
        try {
            File file = new File("Data/Layout.txt");
            fw = new FileWriter(file);
            fw.write(Imagelocation.x + "," + Imagelocation.y + "\n");
            fw.write(Imagesize.width + "," + Imagesize.height + "\n");

            fw.write(Infolocation.x + "," + Infolocation.y + "\n");
            fw.write(Infosize.width + "," + Infosize.height + "\n");

        } catch (IOException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadLayout() {
        try {
            File file = new File("Data/Layout.txt");
            Scanner sc = new Scanner(file);
            String[] nextLine = sc.nextLine().split(",");
            Imagelocation = new Point(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1]));
            nextLine = sc.nextLine().split(",");
            Imagesize = new Dimension(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1]));
            nextLine = sc.nextLine().split(",");
            Infolocation = new Point(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1]));
            nextLine = sc.nextLine().split(",");
            Infosize = new Dimension(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1]));
            sc.close();

            img.setLocation(Imagelocation);
            img.setSize(Imagesize);

            ip.setLocation(Infolocation);
            ip.setSize(Infosize);
        } catch (IOException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetImageViewer() {
//        Point location = img.getLocation();
//        Dimension size = img.getSize();
        ImageSQLHandler.setQueryPosition(1);
//        img.dispose();
//        img.setVisible(false);
//        img = new ImageViewer(this);
//        img.setVisible(true);
//        this.jDesktopPane1.add(img);
//        img.setLocation(location);
//        img.setSize(size);
        update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem11 = new javax.swing.JMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        thumbListButton = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        tagMenu = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        copySlot1 = new javax.swing.JMenuItem();
        copySlot2 = new javax.swing.JMenuItem();
        copySlot3 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        pasteSlot1 = new javax.swing.JMenuItem();
        pasteSlot2 = new javax.swing.JMenuItem();
        pasteSlot3 = new javax.swing.JMenuItem();
        resetTagSlots = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();

        jMenuItem11.setText("jMenuItem11");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jDesktopPane1.setPreferredSize(new java.awt.Dimension(1280, 800));

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Save");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setText("Rescan");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("loadAll");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem16.setText("Reload Histogram Data");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem16);

        jMenuItem14.setText("Java GarbageCollector Call");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem14);

        jMenuItem18.setText("Reset");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem18);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Open");

        jMenuItem2.setText("Filters");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem5.setText("Viewer");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("TagUtility");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("TagStatistics");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        thumbListButton.setText("Thumbnails List");
        thumbListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thumbListButtonActionPerformed(evt);
            }
        });
        jMenu2.add(thumbListButton);

        jMenuItem15.setText("test openIMAJ");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jMenuBar1.add(jMenu2);

        tagMenu.setText("Tags");

        jMenuItem10.setText("Reload Relations");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        tagMenu.add(jMenuItem10);

        jMenu3.setText("Copy to Slot");

        copySlot1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        copySlot1.setText("Slot 1");
        copySlot1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copySlot1ActionPerformed(evt);
            }
        });
        jMenu3.add(copySlot1);

        copySlot2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        copySlot2.setText("Slot 2");
        copySlot2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copySlot2ActionPerformed(evt);
            }
        });
        jMenu3.add(copySlot2);

        copySlot3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        copySlot3.setText("Slot 3");
        copySlot3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copySlot3ActionPerformed(evt);
            }
        });
        jMenu3.add(copySlot3);

        tagMenu.add(jMenu3);

        jMenu4.setText("Paste from Slot");

        pasteSlot1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pasteSlot1.setText("Slot 1");
        pasteSlot1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteSlot1ActionPerformed(evt);
            }
        });
        jMenu4.add(pasteSlot1);

        pasteSlot2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pasteSlot2.setText("Slot 2");
        pasteSlot2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteSlot2ActionPerformed(evt);
            }
        });
        jMenu4.add(pasteSlot2);

        pasteSlot3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pasteSlot3.setText("Slot 3");
        pasteSlot3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteSlot3ActionPerformed(evt);
            }
        });
        jMenu4.add(pasteSlot3);

        tagMenu.add(jMenu4);

        resetTagSlots.setText("Reset All Slots");
        resetTagSlots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetTagSlotsActionPerformed(evt);
            }
        });
        tagMenu.add(resetTagSlots);

        jMenuBar1.add(tagMenu);

        jMenu5.setText("Layout");

        jMenuItem8.setText("Save Layout");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem8);

        jMenuItem9.setText("Load Layout");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("ImageViewer");

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setText("Next Image");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem12);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem13.setText("Previous Image");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem13);

        jMenuItem17.setText("Similar Images");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem17);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
//        Data.SaveAllXml();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FilterWindow filterWindow = new FilterWindow(this);
        filterWindow.setVisible(true);
        this.jDesktopPane1.add(filterWindow);
        //img.setKeybinds();
        try {
            filterWindow.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
//        IO.rescanImages();
//        Data.SaveAllXml();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
//        IO.loadAllImagesAgain();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        this.img.setVisible(true);
        this.ip.setVisible(true);
        this.jDesktopPane1.add(img);
        this.jDesktopPane1.add(ip);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            TagUtility tag = new TagUtility();
            tag.setVisible(true);
            this.jDesktopPane1.add(tag);
            this.jDesktopPane1.setSelectedFrame(tag);
            tag.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        TagStatistics tag = new TagStatistics(this);
        tag.setVisible(true);
        this.jDesktopPane1.add(tag);
        try {
            tag.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }
       }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void pasteSlot2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteSlot2ActionPerformed
        for (String t : tagSlot2) {
            ip.image.addTag(t);
        }
        System.out.println("Pasted from slot 3");
        update();
    }//GEN-LAST:event_pasteSlot2ActionPerformed

    private void copySlot2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copySlot2ActionPerformed
        tagSlot2.clear();
        tagSlot2.addAll(ip.image.returnTags());
        System.out.println("Saved to slot 2");
        copySlot2.setFont(copySlot2.getFont().deriveFont(Font.BOLD));
        pasteSlot2.setFont(copySlot2.getFont().deriveFont(Font.BOLD));
        update();
    }//GEN-LAST:event_copySlot2ActionPerformed

    private void copySlot1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copySlot1ActionPerformed
        tagSlot1.clear();
        tagSlot1.addAll(ip.image.returnTags());
        System.out.println("Saved to slot 1");
        copySlot1.setFont(copySlot1.getFont().deriveFont(Font.BOLD));
        pasteSlot1.setFont(copySlot1.getFont().deriveFont(Font.BOLD));
        update();
    }//GEN-LAST:event_copySlot1ActionPerformed

    private void copySlot3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copySlot3ActionPerformed
        tagSlot3.clear();
        tagSlot3.addAll(ip.image.returnTags());
        System.out.println("Saved to slot 3");
        copySlot3.setFont(copySlot3.getFont().deriveFont(Font.BOLD));
        pasteSlot3.setFont(copySlot3.getFont().deriveFont(Font.BOLD));
        update();
    }//GEN-LAST:event_copySlot3ActionPerformed

    private void pasteSlot1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteSlot1ActionPerformed
        for (String t : tagSlot1) {
            ip.image.addTag(t);
        }
        System.out.println("Pasted from slot 1");
        update();
    }//GEN-LAST:event_pasteSlot1ActionPerformed

    private void pasteSlot3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteSlot3ActionPerformed
        for (String t : tagSlot3) {
            ip.image.addTag(t);
        }
        System.out.println("Pasted from slot 3");
        update();
    }//GEN-LAST:event_pasteSlot3ActionPerformed

    private void resetTagSlotsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetTagSlotsActionPerformed
        tagSlot1.clear();
        tagSlot2.clear();
        tagSlot3.clear();
        copySlot1.setFont(copySlot1.getFont().deriveFont(Font.PLAIN));
        copySlot2.setFont(copySlot1.getFont().deriveFont(Font.PLAIN));
        copySlot3.setFont(copySlot1.getFont().deriveFont(Font.PLAIN));
        pasteSlot1.setFont(copySlot1.getFont().deriveFont(Font.PLAIN));
        pasteSlot2.setFont(copySlot1.getFont().deriveFont(Font.PLAIN));
        pasteSlot3.setFont(copySlot1.getFont().deriveFont(Font.PLAIN));
    }//GEN-LAST:event_resetTagSlotsActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        saveLayout();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        loadLayout();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        ImageSQLHandler.RedoAllTagTags();
        System.out.println("Updated tags and their relations");
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void thumbListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thumbListButtonActionPerformed
        tf = new ThumbForm(img);
        img.initThumbList();
        tf.setVisible(true);
    }//GEN-LAST:event_thumbListButtonActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        saveTags();
        try {
            if (!ImageSQLHandler.getImageQuery().isLast()) {
                ImageSQLHandler.getImageQuery().next();
                img.next();
                img.drawImageFromFolderBasedOnNumber();
                update();
            } else {
            }
        } catch (SQLException ex) {
        }

    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        saveTags();
        try {
            if (!ImageSQLHandler.getImageQuery().isFirst()) {
                ImageSQLHandler.getImageQuery().previous();
                img.prev();
                img.drawImageFromFolderBasedOnNumber();
                update();
            } else {
            }
        } catch (SQLException ex) {
        }

    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        System.gc();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        try {
            MBFImage image = ImageUtilities.readMBF(img.imageFile);
            ImageWrapper imageWrapper = new ImageWrapper(img.imageFile.getPath(), null);
            MultidimensionalHistogram histogram1 = Main.getImageProcessing().getHistogram(new ImageWrapper(img.imageFile.getPath(), null));

            ImageSQLHandler.addHistogramDatatoImage(imageWrapper);

            MultidimensionalHistogram histogramDataFromImage = ImageSQLHandler.getHistogramDataFromImage(imageWrapper);

            if (histogramDataFromImage.compare(histogram1, DoubleFVComparison.EUCLIDEAN) == 0.0) {
                System.out.println("histograms were same");
                System.out.println(histogramDataFromImage.compare(histogram1, DoubleFVComparison.EUCLIDEAN));
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        ImageSQLHandler.reAddHistogramsToAllImages();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        ImageWrapper imageWrapper = new ImageWrapper(img.imageFile.getPath(), null);
        //ImageSQLHandler.getHistogrammticallySimilarImagesToResultSet(imageWrapper);

        resetImageViewer();
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        this.remove(img);
        this.remove(ip);
        this.init();
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copySlot1;
    private javax.swing.JMenuItem copySlot2;
    private javax.swing.JMenuItem copySlot3;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem pasteSlot1;
    private javax.swing.JMenuItem pasteSlot2;
    private javax.swing.JMenuItem pasteSlot3;
    private javax.swing.JMenuItem resetTagSlots;
    private javax.swing.JMenu tagMenu;
    private javax.swing.JMenuItem thumbListButton;
    // End of variables declaration//GEN-END:variables

    public static ArrayList<String> getTagSlot1() {
        return tagSlot1;
    }

    public static void setTagSlot1(ArrayList<String> tagSlot1) {
        Gui.tagSlot1 = tagSlot1;
    }

    public static ArrayList<String> getTagSlot2() {
        return tagSlot2;
    }

    public static void setTagSlot2(ArrayList<String> tagSlot2) {
        Gui.tagSlot2 = tagSlot2;
    }

    public static ArrayList<String> getTagSlot3() {
        return tagSlot3;
    }

    public static void setTagSlot3(ArrayList<String> tagSlot3) {
        Gui.tagSlot3 = tagSlot3;
    }

    public JMenuItem getCopySlot1() {
        return copySlot1;
    }

    public void setCopySlot1(JMenuItem copySlot1) {
        this.copySlot1 = copySlot1;
    }

    public JMenuItem getCopySlot2() {
        return copySlot2;
    }

    public void setCopySlot2(JMenuItem copySlot2) {
        this.copySlot2 = copySlot2;
    }

    public JMenuItem getCopySlot3() {
        return copySlot3;
    }

    public void setCopySlot3(JMenuItem copySlot3) {
        this.copySlot3 = copySlot3;
    }

    public static ImageViewer getImageViewer() {
        return img;
    }

    public static InfoWindow getInfoPanel() {
        return ip;
    }

}
