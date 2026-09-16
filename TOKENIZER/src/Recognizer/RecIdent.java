/* Author: Prenom NOM, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear anais, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecIdent extends aRecognizer implements iRecognizer {

  // CONSTRUCTOR

  RecIdent(iCharStream cs, iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // LISTENER / NOTIFIER

  public interface iListener extends iRecognizer.iListener {
     void ident(String recognized);
    void keyword(String recognized);
    void type(String recognized);
  }

  private iListener listener;

  // NOTIFICATION

  @Override
  public void notifyAccept(String recognized) {
    switch (recognized) {
      case "if":
      case "then":
      case "else":
      case "return":
        listener.keyword(recognized);
        return;
    }
    if (Character.isUpperCase(recognized.charAt(0))) {
      listener.type(recognized);
      return;
    }
    listener.ident(recognized);
  }

  @Override
  public void notifyError(String consumed) {
  }

  static final int A1 = aAEF.ACCEPT + 1;  


  private boolean letter(char c) {
    return Character.isLetter(c);
  }

  private boolean digit(char c) {
    return Character.isDigit(c);
  }

  private boolean valid(char c) {
    return letter(c) || digit(c) || c == '_';
  }


  @Override
  public int step(char c) {

    switch (state) {
      case aAEF.ENTRY:
        if (letter(c) || c == '_') {
          consume(c);
          return A1;
        } else {
          return aAEF.REJECT;
        }
      case A1:
        if (valid(c)) {
          consume(c);
          return A1;
        }
        //sinon on consomme rien et on passe a accept final
        return aAEF.ACCEPT;

      default:
        return aAEF.REJECT;
    }
  }

}
