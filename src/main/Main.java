package main;

import app.commands.CommandManager;
import app.commands.CommandReader;
import app.commands.executables.EndProgram;
import app.history.OrderedHistory;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.monetization.payment.PremiumPaymentStrategy;
import app.monetization.subscription.UserPremiumState;
import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import library.Library;
import library.users.User;

public final class Main {
  static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

  /** for coding style */
  private Main() {
  }

  /**
   * DO NOT MODIFY MAIN METHOD Call the checker
   *
   * @param args from command line
   * @throws java.io.IOException in case of exceptions to reading / writing
   */
  public static void main(final String[] args) throws IOException {
    File directory = new File(CheckerConstants.TESTS_PATH);
    Path path = Paths.get(CheckerConstants.RESULT_PATH);

    if (Files.exists(path)) {
      File resultFile = new File(String.valueOf(path));
      for (File file : Objects.requireNonNull(resultFile.listFiles())) {
        file.delete();
      }
      resultFile.delete();
    }
    Files.createDirectories(path);

    for (File file : Objects.requireNonNull(directory.listFiles())) {
      if (file.getName().startsWith("library")) {
        continue;
      }

      String filepath = CheckerConstants.OUT_PATH + file.getName();
      File out = new File(filepath);
      boolean isCreated = out.createNewFile();
      if (isCreated) {
        action(file.getName(), filepath);
      }
    }

    Checker.calculateScore();
  }

  /**
   * @param filePathInput for input file
   * @param filePathOutput for output file
   * @throws java.io.IOException in case of exceptions to reading / writing
   */
  public static void action(final String filePathInput, final String filePathOutput)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    Library library = objectMapper.readValue(new File(LIBRARY_PATH), Library.class);
    library.setObjectTypes();
    Library.setInstance(library);

    ArrayNode outputs = objectMapper.createArrayNode();

    CommandReader reader = new CommandReader();
    List<InputNode> commandList = reader.readCommands(CheckerConstants.TESTS_PATH + filePathInput);
    Node out;

    if (commandList != null && !commandList.isEmpty()) {
      for (InputNode c : commandList) {
        out = CommandManager.getManager().processCommand(c);

        JsonNode commandNode = objectMapper.valueToTree(out);
        outputs.add(commandNode);
      }
    }

    for (User user : Library.getInstance().getNormalUsers()){
      if (user.getPremiumState() == UserPremiumState.PREMIUM) {
        new PremiumPaymentStrategy().pay(user);
      }
    }

    out = new EndProgram().execute(null);
    JsonNode commandNode = objectMapper.valueToTree(out);
    outputs.add(commandNode);

    ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    objectWriter.writeValue(new File(filePathOutput), outputs);
  }
}
