grammar Calculator;

INT    : [0-9]+;
DOUBLE : [0-9]+'.'[0-9]+;
WS     : [ \t\r\n]+ -> skip;
ID     : [a-zA-Z_][a-zA-Z_0-9]*;

PLUS  : '+';
MINUS : '-';
MULT  : '*';
DIV   : '/';
LPAR  : '(';
RPAR  : ')';
COMMA : ',';

input
    : plusOrMinus EOF             # Calculate
    ;

plusOrMinus
    : plusOrMinus PLUS multOrDiv  # Plus
    | plusOrMinus MINUS multOrDiv # Minus
    | multOrDiv                   # ToMultOrDiv
    ;

multOrDiv
    : multOrDiv MULT unaryMinus # Multiplication
    | multOrDiv DIV unaryMinus  # Division
    | unaryMinus                # ToUnaryMinus
    ;

unaryMinus
    : MINUS unaryMinus # ChangeSign
    | atom             # ToAtom
    ;

atom
    : DOUBLE                # Double
    | INT                   # Int
    | ID                    # Variable
    | LPAR plusOrMinus RPAR # Braces
    | func LPAR plusOrMinus (COMMA plusOrMinus)* RPAR # Function
    ;

func: 'sqrt' | 'log' ;
