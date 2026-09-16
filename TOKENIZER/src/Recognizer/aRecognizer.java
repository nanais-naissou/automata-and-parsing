/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL                                                                                                                                 
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public abstract class aRecognizer extends aAEF {

  // CONSTRUCTOR

  public aRecognizer(iCharStream cs) {
    super(cs);
  }

  // JOB

  public final boolean recognize() {
    super.charstream.backup();

    runAEF();

    if (atAcceptingState()) { // accepted as recognized word
      notifyAccept(consumed());
      return true;
    }
    if (atErrorState()) { // accepted as an error
      notifyError(consumed());
      return true;
    }
    {
      super.charstream.backtrack();
      return false; // rejected
    }
  }

  // ABSTRACT = à définir dans les sous-classes

  abstract public void notifyAccept(String recognized);

  abstract public void notifyError(String consumed);

}
