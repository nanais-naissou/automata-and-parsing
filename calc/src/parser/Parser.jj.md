#BEGIN

##### LICENCE, AUTHOR, INSTITUTION, DATE

* GPL
* Michaël PÉRIN, 
* VERIMAG / Univ. Grenoble-Alpes / INP Polytech Grenbole
* December 2025


# Un évaluateur d'expressions arithmétiques

L'objectif du TP est de réaliser un équivalent de l'outil `bc` (_basic calculator_) qui fonctionne en ligne de commande et effectue des calculs arithmétiques simples.

### Déroulé du TP

> __Un parser est difficile à mettre au point__

* il est difficile à déboguer avec les outils standard de debug du fait des appels récursifs en cascade
* pour faire la mise au point, on trace les appels aux non-terminaux et on affiche les tokens reconnus par la partie lexer ; d'où les appels à la classe `Tracing`.

Il faut donc procéder pas à pas pour maîtriser et valider chaque extension du parser.
Je vous conseille donc de procéder en plusieurs étapes.

#### étape 1 : obtenir un parser minimal qui compile

Je vous fournis un parser qui ne reconnaît pas grand chose mais qui fonctionne.

#### étape 2* : complétez les non-terminaux

Certains non-terminaux (par exemple, `EXPR0`) ne sont pas complètement définis.
Répétez le procédé suivant :
  * On réfléchit à la grammaire (sur papier, pas directement dans le code `.jj.md`)
  * On ajoute __une__ alternative manquante et les actions correspondantes 
  * On teste avant d'ajouter une autre alternative. Pour cela ajoutez vos tests dans le répertoire `TEST/` 

> __IMPORTANT__ Vos tests font partie du rendu du projet. 

#### étape 3 : corrigez la grammaire et le parser en procédant par étapes

Vous constaterez que l'évaluation de l'expression `3 * 2 + 5` retourne `30` au lieu de `11`
car la priorité des opérateurs n'est pas prise en compte.

Vous devez modifier la grammaire des expressions pour tenir compte de la priorité des opérateurs 
`*, %, /` par rapport aux opérateurs `+, -`. 


## _Parser_ en JavaCC 

Un _parser_ en JavaCC se compose de 3 parties :

1. Une classe java `Parser`
2. Un lexer défini par une collection d'expressions régulières
3. Un analyseur syntaxique (_parser_ en anglais) défini par une collection de fonctions de reconnaissance


## JAVACC OPTIONS

```Java
options {
  STATIC = false;
}
```
## PARTIE JAVA du parser

```Java
PARSER_BEGIN(Parser)
```

### CLASS `Parser`


```Java
package parser;

import java.io.BufferedReader;
import java.io.FileReader;

public class Parser {
```

#### CONSTRUCTEUR

Le constructeur de la classe `Parser` sera généré automatiquement par `javacc` : il prend en argument un flot de caractères qui sera passé au `lexer` qui à son tour fournira un flot de tokens aux fonctions de _parsing_.

#### CHAMPS

On aura besoin d'un tableau `memory` servira à stocker les valeurs des variables entières `$0` à `$9` du langage CALC.

```Java
  static int memory[];

  static {
    memory = new int[10];
  } 
```

#### MÉTHODES / FONCTIONS

* Une grande partie des méthodes de la classe `Parser` sont des _parsers_ qui correspondent aux non-terminaux de la grammaire. Elles sont générées 
automatiquement par `javacc` à partir de la description des non-terminaux (voir ci-dessous).

* On peut compléter la classe `Parser` avec des méthodes écrites directement par le développeur que l'outil `javacc` conserve, sans les modifier.

##### La fonction `main`

* l'option `-e` évalue le calcul donné en ligne de commande : 
  `Parser -e "41+2"`
* l'option `-f` évalue le calcul donnée dans une fichier : `Parser -f test0.calc`

* l'option `-v` active le mode _verbose_ où le parser affiche les noms terminaux par lesquels il passe lors de son analyse de texte. 


* `main` appelle la méthode `parse()` qui se charge d'effectuer simultanément la reconnaissance et l'évaluation de l'expression parsée.

```Java
  public static void main(String[] args) throws Exception {
    int result = 314159;
    Parser parser = null;
    boolean verbosity = false;
 
    for(int i = 0 ; i < args.length ; i++){    
      if (args[i].equals("-v"))
        verbosity = true ;

      if (args[i].equals("-e") && i+1 < args.length) {
        String input_string = args[i+1];
        parser = new Parser(new java.io.StringReader(input_string));
      }

      if (args[i].equals("-f") && i+1 < args.length) {
        String path_file = args[i+1];
        parser = new Parser(new BufferedReader(new FileReader(path_file)));
      }
    }

    if (parser == null)
      throw new IllegalArgumentException("missing arguments");

    Tracing.enable(verbosity);
    result = parser.parse();
    System.out.print(String.format("\n%d\n", result)); 
  }
```

##### La fonction `int horner(String digits)`

* prend en entrée une séquence de digits sous la forme d'une `String`
* retourne l'entier correspondant
* applique le schéma de Horner pour construire efficacement l'entier
* `horner("00123") == 123`

```Java
  public static int horner(String digits){
    if (digits.isEmpty())
      throw new IllegalArgumentException("Parser.horner: empty string");
    int r = 0 ;
    for(int i = 0 ; i < digits.length() ; i++)
      r = 10 * r + (digits.charAt(i) - '0') ;
    return r;
  }
```

```Java
  public static int fact(int n) {
    if (n < 0) {
        throw new IllegalArgumentException("n<0");
    }
    if (n == 0 || n == 1) {
        return 1;
    }
    return n * fact(n - 1);
  }
  ```


```Java
  public static int logDiv(int n, int b) {
    int count = 0;
    while (n >= b) {
        n = n / b;
        count++;
    }
    return count;
  }
```
```Java
} // end of class Parser

PARSER_END(Parser) // end of the Java part
```


## PARTIE LEXER

Le lexer définit des reconnaisseurs sous la forme d'expressions régulières (RegExp en abbrégée et en anglais) qui sont traduites en automates.

* les mots à ignorer et 
* les lexèmes (_Tokens_ en anglais) 

Les déclarations de reconnaisseurs

* doivent respecter la syntaxe suivante  `<` _Catégorie de token_ `:` _RegExp_`> {` _action Java optionnelle_ `}` 
* sont précédées du mot-clef `SKIP` ou `TOKEN`


##### Fonctionnement

Le `lexer` fait tourner les automates dans l'ordre de déclaration.

* Chaque fois qu'un automate reconnaît un mot, le `lexer` génère un object `Token` contenant 
  * un champ contenant la catégorie du _token_
  * un champ `String image` : le mot reconnu qu'on peut récupérer au moyen de la méthode `toString()`


##### SKIP

Déclarations des mots à ignorer

```Java
SKIP: 
{  " " | "\n"}
```

##### TOKEN

* Il est possible mais pas nécessaire de déclarer un automate pour les mots utilisés une seule fois dans la grammaire tels que `$`, `:=`, `;`, `(`, `)`, `eval`.

* On le fait pour `$`, `:=`, `;` et `eval` afin de pouvoir changer facilement la notation si on le souhaite en `#`, `=`, `,` et `return`.


```Java
TOKEN:
{
  < DIGIT: ["0"-"9"] > { Tracing.token("DIGIT", image.toString()); } 
}

TOKEN:
{ 
  < NAT: (<DIGIT>)+ > { Tracing.token("NAT", image.toString()); } 
}

TOKEN:
{
  < ASSIGN: ":=" > { Tracing.token("ASSIGN", image.toString()); }
}

TOKEN:
{ 
  < MINUS: "-" > { Tracing.token("MINUS", image.toString()); } 
}

TOKEN:
{ 
  < ADD: "+"   > { Tracing.token("ADD", image.toString()); } 

}
TOKEN:
{ 
  < MULT: "*" |"/"   > { Tracing.token("MULT", image.toString()); } 

}
TOKEN:

{ 
  < FUN: "mod" |"log"   > { Tracing.token("FUN", image.toString()); } 
}
TOKEN:
{
  < VAR: "$" > { Tracing.token("VAR", image.toString()); }
}

TOKEN:
{
  < SEP: ";" > { Tracing.token("SEP", image.toString()); }
}

TOKEN:
{
  < EVAL: "eval" > { Tracing.token("EVAL", image.toString()); }
}

TOKEN:
{
  < POSTFIX: "²"|"!" > { Tracing.token("POSTFIX", image.toString()); }
}
TOKEN:
{
  < OP: "(" > { Tracing.token("OP", image.toString()); }
}
TOKEN:
{
  < CP: ")" > { Tracing.token("CP", image.toString()); }
}
```


## PARTIE PARSER = Grammar + Actions Java

##### Germe de la grammaire = Non-Terminal PARSE = méthode `parse()` 

```Haskell
PARSE ::= CALC . <EOF> 
``` 

La méthode `int parse()`

* lit le flot de caractères issu du fichier jusqu'à rencontrer le token `<EOF>` prédéfini, acronyme de End Of File (en anglais) et indiquant la fin du fichier.
* retourne la valeur entière qui résulte de l'évaluation de l'expression parsée

```Java
int parse() ::= 
DECL{ 
  // déclarations des variables
  int depth = 0;
  Tracing.call(depth, "parse"); // <-- affichage avec une indentation
  int i; 
}
RULE{ 
  // production/reconnaissance /reconnaissance du non-terminal PARSE
  i = CALC(depth+1) // <-- appel du non-terminal CALC avec indentation
  <EOF>
  { // actions java
    Tracing.returns(0, "parse", String.format("%d",i)); // <-- affichage du résultat
    return i; 
  }
}
```

##### Non-Terminal DIGIT = méthode `DIGIT(int depth)`

```Haskell
DIGIT ::= <DIGIT>
```

Ne pas confondre la méthode `DIGIT` et le token `<DIGIT>`

La méthode `int DIGIT(int depth)`

* prend en paramètre un entier `depth` qui correspond à la profondeur de l'appel
* `depth` est utilisé pour afficher l'appel avec une indentation correspondante
* consomme un token `<DIGIT>`, le convertit en `String` puis en entier
* si le token n'est pas un entier &in; [0..9], 
  * la fonction ne doit __surtout pas__ rendre une valeur entière : 
    ce qui serait une source de bug, c'est à dire une erreur silencieuce 
    qui ne se détecte pas immédiatement.
  * on préferera provoquer un erreur explicite en levant une exception.

```Java
int DIGIT(int depth) ::=
DECL{ 
  Tracing.call(depth, "DIGIT"); 
  Token token;
}
RULE{
  token = <DIGIT> 
  { // action java
    switch( token.toString() ){
      case "0" : return 0;
      case "1" : return 1; 
      case "2" : return 2; 
      case "3" : return 3;
      case "4" : return 4; 
      case "5" : return 5;
      case "6" : return 6; 
      case "7" : return 7; 
      case "8" : return 8;
      case "9" : return 9 ;
      default: throw new IllegalStateException("unknown digit");
    } 
  }
}
```


##### Non-Terminal NAT = méthode `NAT(int depth)`

```Haskell
NAT ::= <DIGIT> | <NAT>
```

Ne pas confondre la méthode `NAT` et le token `<NAT>`

La méthode `int NAT(int depth)`

* consomme un token `<NAT>` dans le flot de tokens
* le convertit en chaîne de caractères
* puis convertit la chaîne obtenue (formée de digits) en un entier naturel

```Java
int NAT(int depth) ::= 
DECL{ 
  Tracing.call(depth,"NAT"); 
  Token token;  
  int n;
}
RULE{ 
  ( 
    n = DIGIT(depth+1)
  | 
    token = <NAT> { n = horner(token.toString()); }
  )
  { // action java commune aux deux alternatives
    Tracing.returns(depth, "NAT", String.format("%d", n));
    return n; 
  }
}
```


##### Non-Terminal INTEGER = méthode `INTEGER(int depth)`

```Haskell 
INTEGER ::= <MINUS> . NAT | NAT 
```

* Le parseur `INTEGER` réussit lorsque
  * soit le flot de tokens contient un token `<MINUS>` suivi d'un préfixe reconnu par le parseur `NAT` 
  * soit le flot de tokens contient un préfixe reconnu par le non-terminal `NAT`
* il retourne un entier signé


```Java
int INTEGER(int depth) ::= 
DECL{
  Tracing.call(depth,"INTEGER");
  int n,i;  
}
RULE{
  // [1]
  <MINUS>
  n = NAT(depth+1)

  { // action java
    i = -n;
    Tracing.returns(depth, "INTEGER[1]", String.format("%d",i));
    return i;
  }

| // <-- alternative

  // [2]
  n = NAT(depth+1)

  { // action java
    Tracing.returns(depth, "INTEGER[2]", String.format("%d",n));
    return n ;
  }
}
```


##### Non-Terminal EXPR = méthode `EXPR(int depth)`

```Haskell 
EXPR ::= EXPR0 . opt_BINOP_EXPR
```

La méthode `int EXPR(int depth)`

* reconnaît une expression de niveau 0 suivie 
  éventuellement d'un opérateur binaire et d'une expression
* retourne la valeur de l'expression complète 

```Java
int EXPR(int depth) ::=
DECL{
  Tracing.call(depth,"EXPR");
  int l, r;
}
RULE{
  l = EXPR0(depth+1)
  r = Opt_BINOP_EXPR(depth+1, l)
  {
    Tracing.returns(depth, "EXPR", String.format("%d",r));
    return r ;
  }
}
```

##### Non-Terminal EXPR0 = méthode `EXPR0(int depth)`

```Haskell 
EXPR0 ::=
  | EXPR1 . Opt_MULT_EXPR0
```

La méthode `int EXPR0(int depth)`

* reconnaît une expression de niveau 0
* retourne la valeur de l'expression

```Java
int EXPR0(int depth) ::=
DECL{
  Tracing.call(depth, "EXPR0");
  int i,j;
}
RULE{ // FIXME TODO
  // [1]
  i = EXPR1(depth+1)
  j= Opt_MULT_EXPR0(depth+1, i)
  {
    Tracing.returns(depth, "EXPR0[1]", String.format("%d",j));
    return j; 
  }
}
```

##### Non-Terminal EXPR1 = méthode `EXPR1(int depth)`

```Haskell 
EXPR1 ::=
  | EXPR2 . Opt_FUN_EXPR1
```

```Java
int EXPR1(int depth) ::=
DECL{
  Tracing.call(depth, "EXPR1");
  int i,j;
}
RULE{ // FIXME TODO
  // [1]
  i = EXPR2(depth+1)
  j= Opt_FUN_EXPR1(depth+1, i)
  {
    Tracing.returns(depth, "EXPR1[1]", String.format("%d",j));
    return j; 
  }
}
```



##### Non-Terminal EXPR2 = méthode `EXPR2(int depth)`

```Haskell 
EXPR2 ::=
  | EXPR3 . Some_POSTFIX
```

```Java
int EXPR2(int depth) ::=
DECL{
  Tracing.call(depth, "EXPR2");
  int i,j;
}
RULE{ // FIXME TODO
  // [1]
  i = EXPR3(depth+1)
  j= Some_POSTFIX(depth+1, i)
  {
    Tracing.returns(depth, "EXPR2[1]", String.format("%d",j));
    return j; 
  }
}
```

#### Non-Terminal EXPR3 = méthode `EXPR3(int depth)`

```Haskell 
EXPR3 ::=
  | <MINUS> . EXPR3
  | ATOM
```

```Java
int EXPR3(int depth) ::=
DECL{
  Tracing.call(depth, "EXPR3");
  int i,j;
  Token t; 
}
RULE{ // FIXME TODO
  // [1]
  <MINUS>
  j= EXPR3(depth+1)
  {
    Tracing.returns(depth, "EXPR3[1]", String.format("%d",j));
    return -j; 
  }
  |
  i=ATOM(depth+1)
   {
    Tracing.returns(depth, "EXPR3[2]", String.format("%d",i));
    return i; 
  }

}
```




#### Non-Terminal ATOM = méthode `ATOM(int depth)`

```Haskell 
ATOM ::=
  | "(" . EXPR . ")"
  | INTEGER
  | VARIABLE

```

```Java
int ATOM(int depth) ::=
DECL{
  Tracing.call(depth, "ATOM");
  int i,j,k;
  Token t1,t2; 
}
RULE{ // FIXME TODO
  // [1]
  t1= <OP>
  j= EXPR(depth+1)
  t2=<CP>
  {
    Tracing.returns(depth, "ATOM[1]", String.format("%d",j));
    return j; 
  }
  |
  i=INTEGER(depth+1)
   {
    Tracing.returns(depth, "ATOM[2]", String.format("%d",i));
    return i; 
  }
  |
  k=VARIABLE(depth+1)
    {
    Tracing.returns(depth, "ATOM[3]", String.format("%d",memory[k]));
    return memory[k];
     }

}
```



##### Non-Terminal BINOP = méthode `BINOP(int depth)`

&Lscr;(`<BINOP>`) = { `-` , `+` , `*` , `/` , `mod`, `%`, `log` }

```Haskell 
BINOP ::= <MINUS> | <ADD> 
```

Ne pas confondre la méthode `BINOP` et le token `<BINOP>`

La méthode `BINOP(int depth)`

* reconnait un opérateur binaire
* retourne la chaîne de caractères correspondante

```Java
String BINOP(int depth) ::=
DECL{ 
  Tracing.call(depth,"BINOP");
  Token token;
}
RULE{
  (
    token = <MINUS>
  |  
    token = <ADD>

  )
  { // action java commune aux deux alternatives
    Tracing.returns(depth, "BINOP", String.format("%s",token.toString()));
    return token.toString();
  }  
}
```


##### Non-Terminal Opt_BINOP_EXPR = méthode `Opt_BINOP_EXPR(int depth, int l)`

```Haskell 
Opt_BINOP_EXPR ::= 
  | BINOP . EXPR
  | ε
```

La méthode `Opt_BINOP_EXPR(int depth, int l)`

* prend en paramètre `l` = la valeur de l'expression à gauche de l'opérateur 
* reconnaît un opérateur binaire, suivi d'une expression à droite de l'opérateur (qui s'évaluera en `r`)
* retourne la valeur de `l` « opérateur » `r`
* accepte &epsilon; et dans ce cas retourne l 


```Java
int Opt_BINOP_EXPR(int depth, int l) ::=
DECL{
  Tracing.call(depth,"Opt_BINOP_EXPR"); 
  String o;
  int r, i;
}
RULE{
  // [1] BINOP . EXPR
  o = BINOP(depth+1)
  r = EXPR(depth+1)
  {
    switch( o ){
      case "+"  : i = l + r ; break ;
      case "-"  : i = l - r ; break ;
      default: throw new IllegalStateException("unknown binary operator") ;
  	}
    Tracing.returns(depth, "Opt_BINOP_EXPR[1]", String.format("%d",i));
    return i ;
  }

| // <-- alternative

  // [2] ε
  /*ε*/
  {
    Tracing.returns(depth, "Opt_BINOP_EXPR[2]", String.format("%d",l));
    return l ; 
  }
}
```



```Haskell 
Opt_MULT_EXPR0 ::= 
  | <MULT>. EXPR0
  | ε
```

```Java
int Opt_MULT_EXPR0(int depth, int l) ::=
DECL{
  Tracing.call(depth,"Opt_MULT_EXPR0"); 
  Token o;
  int r, i;
}
RULE{
  o = <MULT>
  r = EXPR0(depth+1)
  {
    switch( o.toString() ){
      case "*"  : i = l * r ; break ;
      case "/"  : i = l / r ; break ;
      default: throw new IllegalStateException("unknown mult operator") ;
  	}
    Tracing.returns(depth, "Opt_MULT_EXPR0[1]", String.format("%d",i));
    return i ;
  }

| // <-- alternative

  // [2] ε
  /*ε*/
  {
    Tracing.returns(depth, "Opt_MULT_EXPR0[2]", String.format("%d",l));
    return l ; 
  }
}
```



```Haskell 
Opt_FUN_EXPR1::=                 //for mod and log
  | <FUN> . EXPR1
  | epsilon
```

```Java
int Opt_FUN_EXPR1(int depth, int l) ::=
DECL{
  Tracing.call(depth,"Opt_FUN_EXPR1"); 
  Token o;
  int r, i;
}
RULE{
  // [1] BINOP . EXPR
  o = <FUN>
  r = EXPR1(depth+1)
  {
    switch( o.toString() ){
      case "mod"  : i = l % r ; break ;
      case "log"  : i = logDiv(l,r) ; break ;
      default: throw new IllegalStateException("unknown fun operator") ;
  	}
    Tracing.returns(depth, "Opt_FUN_EXPR1[1]", String.format("%d",i));
    return i ;
  }

| // <-- alternative

  // [2] ε
  {
    Tracing.returns(depth, "Opt_FUN_EXPR1[2]", String.format("%d",l));
    return l ; 
  }
}
```





La méthode `Some_POSTFIX(int depth, int l)`


```Haskell 
Some_POSTFIX ::=                        //for power and ! 
  | <POSTFIX> . Some_POSTFIX
  | epsilon
```



```Java
int Some_POSTFIX(int depth, int l) ::=
DECL{
  Tracing.call(depth,"Some_POSTFIX"); 
  Token t;
  int r;
}
RULE{
  t = <POSTFIX>
  { 
    switch( t.toString() ){
      case "²" : r = l * l ; break ;
      case "!" : r = fact(l) ; break ;
      default : throw new IllegalStateException("unknown postfix") ;
    }
  }
  r = Some_POSTFIX(depth+1, r) 
  { 
    Tracing.returns(depth, "Some_POSTFIX[1]", String.format("%d",r));
    return r ;
  }

| 
  {
    Tracing.returns(depth, "Some_POSTFIX[2]", String.format("%d",l));
    return l ; 
  }
}
```

##### Non-Terminal VARIABLE = méthode `VARIABLE(int depth)`

```Haskell
VARIABLE ::= <VAR> . DIGIT 
```

La méthode `int VARIABLE(int depth)`

* reconnaît un nom de variable
* retourne __le numéro__ de variable

```Java
int VARIABLE(int depth) ::= 
DECL{ 
  Tracing.call(depth,"VARIABLE");
    int i; 

}
RULE{ // TODO
  <VAR>
  i = DIGIT(depth+1)
  { Tracing.returns(depth, "VARIABLE", String.format("%d",i));
    return i ;
  }
}
```


##### Non-Terminal ASSIGN = méthode `ASSIGN(int depth)`

```Haskell 
ASSIGN ::= VARIABLE . <ASSIGN> . EXPR 
```

Ne pas confondre la méthode `ASSIGN` avec le token `<ASSIGN>`

La méthode `ASSIGN(int depth)`

* reconnaît une affectation de variable
* met à jour la valeur de la variable dans le tableau memory
* __ne retourne rien__

###### Remarque 1

On pourrait faire un autre choix : 
Les langages impératifs tels que `C, Java` choississent de retourner la valeur affectée.

Ainsi une instruction d'affectation __est__ une valeur ; et on peut écrire en `C, Java` : `x=y=z=0;` qui est évaluée comme 
`x=(y=(z=0))` où `z=0` vaut `0` et donc s'évalue en `x=(y=0)` où `y=0` vaut `0` et finalement s'évalue en `x=0` mettant ainsi les 3 variables à `0`.  

Pour simplifier, on n'adopte pas la convention choisie par `C, Java`, mais vous pourrez tenter de le faire en fin de TP si ça vous chante.

```Java
void ASSIGN(int depth) ::= 
DECL{ 
  Tracing.call(depth,"ASSIGN");
    Token t;
    int i,j; 

}
RULE{ // TODO
  i= VARIABLE(depth+1)
  <ASSIGN>
  j = EXPR(depth+1)
  { Tracing.returns(depth, "ASSIGN", String.format("$%d",j));
    memory[i]=j ;
  }
}
```


##### Non-Terminal SEQUENCE = méthode `SEQUENCE(int depth)`

```Haskell
SEQUENCE ::= (ASSIGN . <SEP>)* 
```

* utilise l'étoile de Kleene définie en JavaCC
* reconnaît une suite (éventuellement vide) d'affectations
* __ne retourne rien__
  

```Java
void SEQUENCE(int depth) ::=
DECL{
  Tracing.call(depth,"SEQUENCE");

}
RULE{
  
  (  ASSIGN(depth+1)
    <SEP>
    { })*
  
}

```


##### Non-Terminal CALC = méthode `CALC(int depth)`

```Haskell 
CALC ::= SEQUENCE . <EVAL> . EXPR 
```

La méthode `CALC(int depth)`

* reconnaît une séquence d'affectations suivie d'une expression
* retourne le résultat de l'évaluation de l'expression parsée 

```Java
int CALC(int depth) ::=
DECL{ 
  Tracing.call(depth,"CALC");
  int i;
}
RULE{ // FIXME: non conforme à la grammaire

 SEQUENCE(depth+1)
  <EVAL>
  i = EXPR(depth+1)   
  { 
    Tracing.returns(depth, "CALC", String.format("%d",i));
    return i; 
  }
  
}
```

#END