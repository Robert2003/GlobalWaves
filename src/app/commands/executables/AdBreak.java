package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.THE_USERNAME;

public class AdBreak implements Executable {

	@Override
	public Node execute(InputNode command) {
		User user = Library.getInstance().getUserByName(command.getUsername());

		if (user == null) {
			return new AdBreakOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
		}

		return null;
	}

	@Getter
	@Setter
	@JsonPropertyOrder({"command", "user", "timestamp", "message"})
	private final class AdBreakOutputNode extends Node {
		private String user;
		private String message;

		AdBreakOutputNode(final InputNode command, final String message) {
			this.setCommand(command.getCommand());
			this.setTimestamp(command.getTimestamp());
			this.setUser(command.getUsername());
			this.setMessage(message);
		}
	}
}