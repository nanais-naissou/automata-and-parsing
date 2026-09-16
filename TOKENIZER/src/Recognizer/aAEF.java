/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

public abstract class aAEF implements iAEF {

  // CONSTRUCTOR

  aAEF(iCharStream cs) {
    charstream = cs;
  }

  // FIELDS: flot de caractère à lire & état courant

  iCharStream charstream;
  int state;

  // JOB

  @Override
  public final void runAEF() {
    _consumed = new StringBuilder();

    state = aAEF.ENTRY;

    while (true) {
      char c = charstream.read();
      if (c == '\0')
        break;
      this.state = step(c);
      if (atExitState())
        break;
    }
  }

  @Override
  public String consumed() {
    return _consumed.toString();
  }

  // CONSUMED CHARACTERS

  private StringBuilder _consumed;

  void consume(char c) {
    _consumed.append(c);
    charstream.next();
  }

  // TRANSITIONS

  /**
   * @required = à définir dans les sous-classes
   * @implNote encode les transitions de l'automate sous la forme d'une
   *           méthode
   * @param c = caractère courant
   * @return l'état cible de la transition (état courant) -c-> (état cible)
   */
  abstract int step(char c);

  // PREDIFINED STATES

  /**
   * 3 special states which stops the recognizer:
   * 
   * - REJECT = stops and rejects
   * - ACCEPT = stops and accepts
   * - ERROR = stops and recognizes as an error
   * - ENTRY = default entry state (non accepting)
   * 
   * - other error states: [ ERROR +1, ...] = continue and recognizes as an error
   * on EOS
   * - other reject states: [ REJECT +1, ...] = continue and reject on EOS
   * - other accepting states: [ ACCEPT +1, ...] = continue and accept on EOS
   */

  static final int ERROR = Integer.MIN_VALUE;
  static final int REJECT = Integer.MIN_VALUE / 2;
  static final int ACCEPT = 0;
  static final int ENTRY = Integer.MAX_VALUE;

  // STATE CATEGORIES

  /**
   * @return true si l'état courant est un état accepteur qui reconnaît le mot
   *         comme valide.
   */
  final boolean atAcceptingState() {
    return state >= ACCEPT
        && state != ENTRY;
  }

  /**
   * @return true si l'état courant est un état accepteur qui reconnaît le mot
   *         comme erreur.
   */
  final boolean atErrorState() {
    return ERROR <= state && state < REJECT;
  }

  /**
   * @return true si l'état courant est un état de sortie (pas de transition
   *         sortante)
   */
  final boolean atExitState() {
    return state == REJECT || state == ACCEPT || state == ERROR;
  }

}
