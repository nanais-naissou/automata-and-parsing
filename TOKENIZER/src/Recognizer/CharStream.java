/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

import java.nio.CharBuffer;

public class CharStream implements iCharStream {

  private CharBuffer buffer;

  private int current_index;
  private int backup_index;
  private int length;

  // CONSTRUCTOR

  public CharStream(String input) {
    backup_index = current_index = 0;
    length = input.length();
    buffer = CharBuffer.allocate(length);
    for (int i = 0; i < length; i++)
      buffer.put(input.charAt(i));
    buffer.flip();
  }

  // METHODS

  public boolean isEmpty() {
    return current_index >= length;
  }

  public char read() {
    if (isEmpty())
      return '\0';
    return buffer.charAt(current_index);
  }

  public void next() {
    current_index++;
  }

  public void backup() {
    backup_index = current_index;
  }

  public void backtrack() {
    current_index = backup_index;
  }

}