package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.Genre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import okhttp3.*;
import com.google.gson.*;

import java.net.MalformedURLException;
import java.net.URL;



public class MovieAPI {
    private static final String URL_API = "https://prog2.fh-campuswien.ac.at/movies";
    private static final String URL = "http://localhost:8080/movies";
    private static final String DELIMITER = "&"; //Separator


    public static class UrlBuilder {
        private StringBuilder url;

        public UrlBuilder() {
            url = new StringBuilder(URL_API);
        }

        public UrlBuilder withQuery(String query) {
            if (query != null && !query.isEmpty()) {
                url.append("?query=").append(query).append(DELIMITER);
            }
            return this;
        }

        public UrlBuilder withGenre(Genre genre) {
            if (genre != null) {
                url.append("genre=").append(genre).append(DELIMITER);
            }
            return this;
        }

        public UrlBuilder withReleaseYear(String releaseYear) {
            if (releaseYear != null) {
                url.append("releaseYear=").append(releaseYear).append(DELIMITER);
            }
            return this;
        }

        public UrlBuilder withRatingFrom(String ratingFrom) {
            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
            }
            return this;
        }

        public String build() {
            return url.toString();
        }
    }

    //damit wir die dann hinschicken können hehe
    private static String buildURL(String query, Genre genre, String releaseYear, String ratingFrom) {
        return new UrlBuilder()
                .withQuery(query)
                .withGenre(genre)
                .withReleaseYear(releaseYear)
                .withRatingFrom(ratingFrom)
                .build();
    }

    /*
    private static String buildURL(String query, Genre genre, String releaseYear, String ratingFrom){

        StringBuilder url = new StringBuilder(URL_API);

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
        return url.toString();
    }
     */

    //Ohne Parameter Aufgerufen wird dann:  //Übersichtlicher, damit man es nicht später in den anderen Methoden schreiben muss
    public static List<Movie> getAllMovies(){
        return getAllMovies(null, null, null, null);
    }

    //Movies herholen: request schicken -> response zurückbekommen

    public static List<Movie> getAllMovies(String query, Genre genre, String releaseYear, String ratingFrom){
        String url = buildURL(query, genre, releaseYear, ratingFrom);

        //Request bauen:
        //Müssen jetzt einen GET request schicken - mit Okhttp :)
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent") //Hint -> User_agent_Header -> remove
                .addHeader("User-Agent", "http.agent")
                .build();


        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()){
            //bekommen response in JSON format sollen es aber auf unsere Klassen "parsen" -> die Movies gleich draus machen
            String responseBody = response.body().string(); //nur body, dort wo die wichtigen Informationen für uns drinker sind.
            //GSON dafür zuständig, dass es ein JSON (das was im Response Body drinnen steht, automatisch auf eine bestimmte Klasse umändert/parsed.
            Gson gson = new Gson();
            //Movies erstellen
            Movie[] movies = gson.fromJson(responseBody, Movie[].class); //nimmt response body und wandelt es in ein Movie Array um, damit wir das dann in unserer APp benutzen können

            return Arrays.asList(movies);
        }
        catch (IOException ioe){
            MovieCell.showExceptionDialog(new MovieApiException("An error occurred while loading the movies")); //damit User erfährt, dass was falsch gelaufen ist.
        }
        return new ArrayList<>(); //damit es nicht abstürzt eine leere Liste übergeben.
    }

}
