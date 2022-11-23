## Karl, an homemade programming language

## Example

```js
int: number = 150; // this is a comment
bool: boolean = true;
int: num = 1;

func main::(): int -> {
  return 1;
}

show(main() + num);
// -> 2
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
    - [x] AND and OR operator
    - [x] Else Statement
- [ ] While Statement
- [ ] For Statement
- [ ] Array
- [ ] Class
- [ ] Module
- [ ] Import
- [ ] Export
- [ ] Operator
- [x] Increment and Decrement
- [x] Throw new RetardException()
- [ ] Try Catch