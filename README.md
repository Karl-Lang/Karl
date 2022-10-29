## Karl, an homemade programming language

## Example

```js
int: number = 150; // this is a comment
bool: boolean = true; // this is a comment
final string: name = "Karl";
func main::(string: name) {
    show("Hello, ", name, "!");
    if (!true) -> {
        show("i");
    } else -> {
        show("j");
    }
}
main(name);
// -> Hello, Karl!
```

## How to use

- Download the .jar in latest release
- Run `java -jar karl-x.x.x.jar <file.karl>`

## Todo list

- [x] Print Statement
- [x] Variable
- [x] Final Variable
- [x] Comment
- [x] Functions
- [x] If Statement
    - [ ] AND and OR operator
- [ ] While Statement
- [ ] For Statement
- [ ] Array
- [ ] Class
- [ ] Module
- [ ] Import
- [ ] Export
- [ ] Operator
- [ ] Increment and Decrement
- [x] Throw new RetardException()
- [ ] Try Catch