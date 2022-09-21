# Ryoko, my homemade programming language

## Technologies

- JVM
- ANTLR v4

## Syntax example

```
// this is a comment
int: one = 2;

System.show("Hello World!");
System.show(one);

-> Hello World!
-> 2
```

## How to run

- Compile the project with `mvn compile && maven package`
- Copy the generated JAR file in target folder anywhere you want
- Run the JAR file with `java -jar Ryoko-x.x.x-y-jar-with-dependencies.jar -f <file>` *work with -file too*

## TODO

- [x] Functions
- [ ] Class
- [ ] Type Bool
- [ ] File system
- [x] Base of error handler
- [ ] Can enter a file in option 
- [ ] Return statement in functions
- [ ] Math operations
- [ ] String operations
- [ ] Better CLI
- [ ] Better error handler
