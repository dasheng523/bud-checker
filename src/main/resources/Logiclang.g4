grammar Logiclang;

@header {
    package antlr4.parser.expr;
}

stmt: expr+;

expr
    : maybeExpr             # maybe
    | distinctExpr          # distinct
    | notEqExpr             # notEq
    | gtExpr                # gt
    | notNearExpr           # notNear
    | printExpr             # print
    ;

maybeExpr
    : 'var' VARNAME '=' 'maybe(' (INT ',')* INT ')' ';'
    ;

distinctExpr
    : 'distinct' '(' (VARNAME ',')* VARNAME ')' ';'
    ;

notEqExpr
    : 'notEq' '(' VARNAME ',' INT ')' ';'
    ;

gtExpr
    : 'gt' '(' VARNAME ',' VARNAME ')' ';'
    ;

notNearExpr
    : 'notNear' '(' VARNAME ',' VARNAME ')' ';'
    ;

printExpr
    : 'print' '(' (VARNAME ',')* VARNAME ')' ';'
    ;

VARNAME
    : [a-zA-Z][a-zA-Z0-9]*
    ;

INT
    : '0' | [1-9][0-9]*
    ;

WS
   : [ \t\n\r\u00A0\uFEFF\u2003] + -> skip
   ;