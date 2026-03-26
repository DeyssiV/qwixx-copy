package startupGui;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartPaginaController {

    private final DomeinController dc = QwixxApplication.dc;

    @FXML
    private Button btnNieuwSpel;

    @FXML
    private Button btnNieuweSpeler;

    @FXML
    void nieuwSpelKnopIngedrukt(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerKiezen.fxml"));
        Stage stage = (Stage) btnNieuwSpel.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    void nieuweSpelerToevoegen(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/SpelerToevoegen.fxml"));
        Stage stage = (Stage) btnNieuweSpeler.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}