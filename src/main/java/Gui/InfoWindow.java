package Gui;

import DataStructures.ImageWrapper;
import IO.ImageSQLHandler;
import Algorithms.TagAlgorithms;
import Main.Main;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class InfoWindow extends javax.swing.JInternalFrame {

    private final String LEFT = "Left";
    private Action left = new AbstractAction(LEFT) {
        @Override
        public void actionPerformed(ActionEvent e) {
            long currentTimeMillis = System.currentTimeMillis();
            Gui.saveTags();
            parent.getImagePanel().prev();
            //parent.getImagePanel().drawImageFromFolderBasedOnNumber();
            parent.update();
            suggestions();
            System.out.println("Time to next image was: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");

        }
    };
    private final String RIGHT = "Right";
    private Action right = new AbstractAction(RIGHT) {
        @Override
        public void actionPerformed(ActionEvent e) {
            long currentTimeMillis = System.currentTimeMillis();

            Gui.saveTags();
            parent.getImagePanel().next();
            //parent.getImagePanel().drawImageFromFolderBasedOnNumber();
            parent.update();
            suggestions();
            System.out.println("Time to next image was: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");

        }
    };

    Path pathPath = Paths.get("G:/Library/E/Single Images/Sorting Batches");
    File rootFolder = new File(pathPath.toUri());
    ImageWrapper image;
    Gui parent;

    public InfoWindow(Gui parent) {
        initComponents();
        this.parent = parent;
        loadComboBox();
        this.tagListModel.clear();
        this.setTitle("info");
        setKeybinds();
        suggestions();

    }

    public ImageWrapper getImage() {
        return image;
    }

    public void setKeybinds() {
        this.suggestionList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String toString = suggestionList.getSelectedValue().trim().split(",")[0];
                    tagListModel.addElement(toString);

                    if (model.getIndexOf(toString.trim()) != -1) {
                        model.addElement(toString);
                        implications(toString);
                    }
                    saveTags();
                    suggestions();
                }
            }
        });

        this.suggestionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String toString = suggestionList.getSelectedValue().trim().split(",")[0];
                    tagListModel.addElement(toString);
                    if (model.getIndexOf(toString.trim()) != -1) {
                        model.addElement(toString);
                        implications(toString);
                    }
                    saveTags();
                    suggestions();
                }
            }
        });

    }

    public void suggestions() {
        this.suggestionListModel.clear();
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
                this.suggestionListModel.addElement(entry.getKey() + ", " + entry.getValue().toString().subSequence(0, 3));
            }
        }
}

private void implications(String t) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<String> implications = ImageSQLHandler.returnImplicationsForTag(t);
        if (implications == null || implications.isEmpty()) {
            return;
        }
        for (String s : implications) {
            if (!tagListModel.contains(s)) {
                tagListModel.addElement(s);
                implications(s);
            }
        }
        if (Main.debugMode) {
            System.out.println("Calculating implications took: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        }
    }

    private void loadComboBox() {
        try {
            model.removeAllElements();
            model.addElement("");
            ImageSQLHandler.getTags();
            ImageSQLHandler.getTagQuery().beforeFirst();
            while (ImageSQLHandler.getTagQuery().next()) {
                model.addElement(ImageSQLHandler.getTagQuery().getString("name"));
            

}
        } catch (SQLException ex) {
            Logger.getLogger(InfoWindow.class
.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setImage(int i) {
        this.tagListModel.clear();
        this.suggestionListModel.clear();
        long edgeChecksStart = 0;
        long edgeChecksEnd = 0;
        long wrapperAndSizeStart = 0;
        long wrapperAndSizeEnd = 0;
        long suggestionsStart = 0;
        long suggestionsEnd = 0;
        long loadTagsEnd = 0;
        long loadTagsStart = 0;
        int tableSize = ImageSQLHandler.getTableSize();

        try {
            edgeChecksStart = System.currentTimeMillis();
            if (i < 0) {
                ImageSQLHandler.getImageQuery().next();
            }
            if (i > tableSize) {
                ImageSQLHandler.getImageQuery().previous();
            }
            edgeChecksEnd = System.currentTimeMillis();
            wrapperAndSizeStart = System.currentTimeMillis();
            wrapperAndSizeEnd = System.currentTimeMillis();

            this.setTitle("INFO: [" + i + " / " + (tableSize) + "]");
            this.image = new ImageWrapper(ImageSQLHandler.getImageQuery().getString("path"), null);
        

} catch (SQLException ex) {
            Logger.getLogger(InfoWindow.class
.getName()).log(Level.SEVERE, null, ex);
        }
        loadTagsStart = System.currentTimeMillis();
        loadTags();
        loadTagsEnd = System.currentTimeMillis();

        suggestionsStart = System.currentTimeMillis();
        suggestions();
        suggestionsEnd = System.currentTimeMillis();

        if (Main.debugMode) {
            System.out.println("Set Image: ");
            System.out.println("-> Edge Check took: " + (edgeChecksEnd - edgeChecksStart) + " ms");
            System.out.println("-> Making wrapper and checking size took: " + (wrapperAndSizeEnd - wrapperAndSizeStart) + " ms");
            System.out.println("-> loading tags took: " + (loadTagsEnd - loadTagsStart) + " ms");
            System.out.println("-> Suggestions took " + (suggestionsEnd - suggestionsStart) + "ms");
        }

    }

    private void loadTags() { //Very Fast operation
        // long currentTimeMillis = System.currentTimeMillis();
        this.tagListModel.clear();
        for (String t : image.returnTags()) {
            this.tagListModel.addElement(t);
        }
        //System.out.println("loading tags took "+(System.currentTimeMillis()-currentTimeMillis)+"ms");
    }

    public void saveTags() {
        Enumeration<String> elements = tagListModel.elements();
        PreparedStatement[] addImageTagAddToBatch = null;

        while (elements.hasMoreElements()) {
            String nextElement = elements.nextElement();
            image.addTag(nextElement);
            addImageTagAddToBatch = ImageSQLHandler.addImageTagAddToBatch(image, nextElement);
            //SQLHandler.addImageTagExecuteBatch(addImageTagAddToBatch);
            //SQLHandler.addTag(nextElement);
        }
        ImageSQLHandler.addImageTagExecuteBatch(addImageTagAddToBatch);
        elements = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        selectionComboBox = new javax.swing.JComboBox<>(model);
        Suggestions = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tagBox = new javax.swing.JList<>(tagListModel);
        jScrollPane3 = new javax.swing.JScrollPane();
        suggestionList = new javax.swing.JList<>(suggestionListModel);
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        reloadSuggestions = new javax.swing.JMenuItem();

        setClosable(true);
        setResizable(true);

        jButton1.setText("Remove");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        selectionComboBox.setEditable(true);
        AutoCompleteDecorator.decorate(selectionComboBox);
        selectionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionComboBoxActionPerformed(evt);
            }
        });

        Suggestions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Suggestions.setText("Suggestions");

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane1.setViewportView(tagBox);

        jSplitPane1.setBottomComponent(jScrollPane1);

        jScrollPane3.setViewportView(suggestionList);

        jSplitPane1.setLeftComponent(jScrollPane3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(selectionComboBox, 0, 334, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(Suggestions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(selectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Suggestions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(15, 15, 15))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tags");

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Save");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Reset ImageViewer");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        reloadSuggestions.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        reloadSuggestions.setText("Reload Suggestions");
        reloadSuggestions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadSuggestionsActionPerformed(evt);
            }
        });
        jMenu1.add(reloadSuggestions);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionComboBoxActionPerformed
        String toString = selectionComboBox.getSelectedItem().toString().trim();
        //System.out.println(evt.getActionCommand());
        if ("comboBoxEdited".equals(evt.getActionCommand()) && !toString.equals("")) {
            tagListModel.addElement(toString);
            if (model.getIndexOf(toString.trim()) == -1) {
                model.addElement(toString);
                saveTags();
                //implications(toString);
                loadComboBox();
            }
            implications(toString);
            saveTags();
            suggestions();
        } else if ("comboBoxChanged".equals(evt.getActionCommand())) {
        }


    }//GEN-LAST:event_selectionComboBoxActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        saveTags();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.out.println("remove");
        int selectedIndex = tagBox.getSelectedIndex();
        String tag = tagListModel.remove(selectedIndex);
        image.removeTag(tag);
        ImageSQLHandler.removeImageTag(image, tag);
        ImageSQLHandler.removeOrphnaedTags();
        saveTags();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        parent.resetImageViewer();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void reloadSuggestionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadSuggestionsActionPerformed
        suggestions();
    }//GEN-LAST:event_reloadSuggestionsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Suggestions;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem reloadSuggestions;
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    private javax.swing.JComboBox<String> selectionComboBox;
    DefaultListModel<String> suggestionListModel = new DefaultListModel<>();
    private javax.swing.JList<String> suggestionList;
    DefaultListModel<String> tagListModel = new DefaultListModel<>();
    private javax.swing.JList<String> tagBox;
    // End of variables declaration//GEN-END:variables
}
