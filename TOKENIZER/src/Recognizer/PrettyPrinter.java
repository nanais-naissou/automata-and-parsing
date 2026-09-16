/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

import java.io.PrintStream;

public class PrettyPrinter implements Tokenizer.iListener {

  PrintStream ps;

  public PrettyPrinter(PrintStream ps) {
    this.ps = ps;
  }

  // FONT

  // == CONSTANTS
  static final String DEFAULT = "0";

  static final String BOLD = ";1";
  static final String ITALIC = ";3";
  static final String FADED = ";2";
  static final String UNDERLINED = ";4";
  static final String INVERSE = ";7";
  static final String STRIKEOUT = ";9";

  static final String ACCEPT = "93" + BOLD; //jaune
  static final String IDENT = "97" + BOLD; //blanc

  static final String ANY = "90" + BOLD + FADED;
  static final String ERROR = "91";
  static final String STRING = "92"+BOLD;
  static final String COMMENT = "94" + FADED + ITALIC;
  static final String COMMAND = "95";
  static final String MARK = "93" + BOLD;
  static final String EXPLANATION = "97" + INVERSE;

  static final String CYAN = "96";
  static final String RGB = "38;2;";
  static final String ORANGE = RGB + "255;165;0";
  static final String PINK = RGB + "170;0;170";
static final String STRING1 = RGB + "128;0;128"+BOLD; // violet

  // == METHODS

  private String current_font = DEFAULT;

  private void setCurrent_font(String font) {
    current_font = font;
    ps.print(String.format("%c[%sm", (char) 033, current_font));
  }

  private void resetFont() {
    if (!current_font.equals(DEFAULT))
      ps.print(String.format("%c[%sm", (char) 033, DEFAULT));
  }

  private void updateFont(String font) {
    if (!current_font.equals(font)) {
      resetFont();
      setCurrent_font(font);
    }
  }

  // == REQUIRED BY INTERFACE iListener

  @Override
  public void start() {
    resetFont();
  }

  @Override
  public void error(String consumed, String explanation) {
    updateFont(ERROR);
    ps.print(consumed);
    if (explanation != null && !explanation.isEmpty()) {
      updateFont(EXPLANATION);
      ps.print(explanation);
    }
  }

  @Override
  public void accept(String recognized) {
    updateFont(ACCEPT);
    ps.print(recognized);
  }

  @Override
  public void end() {
    resetFont();
    ps.print("\n\n");
  }

  @Override
  public void any(String string) {
    updateFont(ANY);
    ps.print(string);
  }

  @Override
  public void separator(String string) {
    updateFont(ORANGE);
    ps.print(string);
  }

  @Override
  public void operator(String string) {
    updateFont(CYAN + BOLD);
    ps.print(string);
  }

  @Override
  public void command(String string) {
    updateFont(COMMAND);
    ps.print(string);
  }

  @Override
  public void opening(String string) {
    updateFont(MARK);
    ps.print(string);
  }

  @Override
  public void closing(String string) {
    updateFont(MARK);
    ps.print(string);
  }

  @Override
  public void ident(String string) {
    updateFont(IDENT);
    ps.print(string);
  }

  @Override
  public void keyword(String string) {
    updateFont(ACCEPT); //jaune
    ps.print(string);
  }

  @Override
  public void type(String string) {
    updateFont(CYAN); //orange
    ps.print(string);
  }



  @Override
  public void string(String string) {
    updateFont(STRING);
    ps.print(string);
  }

  @Override
  public void comment(String string) {
    updateFont(COMMENT);
    ps.print(string);
  }


  // TEST

  static public void main(String _args[]) {
    PrettyPrinter pp = new PrettyPrinter(System.out);
    pp.start();

    pp.error("error", "explanation");
    pp.accept("abc");

    pp.any("any");
    pp.separator("separator");
    pp.opening("opening");
    pp.command("command");
    pp.closing("closing");
    pp.ident("ident");
    pp.string("string");
    pp.comment("comment");
    pp.end();
  }

}
