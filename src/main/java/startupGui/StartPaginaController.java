package startupGui;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartPaginaController {

    private final DomeinController dc = QwixxApplication.dc;
    private static final int MIN_SPELERS_GEREGISTREERD = 2;

    @FXML
    private Label lblFeedbackSP;

    @FXML
    private Button btnStartSpelSP;

    @FXML
    private Button btnSpelerToevoegenSP;

    @FXML
    private Button btnSpelRegelsSP;

    @FXML
    private Button btnStatsSP;

    @FXML
    private void initialize() {
        int aantal = dc.geefGeregistreerdeSpelers().size();
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        if (aantal < MIN_SPELERS_GEREGISTREERD) {
            zetFeedback(bundle.getString("lblTeWeinigSpeler"), false);
            btnStartSpelSP.setDisable(true);
        } else {
            zetFeedback("", false);
            btnStartSpelSP.setDisable(false);
        }
    }
    private void zetFeedback(String tekst, boolean success) {
        boolean leeg = (tekst == null || tekst.isBlank());
        lblFeedbackSP.setText(tekst);
        lblFeedbackSP.setVisible(!leeg);
        lblFeedbackSP.setManaged(!leeg);
        lblFeedbackSP.getStyleClass().removeAll("feedbackSuccess", "feedbackError");
        if (!leeg) {
            lblFeedbackSP.getStyleClass().add(success ? "feedbackSuccess" : "feedbackError");
        }
    }

    //deze methode is gekoppeld aan de knop NL, en hij roept de laadSchermMetTaal methode op
    @FXML
    void wisselNaarNederlands(ActionEvent event) throws Exception {
        laadSchermMetTaal("nl", event);
    }

    @FXML
    void wisselNaarEngels(ActionEvent event) throws Exception {
        laadSchermMetTaal("en", event);
    }

    @FXML
    void wisselNaarRussisch(ActionEvent event) throws Exception {
        laadSchermMetTaal("ru", event);
    }

    private void laadSchermMetTaal(String taalCode, ActionEvent event) throws Exception {
        Locale locale = new Locale(taalCode); // dit maakt taal object aan en geeft een aanduider (taalcode) om bestand te vinden
        Locale.setDefault(locale); //dit zorgt ervoor dat de standaardtaal veranderd is

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", locale);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));
        loader.setResources(bundle);

        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void nieuwSpelKnopIngedrukt(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerKiezen.fxml"));

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnStartSpelSP.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void nieuweSpelerToevoegen(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerToevoegen.fxml"));

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnSpelerToevoegenSP.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void toonSpelregels(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/Spelregels.fxml"));

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnSpelRegelsSP.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void toonStats(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StatsPagina.fxml"));

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnStatsSP.getScene().getWindow();
        stage.setScene(scene);
    }
}