package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Lox {

    // to make sure we don't try to execute code that has a known error
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            // see https://www.freebsd.org/cgi/man.cgi?query=sysexits&apropos=0&sektion=0&manpath=FreeBSD+4.3-RELEASE&format=html
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * Executes interactive REPL prompt.
     */
    private static void runPrompt() throws IOException {
        final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        for (;;) {
            System.out.println("> ");
            final String line = inputReader.readLine();
            if (line == null) break;
            run(line);
            // reset the error flag to let the user continue even in presence of errors
            hadError = false;
        }
    }

    /**
     * Executes Lox script saved in given file
     */
    private static void runFile(String filePath) throws IOException {
        final byte[] bytes = Files.readAllBytes(Path.of(filePath));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) {
            System.exit(65);
        }
    }

    private static void run(String source) {
        final LoxScanner scanner = new LoxScanner(source);
        final List<Token> tokens = scanner.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
        hadError = true;
    }

    private static void report(int line, String where, String message) {
//        System.err.println("[line " + line + "] Error" + where + ": " + message);
        System.err.printf("[line %s] Error%s: %s%n", line, where, message);
        // I think this should really be in the `error` method so I moved it there
        // hadError = true;
    }
}
