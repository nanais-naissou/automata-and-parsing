# TOKENIZER — Object-Oriented Recognizers

TOKENIZER is a Java project implementing an object-oriented **Tokenizer** based on **Finite State Automata (FSA)**.

The tokenizer analyzes the contents of a text and categorizes its different components into **tokens**. Each token is recognized by a dedicated recognizer implementing a finite state automaton.

The recognized tokens are then sent to a **PrettyPrinter** through a listener mechanism, allowing the source text to be displayed with syntax highlighting.

---

## Overview

The project focuses on three main concepts:

1. **Finite State Automata** described using `.dot` files
2. **Object-oriented implementations** of these automata in Java
3. **Tokenization and syntax highlighting** of source files

The general processing pipeline is:

```text
              Input file
                  │
                  ▼
              Tokenizer
                  │
        ┌─────────┼─────────┐
        ▼         ▼         ▼
    Recognizer Recognizer Recognizer
        │         │         │
        └─────────┼─────────┘
                  │
                  ▼
              Listener
                  │
                  ▼
            PrettyPrinter
                  │
                  ▼
        Syntax-highlighted text
```

---

## Technologies

* **Java**
* **Finite State Automata**
* **Graphviz / DOT**
* **Make**
* **Eclipse / VS Code**

---

## Project Structure

The main structure of the project is:

```text
TOKENIZER/
├── src/
│   └── Recognizer/
│       ├── Tokenizer.java
│       ├── PrettyPrinter.java
│       ├── RecAny.java
│       ├── RecABC.java
│       ├── RecCommand.java
│       ├── RecIdent.java
│       ├── RecString.java
│       ├── RecComment.java
│       └── ...
├── TEST/
│   ├── OK/
│   └── ERROR/
├── *.dot.md
├── Makefile
└── README.md
```

The `.dot.md` files describe the finite state automata, while the corresponding `.java` files contain their object-oriented implementations.

---

# Recognizers

Each recognizer is responsible for identifying a particular category of token.

A recognizer processes the input character by character and determines whether the consumed sequence belongs to its language.

When a token is successfully recognized, the recognizer sends a notification to the `PrettyPrinter` through the listener interface.

Errors can also be reported when an input sequence does not correspond to a valid token.

---

## `RecAny`

`RecAny.java` implements the automaton described by `any.dot`.

It recognizes any word of length one.

When a character is successfully recognized, the recognizer emits:

```java
void any(String recognized)
```

---

## `RecABC`

`RecABC.java` implements the automaton described by `abc.dot`.

It recognizes words belonging to the language:

```text
a.(b.b*|c)
```

The recognizer emits:

```java
void abc(String recognized)
```

when a matching sequence is found.

---

## `RecCommand`

`RecCommand.java` implements the automaton described by `Command.dot`.

It recognizes commands enclosed by backticks:

```text
`command`
```

Line breaks are not allowed inside commands.

The recognizer emits:

```java
void command(String recognized)
```

for successfully recognized commands.

Invalid command sequences generate:

```java
void error(String consumed, String explanation)
```

---

## `RecIdent`

`RecIdent.java` implements the identifier recognizer described by `Ident.dot`.

An identifier:

* contains at least one character;
* can contain uppercase and lowercase letters;
* can contain `_`;
* can contain digits from `0` to `9`;
* cannot start with a digit.

Examples of valid identifiers:

```text
_
x
_ABC
myVariable
value42
```

Identifiers beginning with an uppercase letter are categorized as types.

For example:

```text
Object
Recognizer
```

The recognizer also identifies keywords such as:

```text
if
then
else
return
```

The corresponding notifications are:

```java
void ident(String recognized)
void keyword(String recognized)
void type(String recognized)
```

An input such as:

```text
1_x
```

is not recognized as a valid identifier because it starts with a digit.

---

## `RecString`

`RecString.java` implements the automaton described by `String.dot`.

It recognizes strings enclosed by double quotes:

```text
"hello world"
```

Line breaks are not allowed inside a string.

Escaped characters are supported, for example:

```text
"hello"
"line\n"
"quote: \""
"backslash: \\"
```

When a string is recognized, the recognizer notifies the `PrettyPrinter` using:

```java
void string(String recognized)
```

---

## `RecComment`

`RecComment.java` implements the automaton described by `Comment.dot`.

It recognizes C/Java-style comments.

### Multi-line comments

```java
/*
 * A multi-line comment
 */
```

### Single-line comments

```java
// A single-line comment
```

When a comment is recognized, the recognizer emits:

```java
void comment(String recognized)
```

The implementation handles the termination rules associated with both types of comments.

---

# Automata

The recognizers are based on **Finite State Automata**.

The automata are described using the **DOT** graph description language. This makes it possible to visualize the states and transitions graphically.

For example, an automaton can be represented conceptually as:

```text
             ┌───────────┐
             │           │
             ▼           │
        ┌─────────┐      │
        │  State  │──────┘
        └─────────┘
             │
             │ input
             ▼
        ┌─────────┐
        │  State  │
        └─────────┘
```

The DOT descriptions can be converted into images using Graphviz.

---

# Build System

The project uses a `Makefile` to automate the main operations.

## Display project information

```bash
make
```

This displays the main configuration and verifies that the required tools are available.

---

## Generate automaton images

```bash
make jpg
```

This generates images from the `.dot` automata descriptions using Graphviz.

---

## Compile the Java sources

```bash
make exec
```

This generates the `.class` files from the Java sources.

---

## Run the demonstration

```bash
make demo
```

The demonstration runs the tokenizer on the project README:

```bash
java -cp bin/ Recognizer.Tokenizer README.md
```

The tokenizer analyzes the file and the `PrettyPrinter` displays the recognized elements using different formatting and colors.

---

## Run tests

The project contains two groups of test files:

```text
TEST/
├── OK/
└── ERROR/
```

Valid inputs can be tested with:

```bash
make test
```

Error cases can be tested with:

```bash
make error
```

These tests verify both successful token recognition and error detection.

---

## Clean generated files

Generated files can be removed with:

```bash
make clean
```

This allows the project to be rebuilt from a clean state.

---

# Tokenizer and PrettyPrinter

The `Tokenizer` coordinates the different recognizers.

It reads the input and delegates the recognition of each token to the appropriate finite state automaton.

Once a token has been recognized, a notification is sent to the `PrettyPrinter`.

The `PrettyPrinter` is responsible for displaying the recognized elements with different colors depending on their category.

For example:

```text
Identifier    → identifier formatting
Keyword       → keyword formatting
Type          → type formatting
String        → string formatting
Comment       → comment formatting
Command       → command formatting
```

This architecture separates **recognition** from **presentation**.

The recognizers only focus on identifying tokens, while the `PrettyPrinter` handles their visual representation.

---

# Listener Architecture

The communication between recognizers and the `PrettyPrinter` is implemented using a listener-based architecture.

Conceptually:

```text
Recognizer
    │
    │ notification
    ▼
 Listener
    │
    ▼
PrettyPrinter
```

This allows recognizers to remain independent from the actual presentation logic.

For example, recognizing a string triggers:

```java
string(recognized)
```

while recognizing a comment triggers:

```java
comment(recognized)
```

The `PrettyPrinter` can then decide how each category should be displayed.

---

# Example

Given a source file containing:

```java
// Example
String message = "Hello world";

/*
 * Another comment
 */

if (message != null) {
    return message;
}
```

the tokenizer identifies the different components:

```text
Comment
Type
Identifier
String
Comment
Keyword
Identifier
Keyword
Identifier
```

The `PrettyPrinter` then uses these categories to display the source with syntax highlighting.

---

# Design

The project follows an object-oriented approach where each recognizer encapsulates the behavior of one finite state automaton.

This design makes it possible to:

* add new token types;
* implement each recognizer independently;
* describe automata separately from their Java implementation;
* reuse the listener mechanism;
* separate lexical analysis from presentation;
* test valid and invalid inputs independently.

The project therefore combines **finite state automata**, **object-oriented design**, **event/listener-based communication**, and **syntax highlighting** into a single tokenizer architecture.

---

## Author

Java project focused on **object-oriented finite state automata, lexical analysis and syntax highlighting**.

