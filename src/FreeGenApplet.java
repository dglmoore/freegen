/*
 * Copyright 2014 Douglas Moore. All rights reserved.
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FreeGenApplet extends JApplet implements GeneratorApp {
    private JPanel content;
    private HashMap<String,Synthesizer> synths;
    private KeyGen idGenerator;

    private JLabel message;

    public void addSynth(SynthType type) {
        String id = idGenerator.nextID();
        synths.put(id, new Synthesizer<FreeGenApplet>(this, id, type));
        content.add(synths.get(id).getPanel());
        content.revalidate();
        repaint();
    }

    public void removeSynth(String id) {
        if (synths.containsKey(id)) {
            content.remove(synths.get(id).getPanel());
            synths.remove(id);
            content.revalidate();
            repaint();
        }
    }

    public void init() {
        content = new JPanel();
        synths = new HashMap<String,Synthesizer>();
        idGenerator = new KeyGen();

        JMenu menuNew = new JMenu("New");
        menuNew.setMnemonic('N');

        JMenuItem itemPure = new JMenuItem("Pure Tone");
        itemPure.setMnemonic('T');
        itemPure.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSynth(SynthType.PURE);
            }
        }
        );
        menuNew.add(itemPure);

        JMenuItem itemPanning = new JMenuItem("Panning");
        itemPanning.setMnemonic('P');
        itemPanning.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSynth(SynthType.PANNING);
            }
        }
        );
        menuNew.add(itemPanning);

        JMenu menuControl = new JMenu("Control");
        menuControl.setMnemonic('C');

        JMenuItem playAll = new JMenuItem("Play All");
        playAll.setMnemonic('S');
        playAll.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Set<String> keys = synths.keySet();
                Iterator<String> iter = keys.iterator();
                while (iter.hasNext()) {
                    synths.get((String) iter.next()).start();
                }
            }
        }
        );
        menuControl.add(playAll);

        JMenuItem toggleAll = new JMenuItem("Toggle All");
        toggleAll.setMnemonic('S');
        toggleAll.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Set<String> keys = synths.keySet();
                Iterator<String> iter = keys.iterator();
                while (iter.hasNext()) {
                    synths.get((String) iter.next()).toggle();
                }
            }
        }
        );
        menuControl.add(toggleAll);

        JMenuItem stopAll = new JMenuItem("Stop All");
        stopAll.setMnemonic('S');
        stopAll.addActionListener(
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Set<String> keys = synths.keySet();
                Iterator<String> iter = keys.iterator();
                while (iter.hasNext()) {
                    synths.get((String) iter.next()).stop();
                }
            }
        }
        );
        menuControl.add(stopAll);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(menuNew);
        menuBar.add(menuControl);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(content);
        add(scroll);

        JPanel messageBar = new JPanel();
        message = new JLabel("Welcome!");
        message.setFont(new Font("Monospace", Font.PLAIN, 9));

        messageBar.add(message, BorderLayout.WEST);
        add(messageBar, BorderLayout.SOUTH);
    }

    public void paint(Graphics g) {
        super.paint(g);
    }

    private class MenuHandler implements ActionListener {
        private SynthType type;
        public MenuHandler(SynthType t) {
            type = t;
        }
        public void actionPerformed(ActionEvent e) {
            addSynth(type);
        }
    }

    public void displayMessage(String msg, Color color) {
        message.setText(msg);
        message.setForeground(color);
        repaint();
    }
}
