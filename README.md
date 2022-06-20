# LPSolverAssignment
Linear programming Assignment
 
## Dependencies
LPSolve
On Windows:
Extract all the files from lp_solve_5.5.2.11_dev_win64.zip into C:\Windows </br>
Copy lpsolve55.dll and lpsolve55j.dll into C:\Windows  </br>
[download link](https://sourceforge.net/projects/lpsolve/files/lpsolve/)

On Linux: 
Copy the wrapper stub library liblpsolve55j.so to the directory that already contains liblpsolve55.so. Run ldconfig to include the library in the shared libray cache.

Maven
Use IntelliJ idea or any other idea to get the Maven plugin

## Build
mvn clean install (from the directory that run from the directory that contains pom.xml)


## License
All rights reserved
