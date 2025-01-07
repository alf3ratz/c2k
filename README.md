# C2K Converter
Converter of C/C++ code to Kotlin.

# How to install
Clone this repo with git and run
```bash
./gradlew run [flags]
```

### .jar release:
https://github.com/alf3ratz/converter/releases

# Usage
Run installed app
```bash
java -jar [Downloaded release name].jar [flags]
```

CLI flags:
- `-file` - path to c/c++ file
- `-code` - line of c/c++ code
- `-lines` - multiple lines of c/c++ code separated by commas

Example:
```bash
java -jar c2k.jar -code "int x = 5;"  
java -jar c2k.jar -code "int x = 5;","int y = 2;"
```

### TODO list
- [x] simple assignments
- [ ] curly braces handling
- [ ] simple classes/structures
- [ ] functions