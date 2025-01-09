grammar C;

program: statement+;

statement: declaration
         | assignment
         | expression
         | block
         | whileLoop
         | forLoop
         ;

block: '{' statement* '}';

declaration: type IDENTIFIER '=' expression ';';

assignment: IDENTIFIER '=' expression ';';

expression: IDENTIFIER
          | NUMBER
          | BOOLEAN
          | '(' expression ')'
          | expression operator expression
          | '!' expression
          ;

operator: '+' | '-' | '*' | '/'
        | '&&' | '||' | '==' | '!='
        | '<' | '<=' | '>' | '>=';

type: 'int' | 'float' | 'double' | 'boolean';

BOOLEAN: 'true' | 'false';

IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]*;

NUMBER: [0-9]+;

WS: [ \t\r\n]+ -> skip;

whileLoop: 'while' '(' expression ')' '{' statement* '}';

forLoop: 'for' '(' (declaration | assignment | expression)? ';' expression? ';' expression? ')' statement;