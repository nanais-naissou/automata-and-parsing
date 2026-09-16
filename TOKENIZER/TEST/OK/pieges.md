<MD: author="M.Périn" date=1/11/2025 lang=fr>

<SHELL>
# COMMAND
1. a `command`
2. `the string mark " in a command`
3. `the comment marks */ and /* in a command`
4. error: `a command which contains
    a newline`
5. followed by a `valid command`
</SHELL>

<JAVA: version=11>
# COMMENTS
1. /* comment */
2. /** a 
    * comment 
    * on 
    * multiple 
    * lines
    **/  
3. /* a "string" in a comment */
4. /* a comment that contains an inactive "*/" hidden in a string */
5. /* a comment that contains an inactive `*/` hidden in a command */

# SINGLE LINE COMMANTS
1. // a single line comment
2. /////
3. //
4. // /*
5. // */
6. // "
7. // '

# STRING      
1. "a string"
2. "a unterminated comment /* in a string"
3. "a comment closing mark */ in a string"
4. "a command mark ` in a string" 
5. error: "a string which contains
    a newline"
6. followed by a "valid string"
7. "escape chars \n \\ \b and \" in a string"
8. error: "escape chars after 
9 newline \n \\ \b and \" in a string"
10. followed by a "valid string"
</JAVA>

</MD>
// LAST LINE