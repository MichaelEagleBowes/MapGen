# MapGen

MapGen was developed as part of the bachelor thesis of Michael Bowes and is a stateless software that supports the analysis of procedural algorithms by offering functionalities for controllable, i.e. parametrized, generation of 2D maps and statistical evaluation functions for procedural algorithms.

**Currently Working**

- Two algorithms are currently implemented: The Cellular Automaton and the Diamond-Square.
- Histograms for evaluation of the metrics
- Scatter charts for evaluation of the algorithms

**Missing Features**

- Implementations of other procedural algorithms covered in the thesis.
- Import and Export functionality, such that generated maps can also be used for other projects.

**Running the Application**

The software uses images for the display of its tiles, which are required to execute any runnable .jar files created for this software. 
This means that in order to get an executable build, export a runnable jar file, create a new folder named 'resources' in the root directory of the .jar and copy the image files found within the resource folder of the project into this folder.
