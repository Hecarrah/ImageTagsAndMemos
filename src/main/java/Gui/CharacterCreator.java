package Gui;

import Data.*;
import Enum.*;
import java.awt.Desktop;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 *
 * @author Peter
 */
public class CharacterCreator extends javax.swing.JInternalFrame {

    private Card card;
    private MainWindow parent;

    private HashMap<String, Server> serverMap = new HashMap<>();
    private HashMap<String, Race> raceMap = new HashMap<>();
    private HashMap<String, Subrace> subraceMap = new HashMap<>();
    private HashMap<String, Sex> sexMap = new HashMap<>();
    private HashMap<String, Job> jobMap = new HashMap<>();
    private HashMap<String, DataConnection> connectionMap = new HashMap<>();
    private HashMap<String, DataConnection> fcConMap = new HashMap<>();
    private HashMap<String, Card> groupMap = new HashMap<>();

    public CharacterCreator(MainWindow p) {
        parent = p;
        initComponents();
        setUpFields();
        setUpListeners(p);
    }

    private void setUpListeners(MainWindow p) {
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
        connectionsList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (Deck.personFromName(connectionsList.getSelectedValue()) == null) {
                        p.setGroupList(groupMap.get(connectionsList.getSelectedValue()));
                    } else {
                        p.setConnectionCreator(connectionMap.get(connectionsList.getSelectedValue()), null);
                    }
                    dispose();
                }
            }
        });
        connectionsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (Deck.personFromName(connectionsList.getSelectedValue()) == null) {
                        p.setGroupList(groupMap.get(connectionsList.getSelectedValue()));
                    } else {
                        p.setConnectionCreator(connectionMap.get(connectionsList.getSelectedValue()), card);
                    }
                    dispose();
                }
            }
        });
        p.getDesktop().addContainerListener(new ContainerListener() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                System.out.println("Closed!");
                loadConnections();
            }

            @Override
            public void componentAdded(ContainerEvent e) {
            }
        });
    }

    public CharacterCreator(MainWindow p, Card c) {
        parent = p;
        this.card = c;
        initComponents();
        loadCard(c);
        setUpListeners(p);
    }

    private void setUpFields() {
        this.raceComboBox.removeAllItems();
        List<String> tempList = new ArrayList<>();
        //<editor-fold defaultstate="collapsed" desc="Race">
        tempList.clear();
        for (Race r : Race.values()) {
            raceMap.put(r.toString(), r);
            tempList.add(r.toString());
        }
        Collections.sort(tempList);
        tempList.forEach(a -> raceComboBox.addItem(a));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Sex">
        tempList.clear();
        this.sexComboBox.removeAllItems();
        for (Sex s : Sex.values()) {
            sexMap.put(s.toString(), s);
            tempList.add(s.toString());
        }
        Collections.sort(tempList);
        tempList.forEach(a -> sexComboBox.addItem(a));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Job">
        tempList.clear();
        this.jobComboBox.removeAllItems();
        for (Job j : Job.values()) {
            jobMap.put(j.toString(), j);
            tempList.add(j.toString());
        }
        Collections.sort(tempList);
        tempList.forEach(a -> jobComboBox.addItem(a));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Server">
        tempList.clear();
        this.serverComboBox.removeAllItems();
        for (Server s : Server.values()) {
            serverMap.put(s.toString(), s);
            tempList.add(s.toString());
        }
        Collections.sort(tempList);
        tempList.forEach(a -> serverComboBox.addItem(a));
        //</editor-fold>
    }

    private void loadCard(Card p) {
        setUpFields();
        this.serverComboBox.setSelectedItem(card.getServer().toString());
        this.nameField.setText(card.getName());
        this.raceComboBox.setSelectedItem(card.getRace().toString());
        this.subraceComboBox.setSelectedItem(card.getSubrace().toString());
        this.sexComboBox.setSelectedItem(card.getSex().toString());
        this.jobComboBox.setSelectedItem(card.getMainJob().toString());
        this.fcField.setText(card.getFc());
        this.lodestoneField.setText(card.getLodestone());
        this.idField.setText(card.getId());
        this.notesGeneral.setText(card.getNotes().get(0));
        this.notesPersonal.setText(card.getNotes().get(1));
        this.notesMisc.setText(card.getNotes().get(2));
        loadConnections();
    }

    public void loadConnections() {
        this.connectionsList.removeAll();
        List<String> stringList = new ArrayList<>();
        List<Card> groups = Deck.returnGroups();
        List<DataConnection> connectionList = Deck.returnConnections();
        System.out.println(connectionList.size());
        try {
            for (DataConnection c : connectionList) {
                if (c.getA() == null || c.getB() == null) {
                    return;
                }
                if (!c.isDirected() || c.isDirected() && c.getA().getId().equals(card.getId())) {

                    if (c.getA().getId().equals(card.getId())) {
                        if (!stringList.contains(c.getB().getName())) {
                            stringList.add(c.getB().getName());
                        }
                        this.connectionMap.put(c.getB().getName(), c);
                    }
                    if (c.getB().getId().equals(card.getId())) {
                        if (!stringList.contains(c.getA().getName())) {
                            stringList.add(c.getA().getName());
                        }
                        this.connectionMap.put(c.getB().getName(), c);
                    }
                }
            }
            for (Card g : groups) {
                if (g.getType().equals(CardType.Group)) {
                    for (DataConnection gdc : g.getConnections()) {
//                    gdc.getA().generateId();
//                    gdc.getB().generateId();
                        if (gdc.getA() != null && gdc.getB() != null) {
                            if (gdc.getA().getType().equals(CardType.Group) && gdc.getB().getId().equals(this.card.getId())) {
                                if (!stringList.contains(gdc.getA().getName())) {
                                    stringList.add(gdc.getA().getName());
                                    this.connectionMap.put(gdc.getA().getName(), gdc);
                                    this.groupMap.put(gdc.getA().getName(), g);
                                }
                            } else if (gdc.getB().getType().equals(CardType.Group) && gdc.getA().getId().equals(this.card.getId())) {
                                if (!stringList.contains(gdc.getB().getName())) {
                                    stringList.add(gdc.getB().getName());
                                    this.connectionMap.put(gdc.getB().getName(), gdc);
                                    this.groupMap.put(gdc.getB().getName(), g);
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("tried to load null connection" + ex);
        }
        String[] strarray = stringList.toArray(new String[0]);
        //System.out.println(Arrays.toString(strarray));

        this.connectionsList.setListData(strarray);
    }

    private Card saveCard() {
        Server server = serverMap.get(this.serverComboBox.getSelectedItem());
        String name = this.nameField.getText();
        Race race = raceMap.get(this.raceComboBox.getSelectedItem());
        Subrace subrace = subraceMap.get(this.subraceComboBox.getSelectedItem());
        Sex sex = sexMap.get(this.sexComboBox.getSelectedItem());
        Job job = jobMap.get(this.jobComboBox.getSelectedItem());
        String fc = this.fcField.getText();
        String lodestone = this.lodestoneField.getText();
        ArrayList<String> notes = new ArrayList<>();
        notes.clear();
        notes.add(this.notesGeneral.getText() + "");
        notes.add(this.notesPersonal.getText() + "");
        notes.add(this.notesMisc.getText() + "");
        Card newPerson;
        if (card != null) {
            newPerson = card;
            newPerson.setLodestone(lodestone);
            newPerson.setServer(server);
            newPerson.setName(name);
            newPerson.setRace(race);
            newPerson.setSubrace(subrace);
            newPerson.setSex(sex);
            newPerson.setMainJob(job);
            newPerson.setFc(fc);
            newPerson.setNotes(notes);
        } else {
            newPerson = new Card(lodestone, server, name, race, subrace, sex, job, fc, null);
        }
        if (Deck.returnPersonCards().contains(newPerson)) {
            int showConfirmDialog = JOptionPane.showConfirmDialog(parent, "Character already exists, continue?");
            System.out.println(showConfirmDialog);
            if (showConfirmDialog == 1) {
                return null;
            }
        }
        newPerson.setNotes(notes);
        newPerson.setType(CardType.Character);
        Deck.addPersonCard(newPerson);
        parent.setStatus("Saved Person: " + newPerson.getName());
        return newPerson;
    }

    private void deleteCard() {
        Card p = Deck.personFromId(this.idField.getText());
        Deck.removePersonCard(card);
    }

    public void loadValuesFromLodestone() {
        Element character;
        Elements elementName = null, elementServer = null, elementRace = null, elementFC = null;
        try {
            Document doc = Jsoup.connect(this.lodestoneField.getText()).get();
            character = doc.getElementById("character");
            elementName = character.getElementsByClass("frame__chara__name");
            elementServer = character.getElementsByClass("frame__chara__world");
            elementRace = character.getElementsByClass("character-block__name");
            elementFC = character.getElementsByClass("character__freecompany__name");
        } catch (Exception ex) {
            System.out.println("Malformed lodestone URL");
            return;
        }

        String raceGender = (elementRace.get(0).text());
        raceGender = raceGender.replace(" /", "");

        Race race = Race.HYUR;
        Subrace subrace = Subrace.HYUR_MID;
        Sex sex = Sex.MALE;
        for (Race r : Race.values()) {
            if (raceGender.contains(r.toString())) {
                race = r;
                break;
            }
        }
        for (Subrace r : Subrace.values()) {
            if (raceGender.contains(r.toString())) {
                subrace = r;
                break;
            }
        }
        if (raceGender.contains("♀")) {
            sex = (Sex.FEMALE);
        } else {
            sex = (Sex.MALE);
        }
        System.out.println(elementName.text());
        System.out.println(elementServer.text());
        System.out.println(race.toString());
        System.out.println(subrace.toString());
        System.out.println(sex.toString());
        System.out.println(elementFC.text().replaceAll("Free Company ", ""));
        this.serverComboBox.setSelectedItem(elementServer.text());
        this.nameField.setText(elementName.text());
        this.raceComboBox.setSelectedItem(race.toString());
        this.subraceComboBox.setSelectedItem(subrace.toString());
        this.sexComboBox.setSelectedItem(sex.toString());
        this.fcField.setText(elementFC.text().replaceAll("Free Company ", ""));
        this.jobComboBox.setSelectedItem(Job.UKN.toString());
        
        parent.setCharacterCreation(saveCard());
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fcField = new javax.swing.JTextField();
        raceComboBox = new javax.swing.JComboBox<>();
        subraceComboBox = new javax.swing.JComboBox<>();
        sexComboBox = new javax.swing.JComboBox<>();
        serverComboBox = new javax.swing.JComboBox<>();
        jobComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        lodestoneField = new javax.swing.JTextField();
        browseLodestoneButton = new javax.swing.JButton();
        loadFromLodestone = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesGeneral = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        notesPersonal = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        notesMisc = new javax.swing.JTextArea();
        connectionsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        connectionsList = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveButton = new javax.swing.JMenuItem();
        deleteButton = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        idField = new javax.swing.JMenu();

        jMenu2.setText("File");
        jMenuBar2.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar2.add(jMenu3);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Character Creator / Editor");
        setToolTipText("");
        setMinimumSize(new java.awt.Dimension(349, 191));

        jPanel1.setAutoscrolls(true);

        jLabel1.setText("Server:");

        jLabel2.setText("Name:");

        jLabel3.setText("Race: ");

        jLabel4.setText("Subrace: ");

        jLabel5.setText("Sex: ");

        jLabel6.setText("MainJob: ");

        jLabel7.setText("FC: ");

        fcField.setAutoscrolls(false);

        raceComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        raceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                raceComboBoxActionPerformed(evt);
            }
        });

        subraceComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        sexComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        serverComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jobComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Lodestone:");

        browseLodestoneButton.setText("Open");
        browseLodestoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseLodestoneButtonActionPerformed(evt);
            }
        });

        loadFromLodestone.setText("Load");
        loadFromLodestone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFromLodestoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sexComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serverComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameField)
                    .addComponent(raceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subraceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lodestoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(browseLodestoneButton)
                        .addGap(0, 0, 0)
                        .addComponent(loadFromLodestone))
                    .addComponent(fcField)
                    .addComponent(jobComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lodestoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseLodestoneButton)
                    .addComponent(loadFromLodestone))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(serverComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(raceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(subraceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(sexComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jobComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fcField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Information", jPanel1);

        notesGeneral.setColumns(20);
        notesGeneral.setRows(5);
        jScrollPane1.setViewportView(notesGeneral);

        jTabbedPane2.addTab("General", jScrollPane1);

        notesPersonal.setColumns(20);
        notesPersonal.setRows(5);
        jScrollPane2.setViewportView(notesPersonal);

        jTabbedPane2.addTab("Personal", jScrollPane2);

        notesMisc.setColumns(20);
        notesMisc.setRows(5);
        jScrollPane3.setViewportView(notesMisc);

        jTabbedPane2.addTab("Miscellanious", jScrollPane3);

        jTabbedPane1.addTab("Notes", jTabbedPane2);

        connectionsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(connectionsList);

        jButton1.setText("Add new connection");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectionsPanelLayout = new javax.swing.GroupLayout(connectionsPanel);
        connectionsPanel.setLayout(connectionsPanelLayout);
        connectionsPanelLayout.setHorizontalGroup(
            connectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        );
        connectionsPanelLayout.setVerticalGroup(
            connectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectionsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton1))
        );

        jTabbedPane1.addTab("Connections", connectionsPanel);

        jMenu1.setText("File");

        saveButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jMenu1.add(saveButton);

        deleteButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jMenu1.add(deleteButton);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("Load from Lodestone");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("                                                       ID: ");
        jMenu4.setContentAreaFilled(false);
        jMenu4.setEnabled(false);
        jMenu4.setFocusable(false);
        jMenu4.setRequestFocusEnabled(false);
        jMenu4.setRolloverEnabled(false);
        jMenu4.setVerifyInputWhenFocusTarget(false);
        jMenuBar1.add(jMenu4);

        idField.setEnabled(false);
        idField.setFocusable(false);
        idField.setRequestFocusEnabled(false);
        idField.setRolloverEnabled(false);
        idField.setVerifyInputWhenFocusTarget(false);
        jMenuBar1.add(idField);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browseLodestoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseLodestoneButtonActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(this.lodestoneField.getText()));

        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(CharacterCreator.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_browseLodestoneButtonActionPerformed

    private void raceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_raceComboBoxActionPerformed
        if (raceComboBox.getSelectedIndex() != -1) {
            this.subraceComboBox.removeAllItems();
            if (raceComboBox.getSelectedItem().toString().equals(Race.AURA.toString())) {
                subraceMap.put(Subrace.AURA_RAEN.toString(), Subrace.AURA_RAEN);
                subraceMap.put(Subrace.AURA_XAE.toString(), Subrace.AURA_XAE);
                this.subraceComboBox.addItem(Subrace.AURA_RAEN.toString());
                this.subraceComboBox.addItem(Subrace.AURA_XAE.toString());
            }
            if (raceComboBox.getSelectedItem().toString().equals(Race.ELEZ.toString())) {
                subraceMap.put(Subrace.ELEZ_DUSK.toString(), Subrace.ELEZ_DUSK);
                subraceMap.put(Subrace.ELEZ_WILD.toString(), Subrace.ELEZ_WILD);
                this.subraceComboBox.addItem(Subrace.ELEZ_DUSK.toString());
                this.subraceComboBox.addItem(Subrace.ELEZ_WILD.toString());
            }
            if (raceComboBox.getSelectedItem().toString().equals(Race.HYUR.toString())) {
                subraceMap.put(Subrace.HYUR_HIGH.toString(), Subrace.HYUR_HIGH);
                subraceMap.put(Subrace.HYUR_MID.toString(), Subrace.HYUR_MID);
                this.subraceComboBox.addItem(Subrace.HYUR_MID.toString());
                this.subraceComboBox.addItem(Subrace.HYUR_HIGH.toString());
            }
            if (raceComboBox.getSelectedItem().toString().equals(Race.LALA.toString())) {
                subraceMap.put(Subrace.LALA_DUNE.toString(), Subrace.LALA_DUNE);
                subraceMap.put(Subrace.LALA_PLAIN.toString(), Subrace.LALA_PLAIN);
                this.subraceComboBox.addItem(Subrace.LALA_DUNE.toString());
                this.subraceComboBox.addItem(Subrace.LALA_PLAIN.toString());
            }
            if (raceComboBox.getSelectedItem().toString().equals(Race.MIQO.toString())) {
                subraceMap.put(Subrace.MIQO_MOON.toString(), Subrace.MIQO_MOON);
                subraceMap.put(Subrace.MIQO_SUN.toString(), Subrace.MIQO_SUN);
                this.subraceComboBox.addItem(Subrace.MIQO_MOON.toString());
                this.subraceComboBox.addItem(Subrace.MIQO_SUN.toString());
            }
            if (raceComboBox.getSelectedItem().toString().equals(Race.ROEG.toString())) {
                subraceMap.put(Subrace.ROEG_HELL.toString(), Subrace.ROEG_HELL);
                subraceMap.put(Subrace.ROEG_SEA.toString(), Subrace.ROEG_SEA);
                this.subraceComboBox.addItem(Subrace.ROEG_HELL.toString());
                this.subraceComboBox.addItem(Subrace.ROEG_SEA.toString());
            }
        }
    }//GEN-LAST:event_raceComboBoxActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        this.deleteCard();
        this.dispose();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        this.saveCard();
        //this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        parent.setConnectionCreator(null, card);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        loadValuesFromLodestone();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void loadFromLodestoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFromLodestoneActionPerformed
        loadValuesFromLodestone();
    }//GEN-LAST:event_loadFromLodestoneActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseLodestoneButton;
    private javax.swing.JList<String> connectionsList;
    private javax.swing.JPanel connectionsPanel;
    private javax.swing.JMenuItem deleteButton;
    private javax.swing.JTextField fcField;
    private javax.swing.JMenu idField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JComboBox<String> jobComboBox;
    private javax.swing.JButton loadFromLodestone;
    private javax.swing.JTextField lodestoneField;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextArea notesGeneral;
    private javax.swing.JTextArea notesMisc;
    private javax.swing.JTextArea notesPersonal;
    private javax.swing.JComboBox<String> raceComboBox;
    private javax.swing.JMenuItem saveButton;
    private javax.swing.JComboBox<String> serverComboBox;
    private javax.swing.JComboBox<String> sexComboBox;
    private javax.swing.JComboBox<String> subraceComboBox;
    // End of variables declaration//GEN-END:variables
}
