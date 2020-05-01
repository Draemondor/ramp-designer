General Instructions for Installing External Libraries with Visual Studio.
{Watch Video First}

Required Items:
1.Cmake and/or Premake for building binaries.
2.header & lib files of library intended for use with application.

URLS:
---Binaries---
1.https://cmake.org
2.https://premake.github.io
----------------------------
---Libs Used---
1. https://www.glfw.org/download.html
2. https://glad.dav1d.de
3. https://github.com/SpartanJ/SOIL2
4. https://glm.g-truc.net/0.9.9/index.html
5. https://www.assimp.org

{It is recommended that you create a dependencies folder
 within your visual studio project solution folder}.

Step 1. Create a folder named after the target library within your dependencies folder.

Step 2. Create a lib folder and an include folder within the library folder.

Step 3.  Open Cmake or Premake and select the libs in wich 
you wish to build binaries for. {this application runs x86(32-bit)}

Step 4. Run the solution created by Cmake/Premake and build for
target archtitecture (x86 or x64) within Visual Studio.

Step 5. Locate the build folder and copy the lib files into the lib folder in "Dependencies\library\lib"

Step 6. Paste all header files included in download into the include folder in "Depenencies\library\include"

Step 7. Right click the Solution within Visual Studio and click Properties.

Step 8. Go to C/C++ and select General. Locate Additional Include Directories at the top
of the window and at the very right of the window pane click the down arrow and click edit.

Step 9. Add the directory path of all the library include folders within dependencies and click apply 
and ok when finished.

Step 10. Go to Linker and select General. Locate Additional Library Directories and do the same as before
but this time add the directory path of the library lib folders within dependencies and click apply.

Step 11. Select Input under Linker and locate Additional Dependencies.  put the name of the lib 
file and click apply.

{Note some obj models will take longer to load the more polys they need to render}

****************************IMPORTANT************************
---Controls while in application---
W- move forward
A- move left
S- move back
D - move right
Space - move up
Left Control - move down
F - wireframe view
V - filled poly view
Scroll wheel - Zoom Amount
Esc - exit application
**************************************************************
