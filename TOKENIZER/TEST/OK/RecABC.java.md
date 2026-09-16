/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecABC extends aRecognizer implements iRecognizer {

  // CONSTRUCTOR

  RecABC(iCharStream cs, iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // LISTENER

  public interface iListener extends iRecognizer.iListener {
    void abc(String recognized);
  }

  private iListener listener;

  // NOTIFICATION

  @Override
  public void notifyAccept(String recognized) {
    listener.abc(recognized);
  }

  @Override
  public void notifyError(String consumed) {
  }

  // STATES

  static final int After_A = aAEF.REJECT + 1;
  static final int After_B = aAEF.ACCEPT + 2;
  static final int After_C = aAEF.ACCEPT + 3;

  // STEP : a . (b.b* | c)

  @Override
  public int step(char c) {
    consume(c);
    switch (state) {
      case aAEF.ENTRY:
        if (c == 'a')
          return After_A;
        else
          return aAEF.REJECT;

      case After_A:
        if (c == 'b')
          return After_B;
        if (c == 'c')
          return After_C;
        else
          return aAEF.REJECT;

      case After_B:
        if (c == 'b')
          return After_B;
        else
          return aAEF.REJECT;

      case After_C:
        return aAEF.REJECT;

      default:
        return aAEF.REJECT;
    }
  }
}
