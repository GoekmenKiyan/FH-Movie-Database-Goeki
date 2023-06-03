package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;

import java.net.MalformedURLException;
import java.net.URL;


public class URLBuilder {
    private static final String DELIMITER = "&"; //Separator
    private String query;
    private Genre genre;
    private String releaseYear;
    private String ratingFrom;


    public URLBuilder withQuery(String query) {
        this.query = this.query;
        return this;
    }

    public URLBuilder withGenre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public URLBuilder withReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public URLBuilder withRatingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }


    public URL build() throws MalformedURLException {
        StringBuilder url = new StringBuilder();
        // urlString.append(protocol).append("://").append(protocol);


        //Checken, ob was übergeben wurde (Wenn ein Parameter übergeben wurde, brauchen wir ein "?"
        if((query != null && !query.isEmpty())|| genre != null || releaseYear != null || ratingFrom != null) { //empty ""
            url.append("?");
            if(query != null && !query.isEmpty()){
                url.append("query=").append(query).append(DELIMITER);
            }
            if(genre != null){
                url.append("genre=").append(genre).append(DELIMITER);
            }
            if(releaseYear != null){
                url.append("releaseYear=").append(releaseYear).append(DELIMITER);
            }
            if(ratingFrom != null){
                url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
            }
        }
        //return url.toString();
        return new URL(url.toString());
    }
}

