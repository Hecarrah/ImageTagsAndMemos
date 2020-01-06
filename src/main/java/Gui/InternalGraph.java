/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import Data.*;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;


/**
 *
 * @author Peter
 */
public class InternalGraph extends javax.swing.JInternalFrame {

    private static Graph graph;

    List<Card> personCardList = Deck.returnPersonCards();
    List<DataConnection> ConnectionList = Deck.returnConnections();

    /**
     * Creates new form InternalGraph
     */
    public InternalGraph() {
        initComponents();
        graph();
    }

    private void graph() {
        setLayout(new BorderLayout());
        JPanel jpanel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        jpanel.setPreferredSize(new Dimension(200, 200));
        jpanel.setBorder(BorderFactory.createLineBorder(Color.black, 5));
        graph = new MultiGraph("Graph", false, false);
        generateNodes();
        generateConnections();
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

        ViewPanel viewPanel = viewer.addDefaultView(false);
        jpanel.add(viewPanel);
        add(jpanel);
        pack();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                viewer.close();
                System.out.println("closing graph");
            }
        });
    }

    private void generateNodes(){
        for(Card p : Deck.returnPersonCards()){
            graph.addNode(p.toString()).addAttribute("ui.label", p.getName());
        }
    }
    private void generateConnections(){
        for(DataConnection c : Deck.returnConnections()){
            graph.addEdge(c.getA().getName()+c.getB().getName(), c.getA().toString(), c.getB().toString()).addAttribute("ui.label", c.getLabel());
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

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
