package bomberman.ui;

import bomberman.utils.Defaults;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel implements Runnable {
    private boolean loading;
    private Component mainWindow;
    private JLabel lblStatus;

    public LoadingPanel(Component mainWindow) {
        this.mainWindow = mainWindow;
        setLocation(mainWindow.getWidth() / 4, mainWindow.getHeight() / 4);
        setSize(300, 100);
        setLoading(true);
        initComponents();
        Thread t = new Thread(this);
        t.start();
    }

    public Component getMainWindow() {
        return this.mainWindow;
    }

    public void setMainWindow(Component mainWindow) {
        this.mainWindow = mainWindow;
    }

    public boolean isLoading() {
        return this.loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    private void initComponents() {
        this.lblStatus = new JLabel();
        setBackground(Defaults.LABEL_COLOR);
        this.lblStatus.setFont(Defaults.FONT1);
        this.lblStatus.setText("  Loading: ");
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap();
        sequentialGroup.addComponent(this.lblStatus, Defaults.MIN_COMPONENT_SIZE, 312, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup.addContainerGap();
        GroupLayout.ParallelGroup paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addGroup(sequentialGroup);
        layout.setHorizontalGroup(paraGroup);

        sequentialGroup = layout.createSequentialGroup();
        sequentialGroup.addContainerGap(Defaults.MIN_COMPONENT_SIZE, Defaults.MAX_COMPONENT_SIZE);
        sequentialGroup.addComponent(this.lblStatus, -2, 88, -2);
        sequentialGroup.addContainerGap();
        paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addGroup(GroupLayout.Alignment.CENTER, sequentialGroup);
        layout.setVerticalGroup(paraGroup);

        //layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(this.lblStatus, -1, 312, 32767).addContainerGap()));
        //layout.setVerticalGroup(layout.createParallelGroup(1).add(2, layout.createSequentialGroup().addContainerGap(-1, 32767).add(this.lblStatus, -2, 88, -2).addContainerGap()));
    }

    public void run() {
        while (isLoading()) {
            this.lblStatus.setText(this.lblStatus.getText() + "*");
            if (this.lblStatus.getText().length() > 20) {
                this.lblStatus.setText("  Loading: ");
            }
            synchronized (this) {
                try {
                    updateUI();
                    getMainWindow().repaint();
                    wait(200L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}