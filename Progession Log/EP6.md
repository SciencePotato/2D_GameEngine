## [Shader abstraction & Regexes](https://www.youtube.com/watch?v=ucpi06deiyY&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE&index=)
This is one of the more straightforward tutorial; namely, we just migrate stuff accomplished
prior to a new location, and abstracted it and make it basically usable for all Shader we're making
in the future.

### Regex's
Pattern recognition using `#type vertex/ #type fragment`

### Shader
The code of Shader is mainly moved from the Windows; it's essentially all the stuff we've 
accomplished in EP5.md, just with some new edition.

```java

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
```

This first takes in a filePath, which is the file path of a specific Shader Program that we 
want to pass in. Then, what we want to do is to parse it using REGEX. The purpose of
using REGEX is so that we can find which portion of the code belong with whichever shader
source. Then we just replace everything inside.