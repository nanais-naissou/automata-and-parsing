# CALC — Expression Parser and Evaluator

CALC is a Java project that implements a parser and evaluator for arithmetic expressions using **JavaCC**.

The project defines a grammar with JavaCC, which is then used to generate the Java parser responsible for recognizing and evaluating expressions.

---

## Technologies

* **Java**
* **JavaCC 7.0.12**
* **Make**
* **Eclipse**

---

## JavaCC

[JavaCC](https://javacc.github.io/javacc/) (Java Compiler Compiler) is used to generate the parser from the grammar defined in `Parser.jj`.

The project uses:

```text
Java Compiler Compiler Version 7.0.12
```

JavaCC generates the Java source files required by the parser, including:

```text
TokenMgrError.java
ParseException.java
Token.java
SimpleCharStream.java
```

---

## Project Structure

The project is organized as follows:

```text
CALC/
├── src/
│   └── parser/
│       ├── Parser.jj
│       └── ...
├── TEST/
│   ├── test00.calc
│   ├── test01.calc
│   ├── test02.calc
│   └── test03.calc
├── bin/
├── Makefile
└── README.md
```

### Main components

* `Parser.jj` contains the JavaCC grammar.
* `src/parser/` contains the parser source files.
* `TEST/` contains sample `.calc` files used to test the parser.
* `bin/` contains the generated and compiled Java classes.
* `Makefile` automates the generation, compilation and execution steps.

---

## Compilation

The project uses a `Makefile` to automate the main build operations.

Running:

```bash
make
```

displays the main project configuration and paths, including:

```text
pwd       = .../CALC
PROJECT   = .../CALC
CP        = .../CALC/bin
SOURCE    = .../CALC/src/parser
main      = parser.Parser
EXEC      = ../CALC/bin/parser/Parser.class
TEST      = .../CALC/TEST
```

---

## Parser Generation and Demo

The following command generates the parser from `Parser.jj` and runs a demonstration:

```bash
make demo
```

JavaCC first processes the grammar:

```text
Reading from file Parser.jj . . .
Parser generated successfully.
```

The generated parser can then be compiled and executed.

---

## Example

The parser can evaluate expressions such as:

```text
eval 1 + 2
```

During execution, the parser displays a trace of the syntax analysis:

```text
eval 1 + 2
parse
| CALC <EVAL:"eval">
| | EXPR
| | | EXPR0
| | | | INTEGER <DIGIT:"1">
| | | | | NAT
| | | | | | DIGIT
| | | | | ↳NAT: 1
| | | | ↳INTEGER[2]: 1
| | | ↳EXPR0[1]: 1
| | | Opt_BINOP_EXPR <BINOP:"+">
| | | | BINOP
| | | | ↳BINOP: +
| | | | EXPR
| | | | | EXPR0
| | | | | | INTEGER <DIGIT:"2">
| | | | | | | NAT
| | | | | | | | DIGIT
| | | | | | | ↳NAT: 2
| | | | | | ↳INTEGER[2]: 2
| | | | | ↳EXPR0[1]: 2
| | | | | Opt_BINOP_EXPR
| | | | | ↳Opt_BINOP_EXPR[2]: 2
| | | | ↳EXPR: 2
| | | ↳Opt_BINOP_EXPR[1]: 3
| | ↳EXPR: 3
| ↳CALC: 3
↳parse: 3
```

The expression is evaluated to:

```text
3
```

The trace makes it possible to follow the different stages of the parsing process, including the recognition of integers, operators and expressions.

---

## Tests

The `TEST/` directory contains several `.calc` files used to test the parser:

```text
test00.calc
test01.calc
test02.calc
test03.calc
```

These files provide different input expressions for checking the parser and evaluator.

---

## Development Environment

The project can be opened and developed using **Eclipse** as a Java project.

The `Makefile` handles the JavaCC generation and compilation steps, while Eclipse can be used to edit and inspect the Java source code and the grammar.

---

## How It Works

The overall process can be summarized as follows:

```text
        .calc input
             │
             ▼
          Parser.jj
             │
             │ JavaCC
             ▼
      Generated Java Parser
             │
             ▼
      Syntax Analysis
             │
             ▼
     Expression Evaluation
             │
             ▼
           Result
```

The grammar defined in `Parser.jj` is processed by JavaCC to generate the Java parser. The generated parser then analyzes the input according to the grammar rules and evaluates the resulting expression.

---


