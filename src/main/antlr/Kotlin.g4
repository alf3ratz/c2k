grammar Kotlin;

program: statement+;
statement: declaration | assignment | expression;
declaration: type IDENTIFIER ';';
assignment: IDENTIFIER '=' expression ';';
expression: IDENTIFIER | NUMBER | expression operator expression;
operator: '+' | '-' | '*' | '/';
type: 'Int' | 'Float' | 'Double';
IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]*;
NUMBER: [0-9]+;
WS: [ \t\r\n]+ -> skip;
