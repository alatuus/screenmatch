package screenmatch.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import screenmatch.screenmatch.model.EpisodesData;
import screenmatch.screenmatch.model.SeasonData;
import screenmatch.screenmatch.model.SeriesData;
import screenmatch.screenmatch.service.APIConsume;
import screenmatch.screenmatch.service.DataConversion;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		APIConsume apiConsume = new APIConsume();
		var json = apiConsume.obtainData("http://www.omdbapi.com/?t=silicon+valley&apikey=de2cea");
		System.out.println(json);

		DataConversion dataConversion = new DataConversion();
		SeriesData data = dataConversion.obtainData(json, SeriesData.class);
		System.out.println(data);

		json = apiConsume.obtainData("http://www.omdbapi.com/?t=silicon+valley&season=2&episode=4&apikey=de2cea");
		EpisodesData episodesData = dataConversion.obtainData(json, EpisodesData.class);
		System.out.println(episodesData);

		List<SeasonData> seasons = new ArrayList<>();

		for(int i = 1; i<= data.totalSeasons(); i++){
			json = apiConsume.obtainData("http://www.omdbapi.com/?t=silicon+valley&season="
					+ i + "&apikey=de2cea");
			SeasonData seasonData = dataConversion.obtainData(json, SeasonData.class);

			seasons.add(seasonData);
		}

		seasons.forEach(System.out::println);
	}
}
