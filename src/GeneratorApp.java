/*
 * Copyright 2014 Douglas Moore. All rights reserved.
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 */

import java.awt.Color;

public interface GeneratorApp {
  public void addSynth(SynthType type);
  public void removeSynth(String id);
  public void displayMessage(String msg, Color color);
}
