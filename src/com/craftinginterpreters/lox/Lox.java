package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
//import java.util.Scanner;

/*Lox is a scripting language, which means it executes directly from source. Our interpreter supports two
ways of running code.
 */

public class Lox
{
    static boolean hadError = false;
    static boolean hadRuntimeerror = false;

    private static final Interpreter interpreter = new Interpreter();

      public static void main(String[] args) throws IOException
      {
          if(args.length > 1)
          {
              System.out.print("Usage: jlox [script]");
              System.exit(64);
          }
          else if(args.length == 1)
          {
              runFile(args[0]);
          }
          else
          {
              runPrompt();
          }
      }

      /*
      if you start jlox from the command line and give it a path to a file, it reads the
      file and executes it.
       */
    public static void runFile(String path) throws IOException
    {
        byte[] bytes = Files.readAllBytes((Paths.get(path)));
        run(new String(bytes, Charset.defaultCharset()));

        if(hadError)
            System.exit(65);
    }

    /*
    We can also run the interpreter interactively, If we fire up jlox without any arguments, it'll drop you
    into a prompt where you can enter and exceute one line at a time
     */

    public static void runPrompt() throws IOException
    {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;)
        {
            System.out.print("> ");
            String line = reader.readLine();
            if(line == null)
                break;

            run(line);
        }
    }

    public static void run(String source)
    {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if(hadError)
        {
            System.exit(65);
        }

        if(hadRuntimeerror)
            System.exit(70);
            //return;

        interpreter.interpret(statements);

       // System.out.println(new AstPrinter().print(expr));

       /* for(Token token: tokens)
        {
            System.out.println(token);
        }
        */
    }

    // We move to error handling now
    /*
    If we intend to make a language that's actually usable, then handling errors gracefully is vital
     */

    /*
    It is upto us to give the user all the information they need to understand what went wrong, and guide them
    gently back to where they are trying to go
     */

    static void error(int line, String message)
    {
        report(line, "", message);
    }

    static void error(Token token, String message)
    {
        if(token.type == TokenType.EOF)
        {
            report(token.line, " at end", message);
        }
        else
        {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    public static void report(int line, String where, String message)
    {
        System.err.println("[line " + line + "] Error "+ where + ": " + message);
        hadError = true;
    }

    static void runtimeError(RuntimeError error)
    {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeerror = true;
    }

    /*
    Lexical analysis is to scan through the list of characters and group them into the smallest sequences
    that still represent something. Each of these blobs od character is called a 'lexeme'
     */

    /*
    Lexemes are the raw substrings of the source code
     */
}
