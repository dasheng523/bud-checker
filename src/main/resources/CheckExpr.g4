grammar CheckExpr;

@header {
    package antlr4.parser.expr;
}

check
   : booleanExpr? EOF
   ;

booleanExpr
   : booleanRaw                                 # bLiteral
   | placeholder                                # bHolder
   | numberExpr compareOp numberExpr            # opExpr
   | function                                   # bFunc
   | '(' booleanExpr ')'                        # bBrackets
   | '!' booleanExpr                            # not
   | booleanExpr ('and' | 'AND') booleanExpr    # and
   | booleanExpr ('or' | 'OR') booleanExpr      # or
   ;

numberExpr
   : number                                     # num
   | placeholder                                # nHolder
   | function                                   # nFunc
   | '(' numberExpr ')'                         # nBrackets
   | numberExpr numberOp1 numberExpr            # op1
   | numberExpr numberOp2 numberExpr            # op2
   ;

function
    : IDENTIFIER '(' (funcParam (',' funcParam)*)? ')'
    ;

funcParam
    : booleanExpr       # bExpr
    | numberExpr        # nExpr
    ;

compareOp
    : '>' | '>=' | '<' | '<=' | '==' | '!=';

numberOp1
   : '*' | '/'
   ;

numberOp2
   : '+' | '-'
   ;


booleanRaw
    : 'true'
    | 'false'
    ;

placeholder
    : '${' (IDENTIFICATION ('.' IDENTIFICATION)*)? '}';


obj
   : '{' pair (',' pair)* ','? '}'
   | '{' '}'
   ;

pair
   : key ':' value
   ;

key
   : STRING
   | IDENTIFIER
   | LITERAL
   | NUMERIC_LITERAL
   ;

value
   : STRING
   | number
   | obj
   | arr
   | LITERAL
   ;

arr
   : '[' value (',' value)* ','? ']'
   | '[' ']'
   ;

number
   : SYMBOL?
      ( NUMERIC_LITERAL
      | NUMBER
      )
   ;

// Lexer

IDENTIFICATION
   : ('a' .. 'z' | 'A' .. 'Z' | '_') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.')*
   ;

SINGLE_LINE_COMMENT
   : '//' .*? (NEWLINE | EOF) -> skip
   ;

MULTI_LINE_COMMENT
   : '/*' .*? '*/' -> skip
   ;

LITERAL
   : BOOLEAN_LITERAL
   | 'null'
   ;

BOOLEAN_LITERAL
    : 'true'
    | 'false'
    ;



STRING
   : '"' DOUBLE_QUOTE_CHAR* '"'
   | '\'' SINGLE_QUOTE_CHAR* '\''
   ;

fragment DOUBLE_QUOTE_CHAR
   : ~["\\\r\n]
   | ESCAPE_SEQUENCE
   ;
fragment SINGLE_QUOTE_CHAR
   : ~['\\\r\n]
   | ESCAPE_SEQUENCE
   ;
fragment ESCAPE_SEQUENCE
   : '\\'
   ( NEWLINE
   | UNICODE_SEQUENCE       // \u1234
   | ['"\\/bfnrtv]          // single escape char
   | ~['"\\bfnrtv0-9xu\r\n] // non escape char
   | '0'                    // \0
   | 'x' HEX HEX            // \x3a
   )
   ;
NUMBER
   : INT ('.' [0-9]*)? EXP? // +1.e2, 1234, 1234.5
   | '.' [0-9]+ EXP?        // -.2e3
   | '0' [xX] HEX+          // 0x12345678
   ;
NUMERIC_LITERAL
   : 'Infinity'
   | 'NaN'
   ;
SYMBOL
   : '+' | '-'
   ;
fragment HEX
   : [0-9a-fA-F]
   ;
fragment INT
   : '0' | [1-9] [0-9]*
   ;
fragment EXP
   : [Ee] SYMBOL? [0-9]*
   ;
IDENTIFIER
   : IDENTIFIER_START IDENTIFIER_PART*
   ;
fragment IDENTIFIER_START
   : [\p{L}]
   | '$'
   | '_'
   | '\\' UNICODE_SEQUENCE
   ;
fragment IDENTIFIER_PART
   : IDENTIFIER_START
   | [\p{M}]
   | [\p{N}]
   | [\p{Pc}]
   | '\u200C'
   | '\u200D'
   ;
fragment UNICODE_SEQUENCE
   : 'u' HEX HEX HEX HEX
   ;
fragment NEWLINE
   : '\r\n'
   | [\r\n\u2028\u2029]
   ;
WS
   : [ \t\n\r\u00A0\uFEFF\u2003] + -> skip
   ;