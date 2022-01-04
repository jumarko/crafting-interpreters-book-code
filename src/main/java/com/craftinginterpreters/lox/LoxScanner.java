package com.craftinginterpreters.lox;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See https://craftinginterpreters.com/scanning.html#the-scanner-class
 */
class LoxScanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    // these fields are mutable!
    private int start = 0; // first char of the lexeme being scanned
    private int current = 0; // current char of the current lexeme
    private int line = 1; // line on which the current char is

    LoxScanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return Collections.unmodifiableList(tokens);
    }

    private void scanToken() {
        char c = advance();
        // see switch expressions enhancements: https://www.codejava.net/java-core/the-java-language/switch-expression-examples
        switch (c) {
            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);
            case '{' -> addToken(TokenType.LEFT_BRACE);
            case '}' -> addToken(TokenType.RIGHT_BRACE);
            case ',' -> addToken(TokenType.COMMA);
            case '.' -> addToken(TokenType.DOT);
            case '-' -> addToken(TokenType.MINUS);
            case '+' -> addToken(TokenType.PLUS);
            case ';' -> addToken(TokenType.SEMICOLON);
            case '*' -> addToken(TokenType.STAR);
            case '!' -> addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '=' -> addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '<' -> addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>' -> addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
            // '/' needs special handling because comments start with slash
            case '/' -> {
                if (match('/')) {
                    // consume the comment content until the end of line
                    // notice here we check if it's not a new line - this is to be able to increment
                    // the line counter (see `case '\n'` below)
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
            }
            case ' ', '\r', '\t' -> {} // ignore whitespace
            case '\n' -> line++; // ignore new lines too but also increment the line counter

            // we keep scanning even after we encounter erroneous character
            default -> Lox.error(line, "Unexpected character: " + c);
        }
    }

    /**
     * Looks at the next character without consuming it.
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * `match` is like a conditional `advance` - it only consumes the current character if it's what we are looking
     * for.
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
