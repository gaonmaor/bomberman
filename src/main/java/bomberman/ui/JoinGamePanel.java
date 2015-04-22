package bomberman.ui;

import bomberman.entities.GameBoard;
import bomberman.server.RequestHandler;
import bomberman.utils.Defaults;
import bomberman.utils.Utils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JoinGamePanel
        extends JPanel {
    private JButton btnCancel;
    private JButton btnStart;
    private JLabel lblServerIP;
    private JLabel lblPortNumber;
    private JTextField txtHost;
    private JTextField txtPort;

    public JoinGamePanel() {
        setLocation(100, 50);
        setSize(300, 150);
        initComponents();
    }

    private void initComponents() {
        this.txtHost = new JTextField();
        this.lblServerIP = new JLabel();
        this.btnCancel = new JButton();
        this.btnStart = new JButton();
        this.lblPortNumber = new JLabel();
        this.txtPort = new JTextField();
        setBackground(Defaults.POP_WINDOW_COLOR);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.lblServerIP.setText("Server ip:");
        this.lblServerIP.setFont(Defaults.FONT2);
        this.btnCancel.setText("Cancel");
        this.btnCancel.setBackground(Defaults.LABEL_COLOR);
        this.btnCancel.addActionListener(JoinGamePanel.this::btnCancelActionPerformed);
        //this.btnCancel.setActionCommand("jButton1");
        this.btnStart.setText("Join");
        this.btnStart.setBackground(Defaults.LABEL_COLOR);
        this.btnStart.addActionListener(JoinGamePanel.this::btnStartActionPerformed);
        //this.btnStart.setActionCommand("jButton1");
        this.lblPortNumber.setText("Port Number:");
        this.lblPortNumber.setFont(Defaults.FONT2);
        this.txtPort.setText(Utils.getInstance().getProperty(JoinGamePanel.class, "defaultPort"));
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        /*
        GroupLayout.SequentialGroup sequentialGroup2 = layout.createSequentialGroup();
        sequentialGroup2.addComponent(this.btnCancel);
        sequentialGroup2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 52, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup2.addComponent(this.btnStart);
        GroupLayout.SequentialGroup sequentialGroup3 = layout.createSequentialGroup();
        sequentialGroup3.addComponent(this.lblServerIP);
        sequentialGroup3.addPreferredGap(LayoutStyle.ComponentPlacement.INDENT);
        sequentialGroup3.addComponent(this.txtHost, Defaults.MIN_COMPONENT_SIZE, 95, Defaults.MAX_COMPONENT_SIZE);
        GroupLayout.SequentialGroup sequentialGroup4 = layout.createSequentialGroup();
        sequentialGroup4.addComponent(this.lblPortNumber);
        sequentialGroup4.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup4.addComponent(this.txtPort, -2, 39, -2);
        sequentialGroup3.addGroup(sequentialGroup4);
        sequentialGroup2.addGroup(sequentialGroup3);
        GroupLayout.ParallelGroup paraGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup2.addGroup(GroupLayout.Alignment.CENTER, sequentialGroup2);
        GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        sequentialGroup.addGroup(paraGroup2);
        sequentialGroup.addContainerGap();
        GroupLayout.ParallelGroup paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addGroup(sequentialGroup);
        layout.setHorizontalGroup(paraGroup);

        paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        paraGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup2.addComponent(this.lblServerIP, -2, 22, -2);
        paraGroup2.addComponent(this.txtHost, -2, 19, -2);
        sequentialGroup.addGroup(paraGroup2);
        sequentialGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        GroupLayout.ParallelGroup paraGroup3 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup3.addComponent(this.lblPortNumber, -2, 22, -2);
        paraGroup3.addComponent(this.txtPort, -2, -1, -2);
        sequentialGroup.addGroup(paraGroup3);
        sequentialGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767);
        GroupLayout.ParallelGroup paraGroup4 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup4.addComponent(this.btnStart);
        paraGroup4.addComponent(this.btnCancel);
        sequentialGroup.addGroup(paraGroup4).addGap(20, 20, 20);
        paraGroup.addGroup(sequentialGroup);
        layout.setVerticalGroup(paraGroup);
        */

        GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        GroupLayout.SequentialGroup sequentialGroup1 = layout.createSequentialGroup();
        sequentialGroup1.addComponent(this.btnCancel);
        sequentialGroup1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 52, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup1.addComponent(this.btnStart);
        parallelGroup1.addGroup(GroupLayout.Alignment.CENTER, sequentialGroup1);
        GroupLayout.SequentialGroup sequentialGroup2 = layout.createSequentialGroup();
        sequentialGroup2.addComponent(this.lblServerIP);
        sequentialGroup2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        sequentialGroup2.addComponent(this.txtHost, Defaults.MIN_COMPONENT_SIZE, 95, Defaults.MAX_COMPONENT_SIZE);
        parallelGroup1.addGroup(sequentialGroup2);
        GroupLayout.SequentialGroup sequentialGroup3 = layout.createSequentialGroup();
        sequentialGroup3.addComponent(this.lblPortNumber);
        sequentialGroup3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup3.addComponent(this.txtPort, -2, 39, -2);
        parallelGroup1.addGroup(sequentialGroup3);
        sequentialGroup.addGroup(parallelGroup);
        sequentialGroup.addContainerGap();
        parallelGroup.addGroup(sequentialGroup);
        layout.setHorizontalGroup(parallelGroup);

        parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        parallelGroup1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        parallelGroup1.addComponent(this.lblServerIP, -2, 22, -2);
        parallelGroup1.addComponent(this.txtHost, -2, 19, -2);
        sequentialGroup.addGroup(parallelGroup1);
        sequentialGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        GroupLayout.ParallelGroup parallelGroup2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        parallelGroup2.addComponent(this.lblPortNumber, -2, 22, -2);
        parallelGroup2.addComponent(this.txtPort, -2, -1, -2);
        sequentialGroup.addGroup(parallelGroup2);
        sequentialGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, Defaults.MIN_COMPONENT_SIZE, Defaults.MAX_COMPONENT_SIZE);
        GroupLayout.ParallelGroup parallelGroup3 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        parallelGroup3.addComponent(this.btnStart);
        parallelGroup3.addComponent(this.btnCancel);
        sequentialGroup.addGroup(parallelGroup3);
        sequentialGroup.addGap(20, 20, 20);
        parallelGroup.addGroup(sequentialGroup);
        layout.setVerticalGroup(parallelGroup);

        //layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().add(this.btnCancel).addPreferredGap(0, 52, 32767).add(this.btnStart)).add(layout.createSequentialGroup().add(this.jLabel3).addPreferredGap(0).add(this.txtHost, -1, 95, 32767)).add(layout.createSequentialGroup().add(this.jLabel4).addPreferredGap(0, 37, 32767).add(this.txtPort, -2, 39, -2))).addContainerGap()));
        //layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(3).add(this.jLabel3, -2, 22, -2).add(this.txtHost, -2, 19, -2)).addPreferredGap(0).add(layout.createParallelGroup(3).add(this.jLabel4, -2, 22, -2).add(this.txtPort, -2, -1, -2)).addPreferredGap(0, -1, 32767).add(layout.createParallelGroup(3).add(this.btnStart).add(this.btnCancel)).add(20, 20, 20)));
    }

    private void btnStartActionPerformed(ActionEvent evt) {
        RequestHandler cReq = new RequestHandler(this.txtHost.getText());
        GameBoard.getInstance().setSelectedWorld(cReq.getSelectedWorld());
        GameBoard.getInstance().setWorldRes(GameBoard.getInstance().loadWorldRes());
        GameBoard.getInstance().setIPAddress(this.txtHost.getText());
        GameBoard.getInstance().newGame(Integer.parseInt(this.txtPort.getText()));
        btnCancelActionPerformed(evt);
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        Component component = GameBoard.getInstance().getMainWindow();
        if ((component instanceof JFrame)) {
            JFrame jFrame = (JFrame) component;
            jFrame.getContentPane().remove(this);
        }
        if ((component instanceof JApplet)) {
            JApplet jApplet = (JApplet) component;
            jApplet.getContentPane().remove(this);
        }
        component.requestFocus();
        GameBoard.getInstance().getMainWindow().repaint();
    }
}