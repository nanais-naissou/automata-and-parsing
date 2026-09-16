/* Author: Michaël Périn, Polytech Grenoble
 *  
 * LICENCE GPL
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Dear USER, see the GNU General Public License for more details.
*/

package Recognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Tokenizer {

  private iRecognizer abc, any, command, omark, cmark, comment, ident, string;

  private iListener listener;

  // CONSTRUCTOR

  public Tokenizer(iCharStream cs, iListener lr) {
    if (lr == null)
      throw new IllegalArgumentException("need a iListener");
    else {
      listener = lr;
      abc = new RecABC(cs, lr);
      any = new RecAny(cs, lr);
      command = new RecCommand(cs, lr);
      omark = new RecOMark(cs, lr);
      cmark = new RecCMark(cs, lr);
      ident= new RecIdent(cs, lr); 
     string = new RecString(cs, lr);
     comment= new RecComment(cs, lr);
      }
  }

  // LISTENER

  public interface iListener
      extends
       RecString.iListener,
      RecComment.iListener,
      RecAny.iListener,
      RecCommand.iListener,
      RecOMark.iListener,
      RecCMark.iListener,
      RecABC.iListener,
      RecIdent.iListener

  // <nouveau reconnaisseur>.iListener
  {

    // PROTOCOL: start() ; (_) * ; end()

    void start();

    void end();
  }

  // RECOGNIZER

  private boolean recognize() {
    return false
        // || <nouveau reconnaisseur>.recognize()

        || command.recognize()
                      || string.recognize()

                                        || comment.recognize()
                || abc.recognize()

        || ident.recognize()
        || omark.recognize()
        || cmark.recognize()
        || any.recognize();

  }

  // TOKENIZER

  public void tokenize() {
    listener.start();
    while (recognize()) {
      // NOTHING
    }
    listener.end();
  }

  // TEST

  public static void main(String args[]) throws FileNotFoundException, IOException {
    iListener lr = new PrettyPrinter(System.out);
    String input;
    iCharStream cs;
    Tokenizer tokenizer;

    if (args.length == 1) {
      input = Files.readString(Path.of(args[0]));
      cs = new CharStream(input);
      tokenizer = new Tokenizer(cs, lr);
      tokenizer.tokenize();
      return;
    }
  }

}
