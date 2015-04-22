package bomberman.ui;

import bomberman.entities.GameBoard;
import bomberman.utils.Defaults;
import bomberman.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SelectPlayer extends JPanel {
    private static Image selectedImage;
    private static String selectedCharacter;
    private static String selectedName;
    private JButton btnCancel;
    private JButton btnStart;
    private JComboBox cbSelectedImage;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JTextField txtName;

    public SelectPlayer() {
        setLocation(100, 50);
        setSize(300, 150);
        initComponents();
        if (GameBoard.getInstance().getActivePlayer() != null) {
            this.txtName.setText(GameBoard.getInstance().getActivePlayer().getName());
        }
    }

    public static String getSelectedCharacter() {
        return selectedCharacter;
    }

    public static void setSelectedCharacter(String aSelectedCharacter) {
        selectedCharacter = aSelectedCharacter;
    }

    public static Image getSelectedImage() {
        return selectedImage;
    }

    public static void setSelectedImage(Image aSelectedImage) {
        selectedImage = aSelectedImage;
    }

    public static String getSelectedName() {
        return selectedName;
    }

    public static void setSelectedName(String aSelectedName) {
        selectedName = aSelectedName;
    }

    private void initComponents() {
        this.btnCancel = new JButton();
        this.btnStart = new JButton();
        this.jLabel2 = new JLabel();
        this.jLabel1 = new JLabel();
        this.cbSelectedImage = new JComboBox();
        this.txtName = new JTextField();

        setBackground(new Color(173, 215, 255));
        setBorder(BorderFactory.createBevelBorder(0));
        this.btnCancel.setBackground(new Color(255, 145, 19));
        this.btnCancel.setText("Cancel");
        this.btnCancel.setActionCommand("jButton1");
        this.btnCancel.addActionListener(SelectPlayer.this::btnCancelActionPerformed);
        this.btnStart.setBackground(new Color(255, 145, 19));
        this.btnStart.setText("Set");
        this.btnStart.setActionCommand("jButton1");
        this.btnStart.addActionListener(SelectPlayer.this::btnStartActionPerformed);
        this.jLabel2.setFont(new Font("Arial", 1, 14));
        this.jLabel2.setText("Name: ");

        this.jLabel1.setFont(new Font("Arial", 1, 14));
        this.jLabel1.setText("Character: ");

        this.cbSelectedImage.setBackground(new Color(252, 251, 226));
        this.cbSelectedImage.setModel(new DefaultComboBoxModel<>(new String[]{"barbarian", "bart", "bomberman", "bride", "businessman", "cecildk", "chara", "bobaFett", "JodoKast", "quiGon", "stormTrooper", "yoda", "alexCloth1", "alexCloth2", "armyblue1", "armyblue2", "blackSold1", "blueSold3", "blueSold4", "guard1", "darkknight", "EnergyBall", "floatrobot", "forestRanger", "FunkySquirrel", "ghost", "girl", "greenCape", "houseWifeOld", "Indy", "jodoKast", "kattBof2", "klarthTalesphantasia", "littleGirl", "maincharaHarvestmoon", "mysteryMan", "oddman", "oldmanCape", "oldman", "oxydAlexCloth", "oxydDude3", "oxydDude4", "oxyddude6", "oxydDude7", "oxydDude8", "oxydDude9", "oxydSoldier", "plantMonster", "quiGon", "ryuAdultBof2", "ryuKidBof2", "santa", "shadowMan", "shadowMan2", "stenBof2", "suzuTalesphantasia", "vincent"}));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER, false);
        sequentialGroup.addGroup(parallelGroup1);
        sequentialGroup.addGroup(layout.createSequentialGroup());
        sequentialGroup.addComponent(this.btnCancel);
        sequentialGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, Defaults.MIN_COMPONENT_SIZE, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup.addComponent(this.btnStart);
        parallelGroup.addGroup(sequentialGroup);
        GroupLayout.SequentialGroup sequentialGroup1 = layout.createSequentialGroup();
        GroupLayout.ParallelGroup parallelGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup2.addComponent(this.jLabel2);
        parallelGroup2.addComponent(this.jLabel1);
        sequentialGroup1.addGroup(parallelGroup2);
        sequentialGroup1.addGap(16, 16, 16);
        GroupLayout.ParallelGroup parallelGroup3 = layout.createParallelGroup(GroupLayout.Alignment.CENTER, false);
        parallelGroup3.addComponent(this.txtName);
        parallelGroup3.addComponent(this.cbSelectedImage, -2, 93, -2);
        sequentialGroup1.addGroup(parallelGroup3);
        parallelGroup.addGroup(sequentialGroup1);
        layout.setHorizontalGroup(parallelGroup);

        parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        parallelGroup1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup1.addComponent(this.jLabel2, -2, 22, -2);
        parallelGroup1.addComponent(this.txtName, -2, -1, -2);
        sequentialGroup.addGroup(parallelGroup1);
        sequentialGroup.addGap(18, 18, 18);
        parallelGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup2.addComponent(this.jLabel1, -2, 22, -2);
        parallelGroup2.addComponent(this.cbSelectedImage, -2, -1, -2);
        sequentialGroup.addGroup(parallelGroup2);
        sequentialGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 21, Defaults.MAX_COMPONENT_SIZE);
        parallelGroup3 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        parallelGroup3.addComponent(this.btnCancel);
        parallelGroup3.addComponent(this.btnStart);
        sequentialGroup.addGroup(parallelGroup3);
        parallelGroup.addGroup(sequentialGroup);
        layout.setVerticalGroup(parallelGroup);

        //layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(1, false).add(layout.createSequentialGroup().add(this.btnCancel).addPreferredGap(0, -1, 32767).add(this.btnStart)).add(layout.createSequentialGroup().add(layout.createParallelGroup(1).add(this.jLabel2).add(this.jLabel1)).add(16, 16, 16).add(layout.createParallelGroup(1, false).add(this.txtName).add(this.cbSelectedImage, -2, 93, -2)))).addContainerGap(-1, 32767)));
        //layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(3).add(this.jLabel2, -2, 22, -2).add(this.txtName, -2, -1, -2)).add(18, 18, 18).add(layout.createParallelGroup(3).add(this.jLabel1, -2, 22, -2).add(this.cbSelectedImage, -2, -1, -2)).addPreferredGap(0, 21, 32767).add(layout.createParallelGroup(3).add(this.btnCancel).add(this.btnStart)).addContainerGap()));
    }

    private void btnStartActionPerformed(ActionEvent evt) {
        setSelectedCharacter(this.cbSelectedImage.getSelectedItem().toString());
        setSelectedName(this.txtName.getText());
        if ((getSelectedCharacter() != null) && (getSelectedName() != null)) {
            SelectMenu.getCurrentSelectMenu().getBtnJoin().setEnabled(true);
            SelectMenu.getCurrentSelectMenu().getBtnHost().setEnabled(true);
        }
        Image img;
        if ((img = Utils.getInstance().getImage(this.cbSelectedImage.getSelectedItem().toString() + ".png")) == null) {
            Utils.getInstance().loadImage("characters/" + this.cbSelectedImage.getSelectedItem().toString() + ".png", this.cbSelectedImage.getSelectedItem().toString() + ".png");
            img = Utils.getInstance().getImage(this.cbSelectedImage.getSelectedItem().toString() + ".png");
        }
        setSelectedImage(img);


        GameBoard.getInstance().getMainWindow().requestFocusInWindow();
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
        GameBoard.getInstance().getMainWindow().repaint();
    }
}