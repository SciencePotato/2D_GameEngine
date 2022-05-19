## [Camera Projection & View](https://www.youtube.com/watch?v=r857cbEtEY8&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=8)

### Perspective and Orthogonal Camera
Orthogonal wouldn't increase the size of an object no matter the distances. This is not true
for Perspective as it's literally a perspective of the object. There's also clipping created by
the near and far. If the object is over or near a certain place; it'll be clipped.

![Orthogonal And Perspective Camera](../Images/Cameras.png)

### Matrix
We're using Homogenous Matrix; using View Matrix, with clipping matrix to transform object 
from world space to Matrix Space.

`Projection * View * Position`

Projection -> Projecting it onto a plane; onto our view.   
View -> How the Camera is Looking.  
Position -> Position of Camera.

**Probably have to Relearn this for Perspective**

### Camera Class
```java

private Matrix4f projectionMatrix, viewMatrix;
private Vector2f position;

public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
}

public void adjustProjection() {
        projectionMatrix.identity();        // Create an Identity Matrix
        // Creates everything and make it block like; init Near and Far plane for clipping
        projectionMatrix.ortho(0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
}

public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        // Creates view matrix for us | Up to 20 Z coordinate, center is a bit in front of camera,
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                            cameraFront.add(position.x, position.y, 0.0f),
                                            cameraUp);
        return this.viewMatrix;
}

public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
}
```
The functions denoted in the Camera class is quite simple yet. The `getProjectionMatrix()` simply
returns your projection Matrix, the view Matrix will be creating a view matrix at a certain 
location looking at a certain direction, indicated by the function `lookAt()`. The adjustProjection
is basically creating an Identity matrix and create chunks or tile blocks for our desired size.

### Shader.java
```java
public void uploadMat4f(String varName, Matrix4f matrix4f) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(matBuffer);

        glUniformMatrix4fv(varLocation, false, matBuffer);
}
```

It added a function so that you can add in the Matrix input, by first creating a buffer,
the matrix size, and putting it as a glUniform in the shader.

### LevelEditorScene
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
This is the code to actually upload the information to the Shader and pass it in as Uniform.



### Default.glsl
More Detail next Video