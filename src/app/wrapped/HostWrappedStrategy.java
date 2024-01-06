package app.wrapped;

import static app.searchbar.SearchType.NOT_INITIALIZED;

import app.Constants;
import app.commands.Executable;
import app.commands.executables.Wrapped;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Episode;
import library.users.User;

public class HostWrappedStrategy implements Executable {
  @Override
  public Node execute(InputNode command) {
    Wrapped.WrappedOutputNode out = new Wrapped.WrappedOutputNode(command);

    User host = Library.getInstance().getUserByName(command.getUsername());

    out.getResult().setTopEpisodes(getTop5Episodes(host));
    out.getResult().setTopFans(getTop5Fans(host));
    out.getResult().setListeners(out.getResult().getTopFans().size());
    out.getResult().setTopFans(null);

    if (out.getResult().getTopEpisodes().size() == 0 && out.getResult().getListeners() == 0) {
      out.setResult(null);
      out.setMessage("No data to show for user " + command.getUsername() + ".");
    }

    return out;
  }

  public Map<String, Integer> getTop5Episodes(User host) {
    Map<String, Integer> episodes = new LinkedHashMap<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == NOT_INITIALIZED) {
          Episode episode = (Episode) entry.getKey();

          for (Podcast podcast : Library.getInstance().getPodcastsByOwner(host.getUsername())) {
            if (podcast.getEpisodes().contains(episode)) {
              if (episodes.containsKey(episode.getName())) {
                episodes.put(episode.getName(), episodes.get(episode.getName()) + entry.getValue());
              } else {
                episodes.put(episode.getName(), entry.getValue());
              }
            }
          }
        }
      }
    }

    return episodes.entrySet().stream()
        .sorted(
            Map.Entry.<String, Integer>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry::getKey))
        .limit(Constants.PRINT_LIMIT)
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  public List<String> getTop5Fans(User host) {
    Set<String> fans = new HashSet<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == NOT_INITIALIZED) {
          Episode episode = (Episode) entry.getKey();

          for (Podcast podcast : Library.getInstance().getPodcastsByOwner(host.getUsername())) {
            if (podcast.getEpisodes().contains(episode)) {
              fans.add(user.getUsername());
            }
          }
        }
      }
    }

    return fans.stream()
            .sorted(String::compareTo)
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toList());
  }
}
