package bomberman.ui;

import bomberman.entities.GameBoard;
import bomberman.server.MainServer;
import bomberman.utils.Defaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SelectMap extends JPanel {
    private JButton btnCancel;
    private JButton btnStart;
    private JComboBox cbSelectedMap;
    private JComboBox cbSelectedWorld;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField txtPort;

    public SelectMap() {
        setLocation(100, 50);
        setSize(300, 170);
        initComponents();
    }

    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.cbSelectedMap = new JComboBox();
        this.jLabel2 = new JLabel();
        this.cbSelectedWorld = new JComboBox();
        this.btnStart = new JButton();
        this.btnCancel = new JButton();
        this.txtPort = new JTextField();
        this.jLabel3 = new JLabel();
        setBackground(new Color(173, 215, 255));
        setBorder(BorderFactory.createBevelBorder(0));
        setPreferredSize(new Dimension(0, 0));
        this.jLabel1.setFont(new Font("Arial", 1, 14));
        this.jLabel1.setText("Map Name: ");
        this.cbSelectedMap.setBackground(new Color(252, 251, 226));
        this.cbSelectedMap.setModel(new DefaultComboBoxModel<>(new String[]{"map1", "map2"}));

        this.jLabel2.setFont(new Font("Arial", 1, 14));
        this.jLabel2.setText("World Name: ");

        this.cbSelectedWorld.setBackground(new Color(252, 251, 226));
        this.cbSelectedWorld.setModel(new DefaultComboBoxModel<>(new String[]{"yard", "cave", "city"}));
        this.btnStart.setBackground(new Color(255, 145, 19));
        this.btnStart.setText("Start");
        this.btnStart.setActionCommand("jButton1");
        this.btnStart.addActionListener(SelectMap.this::btnStartActionPerformed);
        this.btnCancel.setBackground(new Color(255, 145, 19));
        this.btnCancel.setText("Cancel");
        this.btnCancel.setActionCommand("jButton1");
        this.btnCancel.addActionListener(SelectMap.this::btnCancelActionPerformed);
        this.txtPort.setText("4747");
        this.jLabel3.setFont(new Font("Arial", 1, 14));
        this.jLabel3.setText("Port Number:");
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        sequentialGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false));
        GroupLayout.SequentialGroup sequentialGroup1 = layout.createSequentialGroup();
        sequentialGroup1.addComponent(this.btnCancel);
        sequentialGroup1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, Defaults.MIN_COMPONENT_SIZE, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup1.addComponent(this.btnStart);
        sequentialGroup.addGroup(sequentialGroup1);
        GroupLayout.SequentialGroup sequentialGroup2 = layout.createSequentialGroup();
        parallelGroup.addGroup(sequentialGroup2);
        sequentialGroup2.addComponent(this.jLabel3);
        sequentialGroup2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, Defaults.MIN_COMPONENT_SIZE, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup2.addComponent(this.txtPort, -2, 39, -2);
        parallelGroup.addGroup(sequentialGroup);
        GroupLayout.SequentialGroup sequentialGroup3 = layout.createSequentialGroup();
        GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup1.addComponent(this.jLabel2);
        parallelGroup1.addComponent(this.jLabel1);
        GroupLayout.ParallelGroup parallelGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER, false);
        parallelGroup2.addComponent(this.cbSelectedMap, 0, Defaults.MIN_COMPONENT_SIZE, Defaults.MAX_COMPONENT_SIZE);
        parallelGroup2.addComponent(this.cbSelectedWorld, -2, 93, -2);
        parallelGroup1.addGroup(parallelGroup2);
        sequentialGroup3.addGroup(parallelGroup1);
        parallelGroup.addGroup(sequentialGroup3);
        layout.setHorizontalGroup(parallelGroup);

        parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        parallelGroup1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup1.addComponent(this.jLabel1, Defaults.MIN_COMPONENT_SIZE, 22, Defaults.MAX_COMPONENT_SIZE);
        parallelGroup1.addComponent(this.cbSelectedMap, -2, -1, -2);
        parallelGroup.addGap(15, 15, 15);
        parallelGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup2.addComponent(this.jLabel2, -2, 22, -2);
        parallelGroup2.addComponent(this.cbSelectedWorld, -2, -1, -2);
        parallelGroup.addGroup(parallelGroup2);
        GroupLayout.ParallelGroup parallelGroup3 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup3.addComponent(this.jLabel3, -2, 22, -2);
        parallelGroup3.addComponent(this.txtPort, -2, -1, -2);
        parallelGroup.addGroup(parallelGroup3);
        parallelGroup.addGap(18, 18, 18);
        GroupLayout.ParallelGroup parallelGroup4 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup4.addComponent(this.btnCancel);
        parallelGroup4.addComponent(this.btnStart);
        parallelGroup.addGroup(parallelGroup4);
        sequentialGroup.addGroup(parallelGroup);
        sequentialGroup.addContainerGap();
        parallelGroup.addGroup(sequentialGroup);
        layout.setVerticalGroup(parallelGroup);

        //layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(1, false).add(layout.createSequentialGroup().add(this.btnCancel).addPreferredGap(0, -1, 32767).add(this.btnStart)).add(layout.createSequentialGroup().add(this.jLabel3).addPreferredGap(0, -1, 32767).add(this.txtPort, -2, 39, -2)).add(layout.createSequentialGroup().add(layout.createParallelGroup(1).add(this.jLabel2).add(this.jLabel1)).addPreferredGap(0).add(layout.createParallelGroup(1, false).add(this.cbSelectedMap, 0, -1, 32767).add(this.cbSelectedWorld, -2, 93, -2)))).addContainerGap(-1, 32767)));
        //layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(3).add(this.jLabel1, -1, 22, 32767).add(this.cbSelectedMap, -2, -1, -2)).add(15, 15, 15).add(layout.createParallelGroup(3).add(this.jLabel2, -2, 22, -2).add(this.cbSelectedWorld, -2, -1, -2)).addPreferredGap(0).add(layout.createParallelGroup(3).add(this.jLabel3, -2, 22, -2).add(this.txtPort, -2, -1, -2)).add(18, 18, 18).add(layout.createParallelGroup(3).add(this.btnCancel).add(this.btnStart)).addContainerGap()));
    }

    private void btnStartActionPerformed(ActionEvent evt) {
        MainServer ms = new MainServer();
        ms.setSelectedMap(this.cbSelectedMap.getSelectedItem().toString());
        ms.setSelectedWorld(this.cbSelectedWorld.getSelectedItem().toString());
        GameBoard.getInstance().setIPAddress("localhost");
        GameBoard.getInstance().newGame(Integer.parseInt(this.txtPort.getText()));
        btnCancelActionPerformed(evt);
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        Component c = GameBoard.getInstance().getMainWindow();
        if ((c instanceof JFrame)) {
            JFrame jFrame = (JFrame) c;
            jFrame.getContentPane().remove(this);
        }
        if ((c instanceof JApplet)) {
            JApplet jApplet = (JApplet) c;
            jApplet.getContentPane().remove(this);
        }
        c.requestFocus();
        GameBoard.getInstance().getMainWindow().repaint();
    }
}