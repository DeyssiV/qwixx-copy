package startupGui;

import domein.DomeinController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static startupGui.QwixxApplication.dc;


public class GekozenSpelersPaginaController implements Initializable {

    @FXML
    private Button btnAnnuleren, btnStartSpel;

    @FXML
    private ListView<String> lijstGekozenSpelers;


    @FXML
    void annuleer(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    private void startSpel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/Spel.fxml"));

            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
            loader.setResources(bundle);

            Stage stage = (Stage) btnStartSpel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lijstGekozenSpelers.setItems(FXCollections.observableArrayList(
                dc.volgordeSpelersInStringLijst()
        ));

        lijstGekozenSpelers.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String naam, boolean empty) {
                super.updateItem(naam, empty);
                if (empty || naam == null) {
                    setText(null);
                } else {
                    setText((getIndex() + 1) + ". " + naam);
                }
            }
        });

        lijstGekozenSpelers.setSelectionModel(null);
    }
}