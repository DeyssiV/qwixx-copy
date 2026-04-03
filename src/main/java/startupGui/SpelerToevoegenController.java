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
import java.sql.SQLIntegrityConstraintViolationException;

public class SpelerToevoegenController {
    private final DomeinController dc = QwixxApplication.dc;
    @FXML
    private Button btnAnnuleren;

    @FXML
    private Button btnRegistreren;

    @FXML
    private Label lblSpelerToevoegen;

    @FXML
    private Label lblFeedback;

    @FXML
    private TextField txfGeboortejaar;

    @FXML
    private TextField txfNaam;

    @FXML
    void annuleerRegistratie(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void startRegistratieSpeler(ActionEvent event) {
        lblFeedback.setText("");
        lblFeedback.setStyle("-fx-text-fill: black;");


        try {
            String naam = txfNaam.getText();
            String jaar = txfGeboortejaar.getText();

            if (naam.isEmpty() || txfGeboortejaar.getText().isEmpty()) {
                throw new IllegalArgumentException("Niets ingevuld!");
            }

            int geboortejaar = Integer.parseInt(jaar);

            dc.registreerSpeler(naam, geboortejaar);

            lblFeedback.setText("Speler '" + naam + "' is geregistreerd!");
            lblFeedback.setStyle("-fx-text-fill: green;");
            txfNaam.clear();
            txfGeboortejaar.clear();
        }catch (exceptions.GebruikersnaamInGebruikException e) {
            lblFeedback.setText(e.getMessage());
            lblFeedback.setStyle("-fx-text-fill: red;");
        }catch (NumberFormatException e) {
            lblFeedback.setText("Geboortejaar moet een getal zijn!");
            lblFeedback.setStyle("-fx-text-fill: red;");

        }catch (IllegalArgumentException e){
            lblFeedback.setText(e.getMessage());
            lblFeedback.setStyle("-fx-text-fill: red;");
        }catch (Exception e) {
        System.out.println("Gevangen exception type: " + e.getClass().getName());
        System.out.println("Boodschap: " + e.getMessage());

        lblFeedback.setText(e.getMessage());
        lblFeedback.setStyle("-fx-text-fill: red;");
    }

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));
        Stage stage = (Stage) btnRegistreren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));*/

    }

}
