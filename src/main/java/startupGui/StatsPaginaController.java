package startupGui;

import domein.DomeinController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatsPaginaController implements Initializable {

    private final DomeinController dc = QwixxApplication.dc;

    @FXML
    private Button btnAnnuleren;

    @FXML
    private TableColumn<String[], String> kolomNaamSTATP;

    @FXML
    private TableColumn<String[], Integer> kolomAantalGespeeldSTATP;

    @FXML
    private TableColumn<String[], Integer> kolomAantalGewonnenSTATP;

    @FXML
    private TableView<String[]> tabelSpelersSTATP;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Kolommen instellen
        kolomNaamSTATP.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        kolomAantalGespeeldSTATP.setCellValueFactory(data -> new SimpleIntegerProperty(0).asObject()); // nog leeg
        kolomAantalGewonnenSTATP.setCellValueFactory(data -> new SimpleIntegerProperty(0).asObject()); // nog leeg

        // Spelers laden vanuit database
        ObservableList<String[]> rijen = FXCollections.observableArrayList();
        for (String naam : dc.geefGeregistreerdeSpelers()) {
            rijen.add(new String[]{naam});
        }

        tabelSpelersSTATP.setItems(rijen);
        tabelSpelersSTATP.setSelectionModel(null);
    }

    @FXML
    void annuleer(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}