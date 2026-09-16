```C
digraph AEF {

node [fontsize=6]

// == AEF NAME

  node [shape=plain]
    AEF [fontsize=10, fontcolor=blue, label="a.(b.b*|c)"]
 
// == STATE
  node [shape=circle]
  // node [label=""]

// == ACCEPTING STATES 
  node [peripheries=2]
    B
    C

// == NON ACCEPTING STATES
  node [peripheries=1]
    REJECT [style=filled, fillcolor=black, fontcolor=white]
    ENTRY
    A

// == INITIAL STATE
  edge [fontsize=12]
    AEF -> ENTRY
    
// == TRANSITIONS
  edge [fontcolor=green4]
    ENTRY -> A [label="'a'"]
    A, B -> B [label="'b'"]
    A -> C [label="'c'"]
    
// == NOT CONSUMED
  edge [color=gray, fontcolor=gray]

// == COMPLETION   
    ENTRY, A, B, C, REJECT -> REJECT [label="(_)"]

}
```
