grammar Ryoko;
program
    : line+ EOF
    ;

line
    : statement
    ;

statement
    : varDeclaration';'
    | systemLib';'
    ;

varDeclaration
    : TYPE ':' IDENTIFIER '=' expr
    ;

systemLib
    : SHOW '(' expr ')'
    ;

expr
    : IDENTIFIER
    | INTEGER
    | STRING
    ;

SHOW : 'System.show';

TYPE: ('int' | 'string');
STRING : ["] ( ~["\r\n\\] | '\\' ~[\r\n] )* ["];
INTEGER : [0-9]+ ;
IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]* ;
COMMENT : ( '//' ~[\r\n]* | '/*' .*? '*/' ) -> skip ;
WS : [ \t\f\r\n]+ -> skip ;