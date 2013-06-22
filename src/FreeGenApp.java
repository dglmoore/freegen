/*
 * Copyright 2012 Douglas Moore, Baylor University, Department of Physics
 *
 * This file is part of FreeGen.
 *
 * FreeGen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FreeGen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * FreeGen.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FreeGenApp extends JFrame implements GeneratorApp {
  private JPanel content;
  private HashMap<String,Synthesizer> synths;
  private KeyGen idGenerator;

  private JLabel message;

  public FreeGenApp() {
    super("FreeGen");

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

  private class MenuHandler implements ActionListener {
    private SynthType type;
    public MenuHandler(SynthType t) { type = t; }
    public void actionPerformed(ActionEvent e) {
      addSynth(type);
    }
  }

  public void displayMessage(String msg, Color color) {
    message.setText(msg);
    message.setForeground(color);
    repaint();
  }

  public void addSynth(SynthType type) {
    String id = idGenerator.nextID();
    synths.put(id, new Synthesizer<FreeGenApp>(this, id, type));
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
}
