package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource = "";
    private String fragmentSource = "";
    private String filepath;

    public Shader(String filePath){
        this.filepath = filePath;
        try{
           String source = new String(Files.readAllBytes(Paths.get(filePath)));
           String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

           int index = source.indexOf("#type") + 6;
           int eol = source.indexOf("\r\n", index);


           String firstPattern = source.substring(index, eol).trim();

           index = source.indexOf("#type", eol) + 6;
           eol = source.indexOf("\r\n", index);

           String secondPattern = source.substring(index, eol).trim();
           if (firstPattern.equals("vertex")) {
               vertexSource = splitString[1];
           } else if(firstPattern.equals("fragment")){
               fragmentSource = splitString[1];
           } else {
               throw new IOException("Unexpected token; " + firstPattern);
           }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token " + secondPattern);
            }

            System.out.println(vertexSource);
            System.out.println(fragmentSource);
        }catch (IOException e) {
           e.printStackTrace();
           assert false: "Error: Cannot open shader" + filePath;
        }

    }

    public void compile() {
        int vertexID, fragmentID;

        //Load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for Error in compilation process above;
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "\n Vertex Shader");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile fragment Shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for Error in compilation process above;
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "\n Fragment Shader");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link Shaders and check for error
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:" + filepath + "\n ShaderProgram Shader");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        // Bind shader program
        if (!beingUsed) {
            beingUsed = true;
            glUseProgram(shaderProgramID);
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f matrix4f) {
        // Get the location of the program in certain Shader
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        // Make sure you're absolutely using the shader
        use();
        // Flatten the information out instead of 2D array;
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        // Stuff everything
        matrix4f.get(matBuffer);

        // Telling the glUniform(GPU) to make it a Uniform
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f matrix3f) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        matrix3f.get(matBuffer);

        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }
}
