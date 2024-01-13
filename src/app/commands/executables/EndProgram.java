package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.monetization.Monetization;
import app.wrapped.ArtistWrappedStrategy;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public class EndProgram implements Executable {
  private static int ranking = 1;

  @Override
  public Node execute(InputNode command) {
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
//      System.out.println(artist.getMonetization().getSongRevenue());
    }

    return out;
  }

  @Getter
  @Setter
  public class EndProgramOutputNode extends Node {
    Map<String, Revenue> result;

    EndProgramOutputNode() {
      this.setCommand("endProgram");
      result = new LinkedHashMap<>();
    }

    protected void addRevenue(User artist) {
      Revenue revenue = new Revenue();

      double songRevenue =
          Math.round(artist.getMonetization().getTotalSongRevenue() * 100.0) / 100.0;
      revenue.setSongRevenue(songRevenue);

      double merchRevenue =
          Math.round(artist.getMonetization().getTotalMerchRevenue() * 100.0) / 100.0;
      revenue.setMerchRevenue(merchRevenue);

      revenue.setRanking(ranking);

      Map.Entry<AudioEntity, Double> maxEntry = null;

      for (Map.Entry<AudioEntity, Double> entry : artist.getMonetization().getSongRevenue().entrySet()) {
        Double roundedValue = entry.getValue();
        if (maxEntry == null) {
          maxEntry = entry;
        } else {
          Double roundedMaxValue = maxEntry.getValue();
          int valueCompare = roundedMaxValue.compareTo(roundedValue);
          if (valueCompare < 0) {
            maxEntry = entry;
          } else if (valueCompare == 0) {
            if (maxEntry.getKey().getName().compareTo(entry.getKey().getName()) > 0) {
              maxEntry = entry;
            }
          }
        }
      }

      revenue.setMostProfitableSong(
          maxEntry != null ? maxEntry.getKey().getName() : "N/A");

      if (!ArtistWrappedStrategy.getTop5Songs(artist).isEmpty() || revenue.getMerchRevenue() != 0) {
        getResult().put(artist.getUsername(), revenue);
        ranking++;
      }
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
