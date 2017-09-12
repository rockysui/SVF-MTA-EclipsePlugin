# SVF-MTA-EclipsePlugin
EclipsePlugin of Data Race Detection

### Setup instructions.

1. Download the plugin (PLUGIN NAME) at (ADDRESS). (When released).

2. Download the MTA tool and libraries at (ADDRESS).

3. Setup the LLVM Gold plugin. Instructions are at https://github.com/SVF-tools/SVF/wiki/Install-LLVM-Gold-Plugin-on-Ubuntu

4. In your C/C++ project set up a debug configuration (FOR multiple files/and single file //TODO)
For now, set up an external tool to run clang on your file to convert it to a bc file. 
![Imgur](https://i.imgur.com/TZsg9qj.png)
Using the following arguments:
* Location: `path of clang`
* Working Directory: `Your current working directory`
* Arguments: `-S -emit-llvm -g $(workspace_loc:/<PROJECTNAME>/<FILEPATH>} -o <OUTPUTNAME>.bc`
![Imgur](https://i.imgur.com/xmThVXK.png)

6. Setup the MTA external tool:
* Location: `path to MTA`
* Working Directory: `Current working directory`
* Arguments: `-mhp outut.txt`
![Imgur](https://i.imgur.com/qBs5m7h.png)

7. Convert the c file into a bc file by running the external tool that converts c to bc.

8. Run the MTA external tool to generate the output.txt file

9. Now errors in the file should be annotated if there are errors.
