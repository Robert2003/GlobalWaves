package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.notifications.Notification;
import app.notifications.observer.Observer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class GetNotifications implements Executable {

	@Override
	public Node execute(InputNode command) {
		User user = Library.getInstance().getUserByName(command.getUsername());
		List<Notification> notifications = new ArrayList<>(user.getNotifications());
		user.getNotifications().clear();
		return new GetNotificationsOutputNode(command, notifications);
	}

	@Getter
	@Setter
	@JsonPropertyOrder({"command", "user", "timestamp", "message"})
	private final class GetNotificationsOutputNode extends Node {
		private String user;
		List<Notification> notifications;

		GetNotificationsOutputNode(final InputNode command, final List<Notification> notifications) {
			this.setCommand(command.getCommand());
			this.setTimestamp(command.getTimestamp());
			this.setUser(command.getUsername());
			this.setNotifications(notifications);
		}
	}
}
