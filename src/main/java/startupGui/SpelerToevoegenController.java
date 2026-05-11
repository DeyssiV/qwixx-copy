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
    private TextField txtGeboortejaarSTP, txtNaamSTP;

    @FXML
    void annuleer(ActionEvent event) throws Exception {
        dc.resetGekozenSpelers();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void startRegistratieSpeler(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());

        try {
            String naam = txtNaamSTP.getText();
            String jaar = txtGeboortejaarSTP.getText();

            if (naam.isEmpty() || jaar.isEmpty()) {
                throw new IllegalArgumentException(bundle.getString("NietsIngevuld"));
            }

            int geboortejaar = Integer.parseInt(jaar);
            dc.registreerSpeler(naam, geboortejaar);

            zetFeedback(bundle.getString("SpelerGeregistreerd"), true);
            txtNaamSTP.clear();
            txtGeboortejaarSTP.clear();

        } catch (NumberFormatException e) {
            zetFeedback(bundle.getString("GeboortejaarFout"), false);
        } catch (Exception e) {
            zetFeedback(e.getMessage(), false);
        }

    }

    private void zetFeedback(String tekst, boolean success) {
        boolean leeg = (tekst == null || tekst.isBlank());
        lblFeedbackSTP.setText(tekst);
        lblFeedbackSTP.setVisible(!leeg);
        lblFeedbackSTP.setManaged(!leeg);
        lblFeedbackSTP.getStyleClass().removeAll("feedbackSuccess", "feedbackError");
        if (!leeg) {
            lblFeedbackSTP.getStyleClass().add(success ? "feedbackSuccess" : "feedbackError");
        }
    }

}
