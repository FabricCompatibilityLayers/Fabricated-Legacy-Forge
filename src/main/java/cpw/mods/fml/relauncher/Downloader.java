/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.relauncher;

import cpw.mods.fml.common.FMLLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Downloader extends JOptionPane implements IDownloadDisplay {
    private JDialog container;
    private JLabel currentActivity;
    private JProgressBar progress;
    boolean stopIt;
    Thread pokeThread;

    public Downloader() {
    }

    private Box makeProgressPanel() {
        Box box = Box.createVerticalBox();
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel welcomeLabel = new JLabel("<html><b><font size='+1'>FML is setting up your minecraft environment</font></b></html>");
        box.add(welcomeLabel);
        welcomeLabel.setAlignmentY(0.0F);
        welcomeLabel = new JLabel("<html>Please wait, FML has some tasks to do before you can play</html>");
        welcomeLabel.setAlignmentY(0.0F);
        box.add(welcomeLabel);
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        this.currentActivity = new JLabel("Currently doing ...");
        box.add(this.currentActivity);
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        this.progress = new JProgressBar(0, 100);
        this.progress.setStringPainted(true);
        box.add(this.progress);
        box.add(Box.createRigidArea(new Dimension(0, 30)));
        return box;
    }

    public JDialog makeDialog() {
        this.setMessageType(1);
        this.setMessage(this.makeProgressPanel());
        this.setOptions(new Object[]{"Stop"});
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getSource() == Downloader.this && evt.getPropertyName() == "value") {
                    Downloader.this.requestClose("This will stop minecraft from launching\nAre you sure you want to do this?");
                }

            }
        });
        this.container = new JDialog((Window) null, "Hello", Dialog.ModalityType.MODELESS);
        this.container.setResizable(false);
        this.container.setLocationRelativeTo((Component) null);
        this.container.add(this);
        this.updateUI();
        this.container.pack();
        this.container.setMinimumSize(this.container.getPreferredSize());
        this.container.setVisible(true);
        this.container.setDefaultCloseOperation(0);
        this.container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Downloader.this.requestClose("Closing this window will stop minecraft from launching\nAre you sure you wish to do this?");
            }
        });
        return this.container;
    }

    protected void requestClose(String message) {
        int shouldClose = JOptionPane.showConfirmDialog(this.container, message, "Are you sure you want to stop?", 0, 2);
        if (shouldClose == 0) {
            this.container.dispose();
        }

        this.stopIt = true;
        if (this.pokeThread != null) {
            this.pokeThread.interrupt();
        }

    }

    public void updateProgressString(String progressUpdate, Object... data) {
        FMLLog.finest(progressUpdate, data);
        if (this.currentActivity != null) {
            this.currentActivity.setText(String.format(progressUpdate, data));
        }

    }

    public void resetProgress(int sizeGuess) {
        if (this.progress != null) {
            this.progress.getModel().setRangeProperties(0, 0, 0, sizeGuess, false);
        }

    }

    public void updateProgress(int fullLength) {
        if (this.progress != null) {
            this.progress.getModel().setValue(fullLength);
        }

    }

    public void makeHeadless() {
        this.container = null;
        this.progress = null;
        this.currentActivity = null;
    }

    public void setPokeThread(Thread currentThread) {
        this.pokeThread = currentThread;
    }

    public boolean shouldStopIt() {
        return this.stopIt;
    }
}
