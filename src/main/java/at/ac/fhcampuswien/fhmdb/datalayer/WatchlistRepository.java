package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

// EXTRA LAYER TO ACCESS DAO FROM DATABASE
public class WatchlistRepository {
    private static WatchlistRepository instance;
    private Dao<WatchlistEntity, Long> dao;

    private WatchlistRepository() {
        // Exception handling code here
        this.dao = Database.getDatabase().getWatchlistDao();
    }

    public static synchronized WatchlistRepository getInstance() {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    // Rest of the class implementation...

    public void addToWatchlist(Movie movie) throws SQLException {
        // addToWatchlist implementation...
        String title = movie.getTitle().replace("'", "''");
        if (dao.queryForEq("title", title).isEmpty()) {
            dao.create(movieToWatchlistEntity(movie));
            System.out.println("Added " + movie.getTitle() + " to Watchlist");
        }
    }

    public void removeFromWatchlist(Movie movie) throws SQLException {
        // removeFromWatchlist implementation...
        String title = movie.getTitle().replace("'", "''");
        List<WatchlistEntity> movies = dao.queryForEq("title", title);
        if (!movies.isEmpty()) {
            dao.delete(movies);
            System.out.println("Deleted " + movie.getTitle() + " from Watchlist");
        }
    }

    public List<WatchlistEntity> getAll() throws SQLException {
        // getAll implementation...
        return dao.queryForAll();
    }

    private WatchlistEntity movieToWatchlistEntity(Movie movie) {
        // movieToWatchlistEntity implementation...
        return new WatchlistEntity(movie.getApiId(),movie.getTitle(),movie.getDescription(),WatchlistEntity.genresToString(movie.getGenres()), movie.getReleaseYear(),movie.getImgUrl(),movie.getLengthInMinutes(), movie.getRating());
    }
}
