grammar C;

program: statement+;

statement: declaration
         | assignment
         | expression
         | block
         ;

block: '{' statement* '}';

declaration: type IDENTIFIER '=' expression ';';  // Объявление с присваиванием

assignment: IDENTIFIER '=' expression ';';  // Присваивание

expression: IDENTIFIER
          | NUMBER
          | BOOLEAN
          | '(' expression ')'
          | expression operator expression
          | '!' expression  // Унарный оператор NOT
          ;

operator: '+' | '-' | '*' | '/'              // Операторы для выражений
        | '&&' | '||' | '==' | '!='          // Логические операторы
        | '<' | '<=' | '>' | '>=';           // Операторы сравнения

type: 'int' | 'float' | 'double' | 'boolean';  // Типы данных

BOOLEAN: 'true' | 'false';  // Логические значения

IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]*;  // Имя переменной

NUMBER: [0-9]+;  // Числовое значение

WS: [ \t\r\n]+ -> skip;  // Пропускаем пробельные символы
