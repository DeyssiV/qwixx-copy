package startupGui;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SpelerToevoegenController {
    private final DomeinController dc = QwixxApplication.dc;
    @FXML
    private Button btnAnnuleren;

    @FXML
    private Button btnRegistreren;

    @FXML
    private Label lblSpelerToevoegen;

    @FXML
    private TextField txfGeboortejaar;

    @FXML
    private TextField txfNaam;

    @FXML
    void annuleerRegistratie(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));
        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void startRegistratieSpeler(ActionEvent event) throws Exception {
        String naam = txfNaam.getText();
        int geboortejaar = Integer.parseInt(txfGeboortejaar.getText());
        dc.registreerSpeler(naam, geboortejaar);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));
        Stage stage = (Stage) btnRegistreren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

}
