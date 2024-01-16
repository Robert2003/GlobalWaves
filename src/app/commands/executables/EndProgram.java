package app.commands.executables;

import static app.Constants.PRECISION_FACTOR;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.monetization.Monetization;
import app.wrapped.ArtistWrappedStrategy;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class EndProgram implements Executable {
  private static int ranking = 1;

  @Override
  public Node execute(final InputNode command) {
    ranking = 1;
    List<User> artists = Library.getInstance().getArtists();

    artists =
        artists.stream()
            .sorted(
                Comparator.comparing(
                        User::getMonetization, Comparator.comparing(Monetization::getTotalRevenue))
                    .reversed()
                    .thenComparing(User::getUsername))
            .collect(Collectors.toList());

    EndProgramOutputNode out = new EndProgramOutputNode();

    for (User artist : artists) {
      out.addRevenue(artist);
    }

    return out;
  }

  @Getter
  @Setter
  public final class EndProgramOutputNode extends Node {
    private Map<String, Revenue> result;

    EndProgramOutputNode() {
      this.setCommand("endProgram");
      result = new LinkedHashMap<>();
    }

    protected void addRevenue(final User artist) {
      Revenue revenue = new Revenue();

      double songRevenue =
          Math.round(artist.getMonetization().getTotalSongRevenue() * PRECISION_FACTOR)
              / PRECISION_FACTOR;
      revenue.setSongRevenue(songRevenue);

      double merchRevenue =
          Math.round(artist.getMonetization().getTotalMerchRevenue() * PRECISION_FACTOR)
              / PRECISION_FACTOR;
      revenue.setMerchRevenue(merchRevenue);

      revenue.setRanking(ranking);

      Map<String, Double> mergedSongProfits =
          mergeSongPorifts(artist.getMonetization().getSongRevenue());
      Map.Entry<String, Double> maxEntry = null;

      for (Map.Entry<String, Double> entry : mergedSongProfits.entrySet()) {
        Double roundedValue = entry.getValue();
        if (maxEntry == null) {
          maxEntry = entry;
        } else {
          Double roundedMaxValue = maxEntry.getValue();
          int valueCompare = roundedMaxValue.compareTo(roundedValue);
          if (valueCompare < 0) {
            maxEntry = entry;
          } else if (valueCompare == 0) {
            if (maxEntry.getKey().compareTo(entry.getKey()) > 0) {
              maxEntry = entry;
            }
          }
        }
      }

      revenue.setMostProfitableSong(maxEntry != null ? maxEntry.getKey() : "N/A");

      if (!ArtistWrappedStrategy.getTop5Songs(artist).isEmpty() || revenue.getMerchRevenue() != 0) {
        getResult().put(artist.getUsername(), revenue);
        ranking++;
      }
    }

    private Map<String, Double> mergeSongPorifts(final Map<AudioEntity, Double> revenues) {
      Map<String, Double> mergedSongs = new LinkedHashMap<>();

      for (Map.Entry<AudioEntity, Double> entry : revenues.entrySet()) {
        mergedSongs.put(
            entry.getKey().getName(),
            mergedSongs.getOrDefault(entry.getKey().getName(), 0.0) + entry.getValue());
      }

      return mergedSongs;
    }

    @Getter
    @Setter
    public class Revenue {
      private double merchRevenue;
      private double songRevenue;
      private int ranking;
      private String mostProfitableSong;
    }
  }
}
