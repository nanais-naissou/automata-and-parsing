```C
digraph AEF {

node [fontsize=6]

// == AEF NAME
  node [shape=plain]
    AEF [fontsize=10, fontcolor=blue, label="Ident"]

// == STATE
  node [shape=circle]

// == ACCEPTING STATE 
  node [peripheries=2]
    ACCEPT  [style=filled, fillcolor=green4, fontcolor=white]
    A  [style=filled, fillcolor=grey, fontcolor=white]

    
// == NON ACCEPTING STATE
  node [peripheries=1]
    REJECT [style=filled, fillcolor=black, fontcolor=white]
    ENTRY

/*// == ERROR STATE
  node [fontcolor=red]  
    ERROR [style=filled, fillcolor=red, fontcolor=white]*/

// == INITIAL STATE
  edge [fontsize=12]
    AEF -> ENTRY
    
// == TRANSITIONS
  edge [fontcolor=green4]
    ENTRY -> A [label="['a'-'Z'], '_'"]
    ENTRY -> REJECT [label="[0-9]"]
    A -> A [label="['a'-'Z'], ['0'-'9],'_'"]
    A -> ACCEPT [label= "(_)"]



// == NOT CONSUMED
  edge [color=gray, fontcolor=gray]

// == COMPLETION   
    ENTRY, REJECT -> REJECT [label="(_)"] 
}
```
