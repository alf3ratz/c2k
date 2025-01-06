grammar C;

program: statement+;

statement: declaration
         | assignment
         | expression
         ;

declaration: type IDENTIFIER '=' expression ';';  // Объявление с присваиванием

assignment: IDENTIFIER '=' expression ';';  // Присваивание

expression: IDENTIFIER
          | NUMBER
          | '(' expression ')'
          | expression operator expression;  // Операции с выражениями

operator: '+' | '-' | '*' | '/';  // Операторы для выражений

type: 'int' | 'float' | 'double';  // Типы данных

IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]*;  // Имя переменной

NUMBER: [0-9]+;  // Числовое значение

WS: [ \t\r\n]+ -> skip;  // Пропускаем пробельные символы
