## [Drawing the First Square](https://www.youtube.com/watch?v=vnqb9vdaxwA&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=6)
### This chapter is really hard to comprehend, you should rewatch it once or twice to get a better understanding.

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