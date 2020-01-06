/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import DataStructures.ImageWrapper;
import IO.ImageSQLHandler;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Peter
 */
public class ThumbListPanel extends javax.swing.JPanel {

    /**
     * Creates new form ThumbListPanel
     */
    ArrayList<ImageWrapper> iconList = new ArrayList<>();
    DefaultListModel model;
    ImageViewer parent;
    ThumbForm thumbForm;
    HashMap<String, ImageWrapper> map = new HashMap<>();

    //private File imageFile;
    public ThumbListPanel(ImageViewer iv, ThumbForm thumb) {
        parent = iv;
        thumbForm = thumb;
        initComponents();
        this.jScrollPane1.getVerticalScrollBar().setUnitIncrement(100 / 3);
        jList1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    Gui.saveTags();
                    int index = list.locationToIndex(evt.getPoint());
                    parent.current = jList1.getSelectedIndex();
                    //SQLHandler.setQueryPosition(jList1.getSelectedIndex()+1);
                    parent.drawSpecific(index);
                    //thumb.ttf.setSelectedAmount();
                }
                thumb.ttf.reload();
            }

        });
    }

    public ArrayList<ImageWrapper> returnSelected() {
        int[] selectedIndices = jList1.getSelectedIndices();
        ArrayList<ImageWrapper> selected = new ArrayList<>();
        for (int i : selectedIndices) {
            selected.add((ImageWrapper) model.get(i));
        }
        return selected;
    }
    public void addImageToModel(ImageWrapper imageFile) {
        if (!iconList.contains(imageFile)) {
            iconList.add(imageFile);
            map.put(imageFile.getThumbPath(), imageFile);
        }
    }

    public void reloadListDataFromArray() {
        model.removeAllElements();
        ImageSQLHandler.processImageNamesToList();
        ArrayList<String> imageNamesList = ImageSQLHandler.getImageNamesList();
        for (String name : imageNamesList) {
            String renamed = Utils.Utils.thumbRename(name);
            int lastIndexOf = renamed.lastIndexOf("\\");
            StringBuilder sb = new StringBuilder(renamed);
            sb.insert(lastIndexOf + 1, "thumbs\\");
            model.addElement(iconList.get(iconList.indexOf(map.get(sb.toString()))));
        }
    }

    public void selectImage(int file) {
        ListSelectionModel selectionModel = this.jList1.getSelectionModel();
        selectionModel.setSelectionInterval(file, file);
        this.jList1.ensureIndexIsVisible(jList1.getSelectedIndex());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        model = new DefaultListModel();
        jList1 = new javax.swing.JList<>(model);

        jList1.setCellRenderer(new ImageListCellRenderer());
        jList1.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jList1.setSelectionBackground(new java.awt.Color(255, 0, 153));
        jList1.setSelectionForeground(new java.awt.Color(255, 0, 102));
        jList1.setValueIsAdjusting(true);
        jList1.setVisibleRowCount(0);
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        parent.current = jList1.getSelectedIndex();
    }//GEN-LAST:event_jList1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<JLabel> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

class ImageListCellRenderer extends DefaultListCellRenderer {

    private int size;
    private ImageWrapper wrapper;
    BufferedImage icon;

    ImageListCellRenderer() {
        this(100);
    }

    ImageListCellRenderer(int size) {
        this.size = size;
        icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (c instanceof JLabel && value instanceof ImageWrapper) {
            wrapper = (ImageWrapper) value;
            try {
                icon = ImageIO.read(new File(wrapper.getThumbPath()));
            } catch (Exception ex) {
                //Logger.getLogger(ImageListCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("No Thumbnail found");
            }
            JLabel l = (JLabel) c;
            l.setText("");
            //BufferedImage i = (BufferedImage) icon;
            ImageIcon ii = new ImageIcon(icon);
            // ii.setDescription(i.toString());
            //l.setIcon(ii);

            Graphics2D g = icon.createGraphics();
            //g.setColor(new Color(0, 0, 0, 0));
            //g.clearRect(0, 0, size, size);
            g.drawImage(icon, 0, 0, size, size, this);

            g.dispose();
            l.setIcon(ii);
        }
        return c;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }
}
