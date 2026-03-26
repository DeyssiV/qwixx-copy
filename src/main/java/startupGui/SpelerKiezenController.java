package startupGui;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class SpelerKiezenController {

    private final DomeinController dc = QwixxApplication.dc;

    @FXML
    private ComboBox<String> cmbLijstSpelers;

    @FXML
    private ListView<String> lvGekozenSpelers;

    @FXML
    private Button btnAnnuleer;

    @FXML
    private Button btnStartSpel;

    @FXML
    private void initialize() {
        cmbLijstSpelers.getItems().addAll(dc.geefGeregistreerdeSpelers());
    }

    @FXML
    void voegSpelerToe(ActionEvent event) {
        String gekozenSpeler = cmbLijstSpelers.getValue();
        if (gekozenSpeler != null) {
            dc.voegSpelerToeAanSpel(gekozenSpeler);
            lvGekozenSpelers.getItems().add(gekozenSpeler);
        }
    }

    @FXML
    void verwijderSpeler(ActionEvent event) {
        String geselecteerde = lvGekozenSpelers.getSelectionModel().getSelectedItem();
        if (geselecteerde != null) {
            lvGekozenSpelers.getItems().remove(geselecteerde);
            dc.verwijderSpelerUitSpel(geselecteerde);
        }
    }

    @FXML
    void annuleer(ActionEvent event) throws Exception {
        dc.resetGekozenSpelers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));
        Stage stage = (Stage) btnAnnuleer.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void startSpel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nog niet beschikbaar");
        alert.setContentText("Dit is nog niet geïmplementeerd.");
        alert.showAndWait();
    }
}