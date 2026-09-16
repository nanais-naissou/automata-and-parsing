/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecCommand extends aRecognizer implements iRecognizer {

  // CONSTRUCTOR

  public RecCommand(iCharStream cs, iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // LISTENER / NOTIFIER

  public interface iListener extends iRecognizer.iListener {
    void command(String recognized);
  }

  private iListener listener;

  // NOTIFICATION

  @Override
  public void notifyAccept(String recognized) {
    listener.command(recognized);
  }

  @Override
  public void notifyError(String consumed) {
    String explanation = new String();
    {
      if (state == aAEF.ERROR || state == FORBIDDEN)
        explanation += "« [\\n] is forbidden in command »";
      if (state == BODY || state == FORBIDDEN)
        explanation += "« unterminated command, missing [`] »";
    }
    listener.error(consumed, explanation);
  }

  // STATE

  static final int BODY = aAEF.ERROR + 1;
  static final int FORBIDDEN = aAEF.ERROR + 2;
  static final int ESCAPE = aAEF.ERROR + 3;

  // STEP

  public int step(char c) {
    consume(c);
    switch (state) {
      case aAEF.ENTRY:
        if (c == '`')
          return BODY;
        else
          return aAEF.REJECT;

      case BODY:
        if (c == '`')
          return aAEF.ACCEPT;
        if (c == '\\')
          return ESCAPE;
        if (c == '\n')
          return FORBIDDEN;
        else
          return BODY;

      case ESCAPE:
        return BODY;

      case FORBIDDEN:
        if (c == '`')
          return aAEF.ERROR;
        else
          return FORBIDDEN;

      default:
        return aAEF.REJECT;
    }
  }
}
