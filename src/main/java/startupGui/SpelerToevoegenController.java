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
import java.util.Locale;
import java.util.ResourceBundle;

public class SpelerToevoegenController {
    private final DomeinController dc = QwixxApplication.dc;
    @FXML
    private Button btnAnnuleren;

    @FXML
    private Label lblFeedbackSTP;

    @FXML
    private TextField txtGeboortejaarSTP;

    @FXML
    private TextField txtNaamSTP;

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
        lblFeedbackSTP.setText("");
        lblFeedbackSTP.setStyle("-fx-text-fill: black;");

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());


        try {
            String naam = txtNaamSTP.getText();
            String jaar = txtGeboortejaarSTP.getText();

            if (naam.isEmpty() || jaar.isEmpty()) {
                throw new IllegalArgumentException(bundle.getString("NietsIngevuld"));
            }

            int geboortejaar = Integer.parseInt(jaar);

            dc.registreerSpeler(naam, geboortejaar);

            lblFeedbackSTP.setText(bundle.getString("SpelerGeregistreerd"));
            lblFeedbackSTP.setStyle("-fx-text-fill: green;");
            txtNaamSTP.clear();
            txtGeboortejaarSTP.clear();
        }catch (exceptions.GebruikersnaamInGebruikException e) {
            lblFeedbackSTP.setText(e.getMessage());
            lblFeedbackSTP.setStyle("-fx-text-fill: red;");
        }catch (NumberFormatException e) {
            lblFeedbackSTP.setText(bundle.getString("GeboortejaarFout"));
            lblFeedbackSTP.setStyle("-fx-text-fill: red;");

        }catch (IllegalArgumentException e){
            lblFeedbackSTP.setText(e.getMessage());
            lblFeedbackSTP.setStyle("-fx-text-fill: red;");
        }catch (Exception e) {
            lblFeedbackSTP.setText(e.getMessage());
            lblFeedbackSTP.setStyle("-fx-text-fill: red;");
    }

    }

}
