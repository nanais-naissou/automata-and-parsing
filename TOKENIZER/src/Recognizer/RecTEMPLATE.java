/* Author: Prenom NOM, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecTEMPLATE extends aRecognizer implements iRecognizer {

  // CONSTRUCTOR

  RecTEMPLATE(iCharStream cs, iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // LISTENER

  public interface iListener extends iRecognizer.iListener {
    void template(String c);
  }

  private iListener listener;

  // NOTIFICATION

  @Override
  public void notifyAccept(String recognized) {
    listener.template(recognized);
  }

  @Override
  public void notifyError(String consumed) {
  }

  // STATES

  // STEP

  public int step(char c) {
    consume(c);
    switch (state) {
      case aAEF.ENTRY:

      default:
        return aAEF.REJECT;
    }
  }

}
