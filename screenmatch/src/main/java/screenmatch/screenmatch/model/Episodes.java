package screenmatch.screenmatch.model;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodes {
    private Integer season;
    private String title;
    private Integer episodeNum;
    private Double rating;
    private LocalDate releaseDate;

    public Episodes(Integer season, EpisodesData episodesData) {
        this.season = season;
        this.title = episodesData.title();
        this.episodeNum = episodesData.episodeNum();

        try {
            this.rating = Double.valueOf(episodesData.rating());
        } catch (NumberFormatException e) {
            this.rating = 0.0;
        }

        try {
            this.releaseDate = LocalDate.parse(episodesData.releaseDate());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }

    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNum() {
        return episodeNum;
    }

    public void setEpisodeNum(Integer episodeNum) {
        this.episodeNum = episodeNum;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }



    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", episodeNum=" + episodeNum +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate;
    }
}
