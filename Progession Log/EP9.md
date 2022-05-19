## [Texture Loading](https://www.youtube.com/watch?v=7i9oXEoe86Q&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=10)
This is a lighter tutorial comparatively speaking. The basic idea is, you have coordinates
generated from your sprite (UV coordinates), and you want to map them to a certain rendering
square or something. You'll also have to specify certain components such as: 
```java
// Wrapping (X, Y)
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

// Pixelation instead of blurring
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
```

### Shader.java
```java
public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
}
```
This is basically the same as upload a uniform `int`, but instead we're uploading
a texture, so slight changes.

### LevelEditorScene.java
```java
// Upload uniform Texture to GPU
defaultShader.uploadTexture("TEX_SAMPLER", 0);
glActiveTexture(GL_TEXTURE0);
testingTexture.bind();
```
This is the same as us binding VAO/VBO/EBO to the place. In this case, we're binding a texture; 
telling the GPU to specifically use that texture to reduce confusion.

### default.glsl

```glsl
#type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;

out vec4 color;

uniform float uTime;
uniform sampler2D TEX_SAMPLER;

void main(){
    // float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);
    color = texture(TEX_SAMPLER, fTexCoords);
}
```
The only update for GLSL is including fTexCoords, which is supposed to be the UV coordinate
that we're mapping the Image to. Then we use built-in GLSL function to map it as "Color" or
output in this case.

### Texture.java (New)
```java
public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
}

public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
}
```
This is the easy part, we're literally binding the Texture to the specific TextureID and
unbinding it; like VAO/VBO/EBO.
```java
// Generate Texture
texID = glGenTextures();
glBindTexture(GL_TEXTURE_2D, texID);

// Set Texture Parameters
// Repeat Image in both Direction (Wrapping) | For Both X and Y (S and T)
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

// We want pixelation instead of blur         
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
// Shrinking == More pixelation
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
```
This is the initial step up; loading in parameters setting and actually binding the graphics.
```java
stbi_set_flip_vertically_on_load(true);
// Buffering from Image to Pixel information
IntBuffer width     = BufferUtils.createIntBuffer(1);
IntBuffer height    = BufferUtils.createIntBuffer(1);
IntBuffer channels  = BufferUtils.createIntBuffer(1);
ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
```
Then we are creating the Pixel information from the buffer, the height and width respectively.
`I don't know why it's 1 width`. Channels is for RGBA or any other type. So if you have other type
of Image with other channels, you can use the following code to check if there's an error
or not.
```java

if (image != null) {
        // Uploads pixel to GPU, create a buffer of W x H, and tell them the exact format
        if (channels.get(0) == 3) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
            width.get(0), height.get(0),
            0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else if (channels.get(0) == 4){
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
            width.get(0), height.get(0),
            0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        } else {
            assert false: "Error(Texture Channel): could not load image" + filepath;
        }

} else {
    assert false : "Error(Texture): could not load image" + filepath;
}
```
This uploads the information to the GPU, allowing it to take load the Texture Image 2D. Then 
finally, since we're using the `stbi` library, which is essentially like C, we want to free
up the memory by simply call:
```java
stbi_image_free(image);
```
