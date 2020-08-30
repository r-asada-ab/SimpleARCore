# SimpleARCore
Minimal implementation in kotlin when using ARCore.

# Instalation

```
$ git clone git@github.com:r-asada-ab/SimpleARCore.git
```

In Android Studio, open the SimpleARCore project.

# Feature

Removed anything that wasn't necessary to get started for the first time from what was in the official tutorial.
Also, since most of the code was written in MainActivity, it was hard to understand, so I classify it a little.

The settings required to use ARCore are as follows:

* AndroidManifest settings
  1. Set camera permissions
  2. Prevent download from non-ARCore compatible devices
  3. Prevent download from devices that do not support OpenGL ES2.0
  4. Describe meta-data tag of ARCore
  
* build.gradle(app) settings
  1. Set minSdkVersion to 24 or higher.
  2. Add the ARCore library to depenedencies.

* Implementation of GLSurfaceView
  1. 1. Add GLSurfaceView to the layout file
  2. 1. Create a class that implements GLSurfaceView.Renderer interface
  3. Initialize GLSurfaceView

* ARCore initialization
  1. Check if ARCore is installed on the device
  2. Check camera permissions
  3. Initialize Session

* Create Shader
  1. Write a vertex shader
  2. Write a fragment shader
  3. Load shader
  4. Create a shader program
  5. Draw with shader

When drawing a plane etc. detected by ARCore, draw a plane with a shader as if the camera was drawing with a shader.
We use the shader to draw what we want to draw like this, and sometimes we use the model to make it.

Hope this sample helps to understand ARCore.

# Official Tutorial

https://developers.google.com/ar/develop/java/quickstart

