```C
digraph AEF {

node [fontsize=6]

// == AEF NAME
  node [shape=plain]
    AEF [fontsize=10, fontcolor=blue, label="String"]

// == STATE
  node [shape=circle]

// == ACCEPTING STATE 
  node [peripheries=2]
    ACCEPT [style=filled, fillcolor=green4, fontcolor=white]
    
// == NON ACCEPTING STATE
  node [peripheries=1]
    ENTRY
    BODY
    ESCAPE
    ERROR
    FORBIDDEN
    REJECT [style=filled, fillcolor=black, fontcolor=white]

// == ERROR STATE
  node [fontcolor=red]  
    ERROR [style=filled, fillcolor=red, fontcolor=white]

// == INITIAL STATE
  edge [fontsize=12]
    AEF -> ENTRY
    
// == TRANSITIONS
  edge [fontcolor=green4]
  ENTRY -> BODY [label="\""]
   BODY -> BODY [label="(_)"]

  BODY -> ESCAPE [label="\\"]
  BODY -> ACCEPT [label="\""]
  BODY -> FORBIDDEN [label="\\n"]

ESCAPE -> BODY [label="(_)"]
FORBIDDEN -> FORBIDDEN [label="(_)"]

FORBIDDEN -> ERROR [label ="\""]

// == NOT CONSUMED
  edge [color=gray, fontcolor=gray]

// == COMPLETION   
  ENTRY, REJECT-> REJECT [label="(_)"]
 
}
```
