package startupGui;

import domein.Dobbelsteen;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static startupGui.QwixxApplication.dc;

public class SpelController {

    private boolean heeftGerold = false;

    @FXML
    private GridPane grpDobbelstenen;

    @FXML
    private Label lblTotaleScore;

    @FXML
    private Button btnStop;

    @FXML
    private GridPane grpMislukteWorpen;

    @FXML
    private GridPane grpScorekaart;

    @FXML
    private Label lblSpelerAanDeBeurt;

    private void updateSpelerAanDeBeurt() {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        String tekst = String.format(bundle.getString("LabelBeurtVan"), dc.getSpelerAanDeBeurt());

        lblSpelerAanDeBeurt.setText(tekst);    }

    @FXML
    void stopSpel(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnStop.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    @FXML
    private void handleScoreClick(ActionEvent event) {

        Button geklikteKnop = (Button) event.getSource();


        String kleur = (String) geklikteKnop.getProperties().get("kleur");
        Integer getal = (Integer) geklikteKnop.getProperties().get("getal");

        System.out.println("Geklikt op: " + kleur + " " + getal);


        geklikteKnop.setText("X");
        geklikteKnop.setDisable(true);


    }

    private void updateMislukteWorpen() {
        grpMislukteWorpen.getChildren().clear();
        String huidigeNaam = dc.getSpelerAanDeBeurt();
        int aantalMislukt = dc.geefAantalMislukteWorpen(huidigeNaam);

        for (int i = 0; i < 4; i++) {
            Label vakje = new Label();
            vakje.setMinSize(40, 40);
            vakje.setAlignment(Pos.CENTER);

            if (i < aantalMislukt) {
                vakje.setText("X");
                vakje.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                        "-fx-alignment: center; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-color: #c0392b; -fx-border-radius: 5;");
            } else {
                vakje.setText("");
                vakje.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5;");
            }

            grpMislukteWorpen.add(vakje, i, 0);
        }
    }



    public void toonDobbelstenen(List<Dobbelsteen> dobbelstenen) {
        grpDobbelstenen.getChildren().clear();

        for (int i = 0; i < dobbelstenen.size(); i++) {
            Dobbelsteen ds = dobbelstenen.get(i);
            Button dbsteen = new Button(String.valueOf(ds.getWaarde()));

            // Zet de kleur om naar kleine letters zodat de switch altijd matcht
            String kleurNaam = String.valueOf(ds.getKleur()).toLowerCase();

            String achtergrondKleur;
            String tekstKleur = "white";

            switch (kleurNaam) {
                case "rood":   achtergrondKleur = "#e74c3c"; break;
                case "geel":   achtergrondKleur = "#f1c40f"; tekstKleur = "black"; break;
                case "groen":  achtergrondKleur = "#27ae60"; break;
                case "blauw":  achtergrondKleur = "#2980b9"; break;
                case "wit":    achtergrondKleur = "#ffffff"; tekstKleur = "black"; break;
                default:       achtergrondKleur = "#ecf0f1"; tekstKleur = "black"; // Backup grijs
            }

            dbsteen.setStyle(
                    "-fx-background-color: " + achtergrondKleur + "; " +
                            "-fx-text-fill: " + tekstKleur + "; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 24px; " +
                            "-fx-background-radius: 12; " +
                            "-fx-min-width: 60px; " +
                            "-fx-min-height: 60px; " +
                            "-fx-border-color: rgba(0,0,0,0.2); " + // Subtiel randje voor diepte
                            "-fx-border-radius: 12;"
            );

            grpDobbelstenen.add(dbsteen, i, 0);

            GridPane.setHalignment(dbsteen, HPos.CENTER);
            GridPane.setValignment(dbsteen, VPos.CENTER);
            GridPane.setMargin(dbsteen, new Insets(10));
        }
    }

    private void vulScorekaart(boolean alternatief) {
        String[] kleuren = {"ROOD", "GEEL", "GROEN", "BLAUW"};
        int[][] waarden;

        if (alternatief) {
            waarden = new int[][]{
                    {10, 6, 2, 8, 3, 4, 12, 5, 9, 7, 11},
                    {9, 12, 4, 6, 2, 5, 8, 11, 3, 10, 7},
                    {8, 2, 10, 12, 6, 9, 7, 4, 5, 11, 3},
                    {5, 7, 11, 9, 12, 3, 8, 10, 2, 6, 4}
            };
        } else {
            waarden = new int[][]{
                    {2,3,4,5,6,7,8,9,10,11,12},
                    {2,3,4,5,6,7,8,9,10,11,12},
                    {12,11,10,9,8,7,6,5,4,3,2},
                    {12,11,10,9,8,7,6,5,4,3,2}
            };
        }

        for (int rij = 0; rij < 4; rij++) {
            // Bepaal de kleur voor deze hele rij
            String kleurNaam = kleuren[rij].toLowerCase();
            String hexKleur;
            switch (kleurNaam) {
                case "rood":
                    hexKleur = "#f1948a"; // Iets donkerder rood (past bij #e74c3c)
                    break;
                case "geel":
                    hexKleur = "#f7dc6f"; // Iets donkerder geel (past bij #f1c40f)
                    break;
                case "groen":
                    hexKleur = "#82e0aa"; // Iets donkerder groen (past bij #27ae60)
                    break;
                case "blauw":
                    hexKleur = "#85c1e9"; // Iets donkerder blauw (past bij #2980b9)
                    break;
                default:
                    hexKleur = "#d5dbdb"; // Lichtgrijs
            }


            String buttonStyle = "-fx-background-color: " + hexKleur + "; " +
                    "-fx-background-radius: 8; " + // Iets ronder staat moderner
                    "-fx-border-color: rgba(0,0,0,0.1); " + // Subtiel randje
                    "-fx-border-radius: 8; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 23px;";

//            Label lblKleur = new Label(kleuren[rij]);
//            lblKleur.setFont(new Font("Calibri", 18));
//            grpScorekaart.add(lblKleur, 0, rij);
//            GridPane.setHalignment(lblKleur, HPos.RIGHT);
//            GridPane.setMargin(lblKleur, new Insets(0, 10, 0, 0));

            for (int kolom = 0; kolom < 11; kolom++) {
                int getal = waarden[rij][kolom];
                String kleur = kleuren[rij];

                Button btn = new Button(String.valueOf(getal));


                btn.setStyle(buttonStyle);

                btn.getProperties().put("kleur", kleur);
                btn.getProperties().put("getal", getal);
                btn.setOnAction(this::handleScoreClick);

                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setMaxHeight(Double.MAX_VALUE);
                grpScorekaart.add(btn, kolom , rij);

                GridPane.setHalignment(btn, HPos.CENTER);
                GridPane.setValignment(btn, VPos.CENTER);
                GridPane.setHgrow(btn, Priority.ALWAYS);
                GridPane.setVgrow(btn, Priority.ALWAYS);
            }


            Button lock = new Button("(L)");


            lock.setStyle(buttonStyle);
            lock.getProperties().put("type", "LOCK");
            lock.getProperties().put("kleur", kleuren[rij]);
            lock.setOnAction(this::handleScoreClick);

            lock.setMaxWidth(Double.MAX_VALUE);
            lock.setMaxHeight(Double.MAX_VALUE);
            grpScorekaart.add(lock, 11, rij);

            GridPane.setHalignment(lock, HPos.CENTER);
            GridPane.setValignment(lock, VPos.CENTER);
            GridPane.setHgrow(lock, Priority.ALWAYS);
            GridPane.setVgrow(lock, Priority.ALWAYS);
        }
    }



    @FXML
    void rolDobbelstenen(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());

        Button knop = (Button) event.getSource();

        if (!heeftGerold) {
            knop.setDisable(true);

            dc.startBeurt();
            toonDobbelstenen(dc.getDobbelstenen());

            heeftGerold = true;

            PauseTransition delay = new PauseTransition(Duration.seconds(1  ));
            delay.setOnFinished(e -> {
                knop.setText(bundle.getString("BeurtOverslaan"));
                knop.setDisable(false);
            });
            delay.play();

        } else {
            heeftGerold = false;
            knop.setText(bundle.getString("RolDobbelstenen"));
            grpDobbelstenen.getChildren().clear();
        }
    }

    @FXML
    private void updateScore() {
        String huidigeNaam = dc.getSpelerAanDeBeurt();
        int score = dc.geefTotaleScore(huidigeNaam);
        lblTotaleScore.setText(String.valueOf(score));
    }


    @FXML
    private void initialize() {

        vulScorekaart(dc.isAlternatiefModus());
        updateSpelerAanDeBeurt();

        updateMislukteWorpen();
        updateScore();
    }
}


