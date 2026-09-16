/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public class RecABC extends aRecognizer implements iRecognizer {

  private iRecognizer.iListener listener;

  // CONSTRUCTOR

  public RecABC(iCharStream cs, iRecognizer.iListener lr) {
    super(cs);
    this.listener = lr;
  }

  // NOTIFICATION

  @Override
  public void notifyAccept(String recognized) {
    listener.accept(recognized);
  }

  @Override
  public void notifyError(String consumed) {
  }

  // STATES

  static final int After_A = aAEF.REJECT + 1;
  static final int After_B = aAEF.ACCEPT + 2;
  static final int After_C = aAEF.ACCEPT + 3;
  static final int caca = aAEF.ACCEPT + 4;


  // STEP : a . (b.b* | c)

  //pour test le meme automate mais les mots sont entre () cette fois pour pouvoir tester sur README
  /*@Override
  public int step(char c) {
    consume(c);
    switch (state) {
      case aAEF.ENTRY:
          if (c == '(')
            return caca;

          else 
            return aAEF.REJECT;
      case caca:
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
        if (c==')')
          return aAEF.ACCEPT;
        else
          return aAEF.REJECT;

      case After_C:
        if (c==')')
          return aAEF.ACCEPT;
        else 
          return aAEF.REJECT;
    
      default:
        return aAEF.REJECT;
    }
  }*/
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
