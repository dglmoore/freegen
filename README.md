# FreeGen

**FreeGen** is a very simple frequency generator. 

### Generator Types
**FreeGen** offers two types of frequency generator: *pure tone* and *panning tone*.

A *pure tone* generator produces a single frequency generated indefinitely. To add a *pure tone* generator, go to the **New** menu, and select **Pure Tone**. This will add a panel to the main window. You should then be able to adjust the frequency and volume of the sound. Selecting **Play** will then start then audio. Because a *pure tone* generator can only generate a single frequency, if you want to hear more than one frequency playing at once you must add multiple generators. You can do that via the **New** menu as before.

Sometimes, you might want to hear a frequency change continuously over a period of time. This is the purpose of the *panning* generator. You can add a *panning* generator via the **New** menu as above. The generator will provide you with the option to change the starting and stopping frequencies and the period, as well as the volume. The audio will vary continuously from the starting frequency up to the stopping frequency; once the stopping frequency is reached it will immediately repeat. As with the *pure tone* genertors, you can add as many *panning* generators as you wish.

### Additional Controls
In addition to the controls described above, there are a handful more.

First, if you'd like to remove a frequency generator you can right-click any open space in a generator's panel. This will provide you with a context menu with a **Remove** option.

Second, the **Control** dropdown menu provides the user with three options: **Play All**, **Stop All** and **Toggle All**.
  * **Play All** sets the state of all generators to *play*
  * **Stop All** is the opposite of **Play All**
  * **Toggle All** starts all stopped generators and stops all playing generators.

And that is it. If you've made it this far, you know everything there is too know! Congratulations.

### Downloads
You can download compiled releases of **FreeGen** on the [releases page](http://github.com/dglmoore/freegen/releases). There are three compiled versions, one for each of Java 6, 7 and 8:
  * Java 6: [FreeGen6.jar](https://github.com/dglmoore/freegen/releases/download/v1.0.1/FreeGen6.jar)
  * Java 7: [FreeGen7.jar](https://github.com/dglmoore/freegen/releases/download/v1.0.1/FreeGen7.jar)
  * Java 8: [FreeGen8.jar](https://github.com/dglmoore/freegen/releases/download/v1.0.1/FreeGen8.jar)

If you are unsure of which version of Java you have, start with the Java 6 version and move up until one works! Additionally, you can download the source code via the usual GitHub facilities or from the releases page.

### Issues
If you have issues with **FreeGen**, please file an issue on the [issues page](http://github.com/dglmoore/freegen/issues). Describe what operating system you use, e.g. Windows 7, Windows 8, Mac OS X, etc...) as well what the problem was and how I might go about reproducing it.

### Thank you for your interest in **FreeGen**.
