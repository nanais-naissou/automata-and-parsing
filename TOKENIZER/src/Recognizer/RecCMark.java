/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecCMark extends aRecognizer implements iRecognizer {

  // CONSTRUCTOR

  public RecCMark(iCharStream cs, iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // LISTENER / NOTIFIER

  public interface iListener extends iRecognizer.iListener {
    void closing(String recognized);
  }

  private iListener listener;

  // NOTIFICATION: REQUIRED

  @Override
  public void notifyAccept(String recognized) {
    listener.closing(recognized);
  }

  @Override
  public void notifyError(String consumed) {
    listener.error(consumed, "unterminated closing mark");
  }

  // STATES

  static final int OPENING = aAEF.REJECT + 1;
  static final int NAME_FIRST_LETTER = aAEF.ERROR + 2;
  static final int NAME_CONTINUED = aAEF.ERROR + 3;

  // STEP

  public int step(char c) {
    consume(c);
    switch (state) {
      case aAEF.ENTRY:
        if (c == '<')
          return OPENING;
        else
          return aAEF.REJECT;

      case OPENING:
        if (c == '/')
          return NAME_FIRST_LETTER;
        else
          return aAEF.REJECT;

      case NAME_FIRST_LETTER:
        if ('A' <= c && c <= 'Z')
          return NAME_CONTINUED;
        else
          return aAEF.REJECT;

      case NAME_CONTINUED:
        if (('A' <= c && c <= 'Z') || c == '_' || ('0' <= c && c <= '9'))
          return NAME_CONTINUED;
        if (c == '>')
          return aAEF.ACCEPT;
        else
          return aAEF.REJECT;

      default:
        return aAEF.REJECT;
    }
  }
}
