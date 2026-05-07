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
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartPaginaController {

    private final DomeinController dc = QwixxApplication.dc;

    @FXML
    private Button btnNieuwSpel;

    @FXML
    private Button btnNieuweSpeler;

    @FXML
    private Button btnToonSpelregels;

    @FXML
    private Button btnStats;

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
        Locale locale = new Locale(taalCode); // dit maakt taal-object aan, en geeft een aanduider (taalcode) om bestand te vinden
        Locale.setDefault(locale); //dit zorgt ervoor dat de standaardtaal veranderd is

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", locale);
        //dit gaat zoeken in de resources map een bestand (.properties) met de naam "localistaie" en met de taalcode
        //een soort van woordenboek

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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnNieuwSpel.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void nieuweSpelerToevoegen(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerToevoegen.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnNieuweSpeler.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void toonSpelregels(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/Spelregels.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnToonSpelregels.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void toonStats(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StatsPagina.fxml"));

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnStats.getScene().getWindow();
        stage.setScene(scene);
    }
}