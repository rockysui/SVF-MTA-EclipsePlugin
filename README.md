# SVF-MTA-EclipsePlugin
EclipsePlugin of Data Race Detection
![Alt Text](https://i.imgur.com/JDthlpw.gif)
### Setup instructions.

1. Download the plugin (PLUGIN NAME) at (ADDRESS). (When released).

2. Download the MTA tool and libraries at (ADDRESS).

3. Setup the LLVM Gold plugin. Instructions are at https://github.com/SVF-tools/SVF/wiki/Install-LLVM-Gold-Plugin-on-Ubuntu

4. In your C/C++ project set up a debug configuration. Toolchain must be LLVM with Clang.
![Imgur](https://i.imgur.com/Qaj8L1q.png)
5. Right click the project's "Properties":
And use the following compiler settings  
![Imgur](https://i.imgur.com/bN81SMP.png)  
![Imgur](https://i.imgur.com/XFlIopf.png)  
Building with the Debug configuration will yield bitcode files in the Debug folder  
![Imgur](https://i.imgur.com/wBcPBzD.png)

6. Setup the MTA external tool:
In 'External Tools Configurations'
* Location: `path to MTA`
* Working Directory: `Current working directory`
* Arguments: `-mhp <OUTPUTNAME>.bc`  
![Imgur](https://i.imgur.com/kfTtTkQ.png)

7. Run the MTA external tool to generate the output.txt file

8. Now errors in the file should be annotated if there are errors.
