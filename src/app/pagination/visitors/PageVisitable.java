package app.pagination.visitors;

import app.io.nodes.input.InputNode;

public interface PageVisitable {

  /**
   * Accepts a PageVisitor and an InputNode command. This method is part of the Visitor design
   * pattern. It allows a PageVisitor to visit the object that implements this interface.
   *
   * @param visitor The PageVisitor that is visiting the object.
   * @param command The InputNode object that represents the command given by the visitor.
   */
  void accept(PageVisitor visitor, InputNode command);
}
