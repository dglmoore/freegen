/*
 * Copyright 2014 Douglas Moore. All rights reserved.
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 */

import javax.swing.JFrame;
public class FreeGen {
  public static void main(String args[]) {
    FreeGenApp app = new FreeGenApp();
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setSize(550,500);
    app.setVisible(true);
  }
}
