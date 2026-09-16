# Grammaire des calculs reconnus par CALC

##### CALCUL

```Haskell
PARSE ::= CALC . <EOF> 

CALC ::= SEQUENCE . <EVAL> . EXPR 

SEQUENCE ::= (ASSIGN . <SEP>)* 

ASSIGN ::= VARIABLE . <ASSIGN> . EXPR                  //<ASSIGN> here is :=

VARIABLE ::= <VAR> . DIGIT 
```

##### VALUES

```Haskell
DIGIT ::= <DIGIT>

NAT ::= <DIGIT> | <NAT>

INTEGER ::= <MINUS> . NAT | NAT 
```

##### EXPRESSIONS : partie à modifier / compléter

```Haskell
BINOP ::= <MINUS> | <ADD> 

EXPR ::= EXPR0 . Opt_BINOP_EXPR

EXPR0 ::=
  | EXPR1 . Opt_MULT_EXPR0


EXPR1 ::=
  | EXPR2 . Opt_FUN_EXPR1


EXPR2 ::=
  | EXPR3 . Some_POSTFIX

EXPR3 ::=
  | <MINUS> . EXPR3
  | ATOM


ATOM ::=
  | "(" . EXPR . ")"
  | INTEGER
  | VARIABLE


Opt_BINOP_EXPR ::= 
  | BINOP . EXPR
  | epsilon


Opt_MULT_EXPR0 ::=
  | <MULT> . EXPR0
  | epsilon

Opt_FUN_EXPR1::=                 //for mod and log
  | <FUN> . EXPR1
  | epsilon

Some_POSTFIX ::=                        //for power and ! 
  | <POSTFIX> . Some_POSTFIX
  | epsilon
``` 

