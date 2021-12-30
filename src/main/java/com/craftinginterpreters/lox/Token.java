package com.craftinginterpreters.lox;

public class Token {

    private final String lexeme;
    private final TokenType type;
    private final Object literal;
    private final int line;

    Token(String lexeme, TokenType type, Object literal, int line) {
        this.lexeme = lexeme;
        this.type = type;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
