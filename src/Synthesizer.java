/*
 * Copyright 2014 Douglas Moore. All rights reserved.
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 */

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.event.*;

public class Synthesizer<AppClass extends Container & GeneratorApp> extends Object {
  public final Integer MIN_FREQUENCY      = 10;
  public final Integer INITIAL_FREQUENCY  = 250;
  public final Integer MAX_FREQUENCY      = 25000;
  public final Double  MIN_PERIOD         = 0.1;
  public final Double  MAX_PERIOD         = 25.0;
  public final Double  INITIAL_PERIOD     = 1.0;
  private AudioSynth synth;
  private JPanel panel;
  private JPopupMenu popup;
  private SynthType type;
  private final String identity;

  private JButton play;
  private JSlider volume;
  private JTextField freq1;
  private JTextField freq2;
  private JTextField period;

  private JLabel[] labels;

  private AppClass context;

  public Synthesizer(AppClass c, String id, SynthType t) {
    type = t;
    context = c;
    identity = id;

    initPopup();
    initPanel();
    initSynth();
  }

  public JPanel getPanel() {
    return panel;
  }

  private void initPanel() {
    panel = new JPanel();

    GroupLayout layout = new GroupLayout(panel);

    panel.setLayout(layout);

    panel.setMaximumSize(
      new Dimension((int) context.getSize().getWidth(), 80)
    );

    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    panel.addMouseListener(new PopupHandler());

    setupLowFreq();
    setupHighFreq();
    setupPeriod();
    setupPlay();
    setupVolume();

    String[] lbls = { "Freq 1: ", "hz ", "Freq 2: ", "hz ",
                      "Period: ", "s ", "Vol: "
                    };
    labels = new JLabel[lbls.length];
    for (int i = 0; i < lbls.length; ++i) {
      labels[i] = new JLabel(lbls[i]);
    }

    JSeparator[] separator = {
      new JSeparator(SwingConstants.VERTICAL),
      new JSeparator(SwingConstants.VERTICAL),
      new JSeparator(SwingConstants.VERTICAL),
    };

    layout.setAutoCreateContainerGaps(true);

    layout.setHorizontalGroup(
      layout.createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
                .addComponent(labels[0])
                .addComponent(freq1)
                .addComponent(labels[1])
                .addComponent(separator[0])
                .addComponent(labels[2])
                .addComponent(freq2)
                .addComponent(labels[3])
                .addComponent(separator[1])
                .addComponent(labels[4])
                .addComponent(period)
                .addComponent(labels[5])
                .addComponent(separator[2])
                .addComponent(play)
               )
      .addGroup(layout.createSequentialGroup()
                .addComponent(labels[6])
                .addComponent(volume)
               )
    );
    layout.setVerticalGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                      .addComponent(labels[0])
                                      .addComponent(freq1)
                                      .addComponent(labels[1])
                                      .addComponent(separator[0])
                                      .addComponent(labels[2])
                                      .addComponent(freq2)
                                      .addComponent(labels[3])
                                      .addComponent(separator[1])
                                      .addComponent(labels[4])
                                      .addComponent(period)
                                      .addComponent(labels[5])
                                      .addComponent(separator[2])
                                      .addComponent(play)
                                     )
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                      .addComponent(labels[6])
                                      .addComponent(volume)
                                     )
                           );
  }

  private void initPopup() {
    popup = new JPopupMenu();
    JMenuItem remove = new JMenuItem("Remove");
    remove.addActionListener(
    new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stop();
        context.removeSynth(identity);
      }
    }
    );
    popup.add(remove);
  }

  private void initSynth() {
    normalizeFields();
    try {
      int f1 = Integer.parseInt(freq1.getText());
      double vol = ((double) volume.getValue()) / 100.0;
      if (type == SynthType.PURE) {
        synth = AudioSynth.getPureTone(f1, vol);
      } else if (type == SynthType.PANNING) {
        int f2 = Integer.parseInt(freq2.getText());
        double T = Double.parseDouble(period.getText());
        synth = AudioSynth.getPanning(f1, f2, T, vol);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void setupLowFreq() {
    freq1 = new JTextField(INITIAL_FREQUENCY.toString());
    freq1.setHorizontalAlignment(JTextField.RIGHT);
    freq1.setColumns(6);
    freq1.addActionListener(
    new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        normalizeField(source,
                       MIN_FREQUENCY, MAX_FREQUENCY, INITIAL_FREQUENCY,
                       "Low Frequency", "hz");
        start();
      }
    }
    );
  }

  private void setupHighFreq() {
    freq2 = new JTextField((new Integer(INITIAL_FREQUENCY + 750)).toString());
    freq2.setColumns(6);
    freq2.setHorizontalAlignment(JTextField.RIGHT);
    freq2.addActionListener(
    new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        normalizeField(source,
                       MIN_FREQUENCY, MAX_FREQUENCY, INITIAL_FREQUENCY,
                       "High Frequency", "hz");
        start();
      }
    }
    );
    if (type == SynthType.PURE) freq2.setEditable(false);
  }

  private void setupPeriod() {
    period = new JTextField(INITIAL_PERIOD.toString());
    period.setColumns(4);
    period.setHorizontalAlignment(JTextField.RIGHT);
    period.addActionListener(
    new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        normalizeField(source,
                       MIN_PERIOD, MAX_PERIOD, INITIAL_PERIOD,
                       "Period", "s");
        start();
      }
    }
    );
    if (type == SynthType.PURE) period.setEditable(false);
  }

  private void setupPlay() {
    play = new JButton("Play");
    play.addActionListener(
    new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isRunning()) {
          stop();
        } else {
          start();
        }
        context.repaint();
      }
    }
    );
  }

  private void setupVolume() {
    volume = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
    volume.addChangeListener(
    new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
          if (source.getValue() != 0 && isRunning()) {
            start();
          } else {
            stop();
          }
        }
      }
    }
    );

    volume.setMajorTickSpacing(25);
    volume.setPaintTicks(true);
    volume.setMinorTickSpacing(10);
  }

  public void start() {
    if (isRunning()) stop();
    initSynth();
    synth.start();
    play.setText("Stop");
  }

  public void stop() {
    synth.quit();
    play.setText("Play");
  }

  public void toggle() {
    if (isRunning()) {
      stop();
    } else {
      start();
    }
  }

  private void displayMessage(String msg, Color color) {
    context.displayMessage(msg,color);
  }

  private class PopupHandler extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      triggered(e);
    }

    public void mouseReleased(MouseEvent e) {
      triggered(e);
    }

    public void triggered(MouseEvent e) {
      if (e.isPopupTrigger()) {
        popup.show(
          e.getComponent(), e.getX(), e.getY()
        );
      }
    }
  }

  public void normalizeFields() {
    normalizeField(freq1,
                   MIN_FREQUENCY, MAX_FREQUENCY, INITIAL_FREQUENCY,
                   "Low Frequency", "hz");

    normalizeField(freq2,
                   MIN_FREQUENCY, MAX_FREQUENCY, INITIAL_FREQUENCY,
                   "High Frequency", "hz");

    normalizeField(period,
                   MIN_PERIOD, MAX_PERIOD, INITIAL_PERIOD,
                   "Period", "s");
  }

  public void normalizeField(JTextField field, Integer low, Integer high, Integer init, String name, String unit) {

    String unparsed = field.getText();
    String cleaned = unparsed.replaceAll("[^\\d]","");

    if (cleaned.compareTo(unparsed) != 0) {
      displayMessage("ERROR: " + name + " - no non-numeric characters are " +
                     "allowed in text fields.", Color.RED);
      field.setText(cleaned);
    }
    if (cleaned.compareTo("") == 0) {
      displayMessage("ERROR: " + name + " cannot be empty.", Color.RED);
      field.setText(init.toString());
    } else {
      Integer value = new Integer(cleaned);
      if (value < low) {
        field.setText(low.toString());
        displayMessage(
          "ERROR: " + name + " must be at least " + low.toString() + unit + ".",
          Color.RED);
      } else if (value > high) {
        field.setText(high.toString());
        displayMessage(
          "ERROR: " + name + " must not exceed " + high.toString() + unit + ".",
          Color.RED);
      }
    }
  }

  public void normalizeField(JTextField field, Double low, Double high, Double init, String name, String unit) {

    String unparsed = field.getText();

    if (unparsed.compareTo("") == 0) {
      displayMessage("ERROR: " + name + " cannot be empty.", Color.RED);
      field.setText(init.toString());
    } else {
      Double value = new Double(unparsed);
      if (value < low) {
        field.setText(low.toString());
        displayMessage(
          "ERROR: " + name + " must be at least " + low.toString() + unit + ".",
          Color.RED);
      } else if (value > high) {
        field.setText(high.toString());
        displayMessage(
          "ERROR: " + name + " must not exceed " + high.toString() + unit + ".",
          Color.RED);
      }
    }
  }

  public boolean isRunning() {
    return synth != null && synth.isRunning();
  }
