package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.Top5OutputNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;

public final class GetTop5Songs implements Executable {
  /**
   * Returns the top 5 songs based on number of likes and index in the songs list.
   *
   * @param command The input command.
   * @return The top 5 songs.
   */
  @Override
  public Node execute(final InputNode command) {
    List<Song> songsStream = new ArrayList<>(Library.getInstance().getSongs());
    List<AudioEntity> top5Songs =
        songsStream.stream()
            .sorted(
                Comparator.comparingInt(Song::getNumberOfLikes)
                    .reversed()
                    .thenComparing(songsStream::indexOf))
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toList());

    return new Top5OutputNode(command, top5Songs);
  }
}
