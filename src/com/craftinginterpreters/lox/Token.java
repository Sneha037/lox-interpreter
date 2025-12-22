package com.craftinginterpreters.lox;


/*
As the Scanner has to walk through each charcter in the literal to correctly identity it, it can also
convert that textual representation of a value to the living runtime object will be used by the interpreter
 */

class Token
{
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line)
    {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString()
    {
        return type + " " + lexeme + " " + literal;
    }
}
