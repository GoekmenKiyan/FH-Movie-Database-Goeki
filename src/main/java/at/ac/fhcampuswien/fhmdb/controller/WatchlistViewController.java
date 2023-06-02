package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.FhmdbApplication;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class WatchlistViewController {
    @FXML
    public VBox mainVBox;
    @FXML
    public JFXListView movieWatchlistView;
    @FXML
    public JFXButton mainPageBtn;
    WatchlistRepository repository;

   public void initialize() {
       System.out.println("WatchlistViewController initialized");

       //repository = new WatchlistRepository();
       repository = WatchlistRepository.getInstance();
       List<WatchlistEntity> movieEntities = new ArrayList<>();

       try {
           movieEntities = repository.getAll();
       } catch (SQLException e) {
           MovieCell.showExceptionDialog(new DatabaseException("Problem with the Database"));
       }

       for (WatchlistEntity element : movieEntities) {
           System.out.println(element);
       }

       ObservableList<Movie> observableMovies = FXCollections.observableArrayList(
               movieEntities.stream()
                       .map(WatchlistEntity::toMovie)
                       .collect(Collectors.toList()));


       movieWatchlistView.setItems(observableMovies);   // set the items of the listview to the observable list
       movieWatchlistView.setCellFactory(movieListView -> new MovieCell(true)); // apply custom cells to the listview


   }



        // SWITCH SCENE:
    public void switchToMainPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 890, 620);
            Stage stage = (Stage)mainVBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            //System.err.println("Error while loading main page.");
            MovieCell.showExceptionDialog(new IOException("Error occurred while loading"));
        }
    }
}
