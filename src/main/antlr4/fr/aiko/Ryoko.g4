grammar Ryoko;
program
    : line+ EOF
    ;

line
    : statement
    | funcDeclaration
    ;

statement
    : varDeclaration';'
    | systemLib';'
    | functionCall';'
    ;

funcDeclaration
    : 'func' IDENTIFIER '::' '(' parameter? ')' '=>' (TYPE | 'void') funcBody
    ;

funcBody
    : '{' statement* '}'
    ;

functionCall
    : IDENTIFIER '(' expr? ')'
    ;

varDeclaration
    : TYPE ':' IDENTIFIER '=' expr
    ;

systemLib
    : SHOW '(' (expr) ')' // TODO: Multi call
    ;

parameter : TYPE ':' IDENTIFIER (',' TYPE ':' IDENTIFIER)*;

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