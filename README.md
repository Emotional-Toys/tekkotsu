## tekkotsu

An open source framework supporting software development for a variety of robotic platforms. 

## About and how to use

Welcome to the Tekkotsu framework. Unless otherwise noted, all source code and related files are Copyright 2007, Carnegie Mellon University Tekkotsu Lab Released under the LGPL. Documentation is on the old project home [page](http://www.Tekkotsu.org/) where the online documentation is [here](https://www.cs.cmu.edu/~dst/Tekkotsu/Tutorial/docs.shtml). A presentation of its use, which is very handy, is found [here](/learning/sigcse07-workshop-slides.pdf).

### Directory Descriptions
```
* User Code Template *
    ------------------
    project - project template directory, copy to another location for local development

  * Source Files *
    ------------
```
### Platform-Specific Files

* `aperios` - provides support for Sony AIBO robots through the OPEN-R SDK, includes MMCombo, which forks into Main and Motion processes, in addition to Sound and TinyFTPD processes
* `local` - provides simulation support for the host platform (UNIX variants), as well as communication and device drivers for various hardware.

### General Source Code

* `Behaviors` - Base classes and demo code
* `DualCoding` - Vision processing package, see [here](http://www.cs.cmu.edu/~dst/Tekkotsu/Tutorial/vr-intro.shtml)
* `Events` - Base classes and 'custom' events
* `IPC` - Inter-Process Communication, mutual exclusion and message passing
* `Motion` - Most motion related classes
* `Shared` - Utility code
* `Sound` - Handles mixing and sending sound buffers to system
* `Vision` - Vision processing code
* `Wireless` - Networking code

### Non-Source Framework Files
    --------------------------
* `contrib` - additional code which either hasn't been inlined, or the original submissions of code that has been imported
* `docs` - scripts, configuration, and other files for the doxygen documentation
* `tools` - build scripts, utilities, and PC-side tools.
* `deprecated` - archaic source, but may still be of some interest
