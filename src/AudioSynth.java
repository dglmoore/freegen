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

import java.nio.ByteBuffer;
import java.io.ByteArrayInputStream;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.*;

import java.util.concurrent.locks.*;

public class AudioSynth extends Thread implements Cloneable {
  private float rate;
  private int freq1;
  private int freq2;
  private double period;
  private double volume;

  private int frames;
  private int samples;
  private int bytes;

  private AudioFormat format;
  private ByteBuffer buffer;
  private AudioInputStream stream;
  private Clip clip;

  private Lock threadLock;
  private Condition canExit;

  private AudioSynth(float rate, int freq1, int freq2,
                     double period, double vol) throws Exception {
    this.rate = rate;
    this.freq1 = freq1;
    this.freq2 = freq2;
    this.period = period;
    this.volume = vol;

    frames = (int) Math.ceil(rate * period);
    samples = 2*frames;
    bytes = 2*samples;

    init();
  }

  private void init() throws Exception {
    if (buffer == null)
      buffer = ByteBuffer.allocate(bytes);

    threadLock = new ReentrantLock();
    canExit = threadLock.newCondition();

    double step = (double) (freq2 - freq1) / frames;
    double freq = freq1;
    for (int i = 0; i < frames ; ++i, freq += step) {
      double angle = (4.0 * Math.PI * i * freq) / rate;
      short value = (short) (Math.sin(angle) * Short.MAX_VALUE * volume);
      buffer.putShort(4*i, value);
      buffer.putShort(4*i+2, value);
    }

    format = new AudioFormat(rate, 16, 2, true, true);
    stream = new AudioInputStream(new ByteArrayInputStream(buffer.array()),
                                  format, frames+1);
    clip = AudioSystem.getClip();
    clip.open(stream);
  }

  public static AudioSynth getPureTone(int freq, double vol) throws Exception {
    return new AudioSynth(44100f, freq, freq, 10, vol);
  }

  public static AudioSynth getPanning(int f1, int f2, double period,
                                      double vol) throws Exception {
    return new AudioSynth(44100f, f1, f2, period, vol);
  }

  public void quit() {
    threadLock.lock();
    canExit.signal();
    threadLock.unlock();
  }

  public void run() {
    try {
      threadLock.lock();
      clip.loop(Clip.LOOP_CONTINUOUSLY);
      canExit.await();
      clip.stop();
      threadLock.unlock();
    } catch (Exception e) {
      clip.stop();
      threadLock.unlock();
    }
  }

  public boolean isRunning() { return clip.isRunning(); }
}
