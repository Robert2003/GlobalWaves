package app.commands;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;

/**
 * This interface represents an executable command in the application. It provides a single method,
 * execute, which takes an InputNode object as a parameter. This interface is a part of the Command
 * design pattern, where each command is encapsulated as an object.
 */
public interface Executable {
  /**
   * Executes the command represented by this object.
   *
   * @param command The InputNode object that represents the command to be executed.
   * @return Node object that represents the result of the execution.
   */
  Node execute(InputNode command);
}
