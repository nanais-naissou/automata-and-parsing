/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecAny extends aRecognizer implements iRecognizer {

  // CONSTRUCTOR

  public RecAny(iCharStream cs, iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // LISTENER / NOTIFIER

  public interface iListener extends iRecognizer.iListener {

    void any(String c);

    void separator(String c);

    void operator(String c);
  }

  private iListener listener;

  // NOTIFICATION

  @Override
  public void notifyAccept(String recognized) {
    switch (recognized) {
      case ";":
      case "{":
      case "}":
        listener.separator(recognized);
        break;
      case "#":
      case "&":
        listener.operator(recognized);
        break;
      default:
        listener.any(recognized);
    }
  }

  @Override
  public void notifyError(String recognized) {
  }

  // STEP: (ENTRY) -(_)-> ((ACCEPT))

  @Override
  public int step(char c) {
    switch (state) {
      case aAEF.ENTRY:
        consume(c);
        return aAEF.ACCEPT;

      default:
        return aAEF.REJECT;
    }
  }

}
