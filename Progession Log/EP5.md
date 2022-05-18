## [Drawing the First Square](https://www.youtube.com/watch?v=vnqb9vdaxwA&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=6)
### This chapter is really hard to comprehend, you should re-watch it once or twice to get a better understanding.

### Scene
Added a method call init, basically it starts the initial binding of VAO, VBO, EBO, and the update
function, which is depended on the Scene, will render out all the information prior.

### Window
Basically, instead of nothing at all, after we change scene, we `init()` the scene; placing
every vertex and what not.

### Default.glsl
This is our GLSL Shader program; contains both vertex and fragment shader.
```glsl
#type vertex
#version 330 core

layout (location = 0) in vec3 aPos;
layout(location=1) in vec4 aColor;

out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;

out vec4 color;
                                                                                                            
void main(){
    color = fColor;
}
```

The headers, or specifically `#type vertex` and `#type fragment` are just declarative header 
for the user to see. The important part is `version 330 core`. It indicates which version of
GLSL/OpenGL is used in the process.

`layout(location=0) in vec3 aPos` The layout location is indicated of which index position 
the attribute is in a specific VBO, and the size, indicated by vec3 and vec4.

`out vec4 fColor` It spits out a vec4, and this will then be use in the later program, when you
take in a fColor instead of output. By the Fragment Shader, you'll spit out an actual color 
for the user to use.

### LevelEditorScene (The Hard Parts starts)
**Variables** 

`vertexShaderSrc/fragmentShaderSrc` - Basically the source code exactly for GLSL for each
individual parts.
`vertexArray, elementArray` - VBO and EBO, vertex Array being the VBO, it contains information
such as the position (Normalize or not) and RGB if so wish. Then EBO contains the index order in
which they are being draw **COUNTER-CLOCKWISE**.  
`vertexID, fragmentID, programID` - Unique identifiers for the Vertex and Shader code we're running
since there is the possibility that you possess multiple Vertex and Fragment Shader. The program
shader is basically the compiled and linked version of the Vertex and Fragment.  
`vaoID, vboID, eboID` -  Unique identifiers for the vao and vbo and ebo since a given scene could have
multiple. The purpose is to first make them, and bind them to the vertexArray and elementArray
we made in the past. After that, we basically combine them using prebuilt OpenGl functions.

**Code**

#### Initialization process
Firstly, we want to compile and the shader and then link it to a final program. The compile
process are like the following:
```java
vertexID = glCreateShader(GL_VERTEX_SHADER);
// Pass the shader source code to the GPU
glShaderSource(vertexID, vertexShaderSrc);
glCompileShader(vertexID);

// Check for Error in compilation process above;
int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
if (success == GL_FALSE) {
    int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
    System.out.println("ERROR: Default shader.glsl \n Vertex Shader");
    System.out.println(glGetShaderInfoLog(vertexID, len));
    assert false : "";
}

// Load and compile fragment Shader
fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
// Pass the shader source code to the GPU
glShaderSource(fragmentID, fragmentShaderSrc);
glCompileShader(fragmentID);

// Check for Error in compilation process above;
success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
if (success == GL_FALSE) {
    int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
    System.out.println("ERROR: Default shader.glsl \n Fragment Shader");
    System.out.println(glGetShaderInfoLog(fragmentID, len));
    assert false : "";
}
```
In this process, we want to first bind the ID using `glCreateShader` and pass in the type
of shader, which is `GL_VERTEX_SHADER/GL_FRAGMENT_SHADER`. This then will make the CPU realize
which shader program it's processing. The next part is pretty self-explanatory; we combine
the Shader Src with the unique identifying attribute and compile it.  
`glShaderSource(ID, ShaderSrc)` - Takes in an identifying ID and Shader Src  
`glCompileShader(ID)` - Takes in an ID bind to a source, and compiles it.

The next step is for debugging and to see if there's an error in the code. You check the Compiled
status and see if it's equal to `GL_FALSE`; aka, there exists an error.

After this process, we have Linking to the `ShaderProgram`
```java
// Link Shaders and check for error
shaderProgram = glCreateProgram();
glAttachShader(shaderProgram, vertexID);
glAttachShader(shaderProgram, fragmentID);
glLinkProgram(shaderProgram);

success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
if (success == GL_FALSE) {
    int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
    System.out.println("ERROR: Default shader.glsl \n ShaderProgram Shader");
    System.out.println(glGetProgramInfoLog(shaderProgram, len));
    assert false : "";
}
```

This is the easy to understand, since we created both Shader, the vertex and the fragment, we
can just use the function `glAttachShader(Program, ID)` to attach a given shader to the program.
Then we use `glLinkPorgram(Program)` to link and combine to create the file GPU can read; and we 
also check for existing errors.

The next step is to create and bind VBO and EBO to VAO.
```java
// Generating VAO
vaoID = glGenVertexArrays();
// Binds the following processes to this specific vaoID
glBindVertexArray(vaoID);
// Create a float buffer of vertices
FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
// Flips ensures that OpenGL gets the order correctly
vertexBuffer.put(vertexArray).flip();

// Generating VBO
vboID = glGenBuffers();
glBindBuffer(GL_ARRAY_BUFFER, vboID);
glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

// Create indices and upload
IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
elementBuffer.put(elementArray).flip();

eboID = glGenBuffers();
glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
```
`glGenVertexArrays` - Generates the Vertex Array Object, and we bind it to the specific
array Object using `glBindVertexArray`; which is it for the creation process.

The weird thing about OpenGL is it needs a Float or 
even an Int Buffer to be passed in. This is why we use `FloatBuffer/IntBuffer` to put in
the information. We also, then, put in our vertex/Element Array, and **Flip** it so OpenGL reads
it correctly.

Then we create individual buffer parts for VBO and EBO using `glGenBuffers()`. Then, once again, we
need to bind it with a new command `glBindBuffer(GL_TYPE_BUFFER, ID)`. The last step is to use
`glBufferData(GL_TYPE_ARRAY, buffer, GL_DRAW_TYPE)`; This step I'm not that sure about.

The Last step is to indicate each attribute of VAO for vertex Shader
```java

// Add vertex attribute buffers | Strides and offsets
int positionSize = 3;
int colorSize = 4;
// Since everything is in bytes, and VBO is in 4 bytes, hence everything has to be 4;
int floatSizeBytes = 4;
int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

// Index, Amount of Element, Type, if it's normalized, The size of one vertex, the offsets         
glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
glEnableVertexAttribArray(0);

glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
glEnableVertexAttribArray(1);
```
This step is one of the easiest. You first identify how long each part is, since vertex position
consist of 3 point, it's position size is 3, and color consist of RGBA, it's 4. The type of which
is float, so we need to multiply everything by 4 since OpenGL counts in `BYTE`. 

`glVertexAttribPointer(position, size, TYPE, normalized, array size, offsets)`
This is corresponded to your `layout(location=0)` in your Vertex Shader. Then you need to 
enable the attribute for the array. (Don't really get the last part of this).

#### Update/Render

This is significantly easier than the Initialization portion for the code.
```java

// Bind shader program
glUseProgram(shaderProgram);
// Bind the VAO that we're using
glBindVertexArray(vaoID);

// Enable the vertex attribute pointers
glEnableVertexAttribArray(0);
glEnableVertexAttribArray(1);

glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
 
// Unbind everything
glDisableVertexAttribArray(0);
glDisableVertexAttribArray(1);
 
glBindVertexArray(0);

glUseProgram(0);
```

First we want to bind the shader program and the VAO to the specific scene. Then we want to let 
the program know that we're using the specific vertex attribute pointers defined in init(). We then
want to draw the triangles (It could also be points).

Last step of this is to unbind, basically free the memory back up.
