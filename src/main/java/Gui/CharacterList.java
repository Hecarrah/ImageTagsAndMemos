/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import Data.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Peter
 */
public class CharacterList extends javax.swing.JInternalFrame {

    private HashMap<String, Card> map = new HashMap<>();
    MainWindow parent;

    /**
     * Creates new form CharacterList
     */
    public CharacterList(MainWindow p) {
        initComponents();
        parent = p;
        update();
        characterList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    p.setCharacterCreation(map.get(characterList.getSelectedValue()));
                    dispose();
                }
            }
        });
        characterList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    p.setCharacterCreation(map.get(characterList.getSelectedValue()));
                    dispose();
                }
            }
        });
    }

    public CharacterList(MainWindow p, List<Card> list) {
        initComponents();
        parent = p;
        results(list);
        characterList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    p.setCharacterCreation(map.get(characterList.getSelectedValue()));
                    dispose();
                }
            }
        });
        characterList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    p.setCharacterCreation(map.get(characterList.getSelectedValue()));
                    dispose();
                }
            }
        });
        p.getDesktop().addContainerListener(new ContainerListener() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                System.out.println("Closed!");
                update();
            }

            @Override
            public void componentAdded(ContainerEvent e) {
                System.out.println("Closed!");
                update();
            }
        });
    }

    public void update() {
        characterList.removeAll();
        List<String> stringList = new ArrayList<String>();
        List<Card> cardList = Deck.returnPersonCards();
        for (Card c : cardList) {
            map.put(c.toString(), c);
            stringList.add(c.toString());
        }
        String[] strarray = stringList.toArray(new String[0]);
        characterList.setListData(strarray);
    }

    public void results(List<Card> list) {
        characterList.removeAll();
        List<String> stringList = new ArrayList<String>();
        List<Card> cardList = list;
        for (Card c : cardList) {
            map.put(c.toString(), c);
            stringList.add(c.toString());
        }
        String[] strarray = stringList.toArray(new String[0]);
        characterList.setListData(strarray);
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
        characterList = new javax.swing.JList<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        refreshButton = new javax.swing.JMenuItem();
        searchButton = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Character List");

        characterList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        characterList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                characterListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(characterList);

        jMenu1.setText("Menu");

        refreshButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jMenu1.add(refreshButton);

        searchButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jMenu1.add(searchButton);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void characterListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_characterListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_characterListMouseClicked

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        update();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        SearchBox sb = new SearchBox(parent);
        sb.setVisible(true);
        parent.getDesktop().add(sb);
        parent.select(sb);
        parent.disposable(sb);

    }//GEN-LAST:event_searchButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> characterList;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem refreshButton;
    private javax.swing.JMenuItem searchButton;
    // End of variables declaration//GEN-END:variables
}