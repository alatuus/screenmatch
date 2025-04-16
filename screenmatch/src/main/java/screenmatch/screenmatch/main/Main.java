package screenmatch.screenmatch.main;

import screenmatch.screenmatch.model.Episodes;
import screenmatch.screenmatch.model.EpisodesData;
import screenmatch.screenmatch.model.SeasonData;
import screenmatch.screenmatch.model.SeriesData;
import screenmatch.screenmatch.service.APIConsume;
import screenmatch.screenmatch.service.DataConversion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private APIConsume apiConsume = new APIConsume();
    private DataConversion dataConversion = new DataConversion();

    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=de2cea";

    public void showMenu(){
        System.out.println("Series name: ");
        var seriesName = scanner.nextLine();
        var json = apiConsume.obtainData(ADDRESS + seriesName.replace(" ", "+") + APIKEY);

        SeriesData data = dataConversion.obtainData(json, SeriesData.class);

        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

		for(int i = 1; i<= data.totalSeasons(); i++){
			json = apiConsume.obtainData(ADDRESS + seriesName.replace(" ", "+") + "&season=" + i + APIKEY);
			SeasonData seasonData = dataConversion.obtainData(json, SeasonData.class);

			seasons.add(seasonData);
		}

        seasons.forEach(System.out::println);

        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodesData> episodesData = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("Top 5 episodes: ");
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodesData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodes> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episodes(t.seasonNum(), d)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);

        /// //////////////


//        System.out.println("Search for episode: ");
//        var titlePiece = scanner.nextLine();
//
//        Optional<Episodes> searchEpisode = episodes.stream()
//                .filter(e -> e.getTitle().toLowerCase().contains(titlePiece.toLowerCase()))
//                .findFirst();
//        if(searchEpisode.isPresent()){
//            System.out.println(searchEpisode.get().getSeason());
//        } else {
//            System.out.println("Episode not found");
//        }

        /// ////////////////

//        System.out.println("Sort by year: ");
//        var year = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate searchDate = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
//                .forEach(e -> System.out.println(
//                        "Season: " + e.getSeason() +
//                                " | Episode: " + e.getTitle() +
//                                " | Release date: " + e.getReleaseDate().format(dtf)
//                ));

        Map<Integer, Double> seasonRating = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episodes::getSeason, Collectors.averagingDouble(Episodes::getRating)));
        System.out.println(seasonRating);

        DoubleSummaryStatistics stats = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episodes::getRating));
        System.out.println("Average rating: " + stats.getAverage());
        System.out.println("Lowest rating: " + stats.getMin());
        System.out.println("Highest rating: " + stats.getMax());
        System.out.println("Episodes evaluated: " + stats.getCount());


    }
}
