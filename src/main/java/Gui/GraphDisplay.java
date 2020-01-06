package Gui;

import Data.*;
import Enum.CardType;
import Enum.Server;
import Util.CustomMouseObserver;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JFrame;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class GraphDisplay extends JFrame {

    List<Card> personCardList = Deck.returnPersonCards();
    List<DataConnection> ConnectionList = Deck.returnConnections();
    private Graph graph;
    private Viewer view;
    private View panel;
    private ViewPanel vp;

    public void GraphDisplay(MainWindow p) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new MultiGraph("Graph", false, false);
        graph.addAttribute("ui.quality", 4);
        graph.addAttribute("layout.force", 0.8);
        graph.addAttribute("layout.stabilization-limit", 0.8);
        graph.addAttribute("ui.antialias", true);
        graph.addAttribute("ui.stylesheet", "url('Data/Stylesheet/generalStylesheet.css')");
        update();
        view = graph.display();
        vp = view.getDefaultView();
        panel = view.getView("defaultView");
        view.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        view.enableAutoLayout();
        panel.getCamera().setAutoFitView(true);
        new CustomMouseObserver(vp);
        System.out.println(panel.getCamera().getViewPercent());

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("TEST");
                    moveTo(Deck.personFromName("Miah Hecarrah").getId());
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    System.out.println("Z");
                    System.out.println(panel.getCamera().getViewPercent());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
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
            }
        });
    }

    private void goOver() {
        double most = 0;
        for (Node n : graph.getNodeSet()) {
            if (n.getDegree() > most && n.getAttribute("ui.class").equals("character")) {
                most = n.getDegree();
            }
        }
        for (Node n : graph.getNodeSet()) {
            double size = 5 * Math.log((n.getDegree() / most) * 20);
            graph.getNode(n.getId()).addAttribute("ui.size", size);
        }
    }

    private void update() {
        generateNodes();
        generateConnections();
        generateFCNodes();
        generateFCConnections();
        generateServerNodes();
        generateServerConnections();
        generateGroupNodes();
        generateGroupConnections();
        goOver();
    }

    public void moveTo(String s) {
        GraphicNode n = view.getGraphicGraph().getNode(s);
        double nx = n.getX();
        double ny = n.getY();
        double nz = n.getZ();
        panel.getCamera().setViewCenter(nx, ny, nz);
    }

    public void resetView() {
        graph.clear();
        update();
    }

    private void generateNodes() {
        for (Card p : Deck.returnPersonCards()) {
            graph.addNode(p.getId()).addAttribute("ui.label", p.getName());
            graph.getNode(p.getId()).setAttribute("ui.class", "character");
            graph.getNode(p.getId()).addAttribute("layout.weight", 1);
        }
    }

    private void generateFCNodes() {
        for (Card p : Deck.returnFreeCompanies()) {
            graph.addNode(p.getId()).addAttribute("ui.label", p.getName());
            graph.getNode(p.getId()).setAttribute("ui.class", "fc");
            graph.getNode(p.getId()).setAttribute("layout.weight", 3);
        }
    }

    private void generateGroupNodes() {
        for (Card p : Deck.returnGroups()) {
            graph.addNode(p.getId()).addAttribute("ui.label", p.getName());
            graph.getNode(p.getId()).setAttribute("ui.class", "group");
            graph.getNode(p.getId()).setAttribute("layout.weight", 2);
        }
    }

    private void generateServerNodes() {
        for (Server p : Deck.returnServers()) {
            graph.addNode(p.toString()).addAttribute("ui.label", p);
            graph.getNode(p.toString()).setAttribute("ui.class", "server");
            graph.getNode(p.toString()).addAttribute("layout.weight", 4);
        }
    }

    private void generateServerConnections() {
        for (Server p : Deck.returnServers()) {
            for (CardInterface fc : Deck.returnFreeCompanies()) {
                if (fc.getMembers().get(0).getServer().equals(p)) {
                    graph.addEdge(p.toString() + "_" + fc.getId(), p.toString(), fc.getId(), false).addAttribute("layout.weight", 2);
                    graph.getEdge(p.toString() + "_" + fc.getId()).addAttribute("ui.class", "serverEdge");
                }
            }
        }
    }

    private void generateFCConnections() {
        for (CardInterface fc : Deck.returnFreeCompanies()) {
            for (CardInterface p : fc.getMembers()) {
                graph.addEdge(fc.getId() + "_" + p.getId(), fc.getId(), p.getId(), false).addAttribute("layout.weight", 0.5);
                graph.getEdge(fc.getId() + "_" + p.getId()).addAttribute("ui.class", "fcEdge");
            }
        }
    }

    private void generateGroupConnections() {
        for (CardInterface g : Deck.returnGroups()) {
            for (DataConnection p : g.getConnections()) {
                try {
                    String a = p.getA().getId();
                    String b = p.getB().getId();
                    
                    if(p.getA().getType().equals(CardType.FreeCompany)){
                       a = (Deck.fcFromName(p.getA().getName()).getId());
                    }
                    if(p.getB().getType().equals(CardType.FreeCompany)){
                       b = (Deck.fcFromName(p.getA().getName()).getId());
                    }
                    graph.addEdge("G" + a + "_" + b, a, b).addAttribute("layout.weight", 0.5);
                    graph.getEdge("G" + a + "_" + b).addAttribute("ui.class", "groupEdge");
                    graph.getEdge("G" + a + "_" + b).addAttribute("layout.weight", "0.75");
                } catch (Exception ex) {
                    System.out.println("nullPointer at: "+p.getA().getName()+", "+p.getA().getId()+"->" +p.getB().getName()+", "+p.getB().getId());
                }
            }
        }
    }

    private void generateConnections() {
        for (DataConnection c : Deck.returnConnections()) {
            try {
                if (c != null) {
                    graph.addEdge(c.getA().getId() + c.getB().getId(), c.getA().getId(), c.getB().getId(), c.isDirected());
                    graph.getEdge(c.getA().getId() + c.getB().getId()).addAttribute("ui.label", c.getLabel());
                    graph.getEdge(c.getA().getId() + c.getB().getId()).addAttribute("layout.weight", 0.1);
                    graph.getEdge(c.getA().getId() + c.getB().getId()).addAttribute("ui.class", c.getLabel());
                    if (c.isDirected()) {
                        graph.getEdge(c.getA().getId() + c.getB().getId()).addAttribute("ui.class", "directed");
                    }
                } else {
                    System.out.println(c.toString() + "Invalid Connection");
                }
            } catch (NullPointerException ex) {
                System.out.println("Something went null where ID: " + c.getId()
                        + "\n A: " + c.getA().toString()
                        + "\n B: " + c.getB().toString()
                        + "\n Label: " + c.getLabel());
            }
        }
    }
}
