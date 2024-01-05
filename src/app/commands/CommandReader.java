package app.commands;

import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class CommandReader {
  /**
   * Reads commands from the specified file path.
   *
   * @param filePath the path of the file containing the commands
   * @return a list of InputNode objects representing the commands
   */
  public List<InputNode> readCommands(final String filePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    try {
      return objectMapper.readValue(new File(filePath), new TypeReference<>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
