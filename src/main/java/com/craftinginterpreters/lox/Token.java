package com.craftinginterpreters.lox;

public class Token {

    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        // note that I use single quotes to signal that lexeme is a string
        return type + " '" + lexeme + "' " + literal;
    }
}
