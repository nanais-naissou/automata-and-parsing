```C
digraph AEF {

node [fontsize=6]

// == AEF NAME
  node [shape=plain]
    AEF [fontsize=10, fontcolor=blue, label="Comment"]

// == STATE
  node [shape=circle]

// == ACCEPTING STATE 
  node [peripheries=2]
    ACCEPT [style=filled, fillcolor=green4, fontcolor=white]
    
// == NON ACCEPTING STATE
  node [peripheries=1]
    REJECT [style=filled, fillcolor=black, fontcolor=white]
    ENTRY
    SLASH
    IN_COMMENT
    STAR
    LINE_COMMENT
    STRING_IN_COMMENT
    CHAR_IN_COMMENT
    ESCAPE_IN_STRING
    ESCAPE_IN_CHAR

// == INITIAL STATE
  edge [fontsize=12]
    AEF -> ENTRY
    
// == TRANSITIONS
  edge [fontcolor=green4]
  ENTRY -> SLASH [label="/"]
  
  SLASH -> LINE_COMMENT [label="/"]
  SLASH -> IN_COMMENT [label="*"]
  SLASH -> REJECT [label="other"]

  LINE_COMMENT -> LINE_COMMENT [label="any char except \\n"]
  LINE_COMMENT -> ACCEPT [label="\\n"]

  IN_COMMENT -> STAR [label="*"]
  IN_COMMENT -> STRING_IN_COMMENT [label="\""]
  IN_COMMENT -> CHAR_IN_COMMENT [label="\'"]
  IN_COMMENT -> IN_COMMENT [label="other chars"]

  STAR -> ACCEPT [label="/"]
  STAR -> STAR [label="*"]
  STAR -> STRING_IN_COMMENT [label="\""]
  STAR -> CHAR_IN_COMMENT [label="\'"]
  STAR -> IN_COMMENT [label="other chars"]

  STRING_IN_COMMENT -> ESCAPE_IN_STRING [label="\\"]
  STRING_IN_COMMENT -> IN_COMMENT [label="\""]
  STRING_IN_COMMENT -> STRING_IN_COMMENT [label="other chars"]

  ESCAPE_IN_STRING -> STRING_IN_COMMENT [label="any char"]

  CHAR_IN_COMMENT -> ESCAPE_IN_CHAR [label="\\"]
  CHAR_IN_COMMENT -> IN_COMMENT [label="\'"]
  CHAR_IN_COMMENT -> CHAR_IN_COMMENT [label="other chars"]

  ESCAPE_IN_CHAR -> CHAR_IN_COMMENT [label="any char"]

// == NOT CONSUMED
  edge [color=gray, fontcolor=gray]
  ENTRY -> REJECT [label="other"]
  SLASH -> REJECT [label="other"]

}

```
