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
import javafx.scene.Node;
import javafx.scene.Parent;
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
import utils.Kleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static startupGui.QwixxApplication.dc;

public class SpelController {

    private int ronde = 1; //wit = 1; gekleurd = 2
    private int huidigeSpelerIndex = 0; //de speler van wie de scoreblad getoond wordt
    private String actieveSpeler; //speler die dobbelstenen gerold heeft

    private boolean heeftGerold = false;

    @FXML
    private Label lblTotaleScore,lblTitelSPEL;

    @FXML
    private Button btnAnnuleren, btnRolDobbelstenen;

    @FXML
    private GridPane grpMislukteWorpen, grpScorekaart, grpDobbelstenen;

    /**
     * Initialiseert de controller bij het laden van het scherm.
     */
    @FXML
    private void initialize() {

        grpScorekaart.setVgap(5);
        grpScorekaart.setHgap(2);
        vulScorekaart(dc.isAlternatiefModus());
        updateSpelerAanDeBeurt();

        updateMislukteWorpen();
        updateScore();
    }

    /**
     * Keert terug naar de startpagina van de applicatie.
     * @param event Klikken van de knop zorgt voor event -> naar startpagina teruggaan.
     * @throws Exception
     */
    @FXML
    void annuleer(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }


    /**
     * Vult het scoreblad in met de juiste nummers obv keuze (klassiek, alternatief)
     * @param isAlternatief Geeft aan of de alternatieve getallenreeksen gebruikt moeten worden.
     */
    private void vulScorekaart(boolean isAlternatief) {
        String[] kleuren = {"ROOD", "GEEL", "GROEN", "BLAUW"};
        int[][] waarden;

        if (isAlternatief) {
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
            String hexAchtergrond;
            String hexTekst;

            switch (kleurNaam) {
                case "rood":
                    hexAchtergrond = "#fADBD8";
                    hexTekst = "#C0392B";
                    break;
                case "geel":
                    hexAchtergrond = "#FCF3CF";
                    hexTekst = "#D4AC0D";
                    break;
                case "groen":
                    hexAchtergrond = "#D5F5E3";
                    hexTekst = "#1E8449";
                    break;
                case "blauw":
                    hexAchtergrond = "#D6EAF8";
                    hexTekst = "#2E86C1";
                    break;
                default:
                    hexAchtergrond = "#E5E8E8";
                    hexTekst = "#7F8C8D";
            }


            String buttonStyle = "-fx-background-color: " + hexAchtergrond + "; " +
                    "-fx-text-fill: " + hexTekst + "; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-color: rgba(0,0,0,0.05); " +
                    "-fx-border-radius: 8; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 23px;";


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

    /**
     * Verandert de label zodat het overeenkomt met de speler van wie het scoreblad wordt getoond (speler die aan de beurt is)
     */
    private void updateSpelerAanDeBeurt() {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        String tekst = String.format(bundle.getString("LabelBeurtVan"), dc.getSpelerAanDeBeurt());

        lblTitelSPEL.setText(tekst);
    }



    /**
     * Behandelt het rollen van de dobbelstenen of het overslaan van de huidige beurt.
     * @param event Klikken van de knop zorgt voor event -> dobbelstenen rollen.
     * @throws Exception
     */
    @FXML
    void rolDobbelstenen(ActionEvent event) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        Button knop = (Button) event.getSource();

        if (!heeftGerold) {
            actieveSpeler = dc.getSpelerAanDeBeurt();
            huidigeSpelerIndex = dc.getHuidigeSpelerIndex();
            ronde = 1;

            knop.setDisable(true);
            dc.startBeurt();
            toonDobbelstenen(dc.getDobbelstenen());
            heeftGerold = true;
            omrandMogelijkeVakjes("W");

            PauseTransition delay = new PauseTransition(Duration.seconds(1  ));
            delay.setOnFinished(e -> {
                knop.setText(bundle.getString("BeurtOverslaan"));
                knop.setDisable(false);
            });
            delay.play();

        } else {
            if(ronde==1){
                dc.volgendeSpeler();

                if (dc.getSpelerAanDeBeurt().equals(actieveSpeler)) {
                    ronde = 2;
                    volgendeBeurt(false);
                    omrandMogelijkeVakjes("G");
                } else {
                    volgendeBeurt(false);
                    omrandMogelijkeVakjes("W");
                }

            }else{
                dc.voegMislukteWorpToe(actieveSpeler);
                if (dc.geefAantalMislukteWorpen(actieveSpeler) >= 2) {
                    controleerEindeSpel();
                } else {
                    ronde = 1;
                    volgendeBeurt(true);
                }
            }

        }
    }


    /**
     * Toont de visuele weergave van de gerolde dobbelstenen.
     * @param dobbelstenen Lijst met dobbelstenen die gerold zijn.
     */
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


    /**
     * Bepaalt welke vakjes omrand worden adhv de beurt en speler.
     * @param type "W" voor de witte som ronde, "G" voor de gekleurde ronde van de speler aan de beurt/ die gerold heeft..
     */
    private void omrandMogelijkeVakjes(String type) {
        resetScorekaartRanden(); // scoreblad resetten van de omrande vakjes
        List<Dobbelsteen> dobbelstenen = dc.getDobbelstenen();

        if (type.equals("W")) {
            int witteSom = dc.geefWitteSom();
            for (String kleurDb : new String[]{"ROOD", "GEEL", "GROEN", "BLAUW"}) {
                highlightButton(kleurDb, witteSom);
            }
        } else if (type.equals("G")) {
            List<Integer> witteWaarden = new ArrayList<>();
            for (Dobbelsteen ds : dobbelstenen) {
                if (ds.getKleur() == Kleur.WIT) witteWaarden.add(ds.getWaarde());
            }

            for (Dobbelsteen ds : dobbelstenen) {
                if (ds.getKleur() != Kleur.WIT) {
                    int gekleurdeWaarde = ds.getWaarde();
                    String kleurNaam = ds.getKleur().toString();

                    for (int witteWaarde : witteWaarden) {
                        highlightButton(kleurNaam, gekleurdeWaarde + witteWaarde);
                    }
                }
            }
        }
    }

    /**
     * Voegt een gekleurde rand toe aan een specifieke vakje in de scorekaart.
     * @param kleur De kleur van de rij.
     * @param getal De waarde van het vakje.
     */
    private void highlightButton(String kleur, int getal) {
        for (Node node : grpScorekaart.getChildren()) {
            if (node instanceof Button buttonInScoreKaart) {
                String btnKleur = (String) buttonInScoreKaart.getProperties().get("kleur");
                Integer btnGetal = (Integer) buttonInScoreKaart.getProperties().get("getal");

                if (kleur.equals(btnKleur) && btnGetal != null && btnGetal == getal && !buttonInScoreKaart.isDisable()) {

                    String donkereRandKleur;
                    switch (kleur.toUpperCase()) {
                        case "ROOD"  -> donkereRandKleur = "#c0392b";
                        case "GEEL"  -> donkereRandKleur = "#f39c12";
                        case "GROEN" -> donkereRandKleur = "#1e8449";
                        case "BLAUW" -> donkereRandKleur = "#1a5276";
                        default      -> donkereRandKleur = "#34495e";
                    }
                    buttonInScoreKaart.setStyle(buttonInScoreKaart.getStyle() + String.format("-fx-border-width: 3px; -fx-border-color: %s;", donkereRandKleur));
                }
            }
        }
    }


    /**
     * Actie verwerken zodra er op een vakje wordt geklikt.
     * Het verwerkt het kruisje via domeinklasses en gaat door naar volgende beurt/speler.
     * @param event Klikken van de knop zorgt voor event ->vakje aankruisen.
     */
    @FXML
    private void handleScoreClick(ActionEvent event) {

        Button geklikteKnop = (Button) event.getSource();

        if (!heeftGerold || !geklikteKnop.getStyle().contains("-fx-border-width: 3px")) {
            return;
        }

        String kleur = (String) geklikteKnop.getProperties().get("kleur");
        int getal = (Integer) geklikteKnop.getProperties().get("getal");

        dc.kiesRij(dc.getSpelerAanDeBeurt(), kleur, getal);

        geklikteKnop.setText("X");
        geklikteKnop.setDisable(true);

        //de vakjes links van aangekruiste niet meer klikbaar maken
        int rij = GridPane.getRowIndex(geklikteKnop);
        int kolom = GridPane.getColumnIndex(geklikteKnop);

        for (Node node:grpScorekaart.getChildren()){
            if (node instanceof Button btn) {
                Integer nodeRij = GridPane.getRowIndex(node);
                Integer nodeKolom = GridPane.getColumnIndex(node);

                if (nodeRij != null && nodeRij == rij && nodeKolom != null && nodeKolom < kolom) {
                    btn.setDisable(true);
                    btn.setOpacity(0.3);
                }
            }
        }

        resetScorekaartRanden();
        updateScore();

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(e -> {
                try {
                    if (ronde == 1) {
                        dc.volgendeSpeler();
                        if (dc.getSpelerAanDeBeurt().equals(actieveSpeler)) {
                            ronde = 2;
                            volgendeBeurt(false);
                            omrandMogelijkeVakjes("G");
                        } else {
                            volgendeBeurt(false);
                            omrandMogelijkeVakjes("W");
                        }
                    } else {
                        if (dc.isEindeSpelDoorRijDoorkruising()) {
                            controleerEindeSpel();
                        } else {
                            dc.volgendeSpeler();
                            heeftGerold = false;
                            volgendeBeurt(true);
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
        });
        delay.play();
    }


    /**
     * Reset de UI componenten voor de volgende beurt.
     * @param isEindeBeurt Toont aan of de beurt van speler gedaan is om de dobbelstenen te vernieuwen.
     */
    private void volgendeBeurt(boolean isEindeBeurt) {

        resetScorekaartRanden();
        updateSpelerAanDeBeurt();
        updateMislukteWorpen();
        updateScore();
        updateScorekaartVisueel();

        if (isEindeBeurt){
            heeftGerold = false;
            grpDobbelstenen.getChildren().clear();

            ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
            btnRolDobbelstenen.setText(bundle.getString("RolDobbelstenen"));
        }

    }

    /**
     * Controleert of het spel beëindigd moet worden en navigeert naar de GameOver pagina.
     * @throws Exception
     */
    private void controleerEindeSpel() throws Exception {
        String huidigeNaam = dc.getSpelerAanDeBeurt();
        int aantalFouten = dc.geefAantalMislukteWorpen(huidigeNaam);

        if (aantalFouten >= 2 || dc.isEindeSpelDoorRijDoorkruising()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/GameOver.fxml"));

            ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) lblTitelSPEL.getScene().getWindow();
            stage.setScene(scene);
        }

    }

    /**
     * Vernieuwt het scoreblad bij (mbv de domeinklasses).
     * Zet kruisjes en blokkeert vakjes die niet meer te kiezen zijn.
     */
    private void updateScorekaartVisueel() {
        String huidigeSpeler = dc.getSpelerAanDeBeurt();
        int[][] status = dc.geefStatus(huidigeSpeler);

        for (Node node : grpScorekaart.getChildren()) {
            if (node instanceof Button btn) {
                Integer r = GridPane.getRowIndex(node);
                Integer k = GridPane.getColumnIndex(node);

                if (r != null && k != null && k < 11) {
                    if (status[r][k] == 1) {
                        btn.setText("X");
                        btn.setDisable(true);
                        btn.setOpacity(1.0);
                    } else {
                        btn.setText(String.valueOf(btn.getProperties().get("getal")));

                        boolean geblokkeerdDoorRechts = false;
                        for (int i = k + 1; i < 11; i++) {
                            if (status[r][i] == 1) {
                                geblokkeerdDoorRechts = true;
                                break;
                            }
                        }

                        btn.setDisable(geblokkeerdDoorRechts);
                        btn.setOpacity(geblokkeerdDoorRechts ? 0.3 : 1.0);
                    }
                }
            }
        }
    }



    /**
     * Reset de scoreblad bij elke beurt
     */
    private void resetScorekaartRanden() {
        grpScorekaart.getChildren().forEach(node -> {
            if (node instanceof Button btn) {
                String huidigeStyle = btn.getStyle();
                String schoneStyle = huidigeStyle.replaceAll("-fx-border-width:.*", "");
                btn.setStyle(schoneStyle.trim());
            }
        });
    }


    /**
     * Toont de mislukte worpen visueel.
     */
    private void updateMislukteWorpen() {
        grpMislukteWorpen.getChildren().clear();
        String huidigeNaam = dc.getSpelerAanDeBeurt();
        int aantalMislukt = dc.geefAantalMislukteWorpen(huidigeNaam);

        for (int i = 0; i < 2; i++) {
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


    /**
     * Update het label met de huidige score van de speler die aan de beurt is.
     */
    @FXML
    private void updateScore() {
        String huidigeNaam = dc.getSpelerAanDeBeurt();
        int score = dc.geefTotaleScore(huidigeNaam);
        lblTotaleScore.setText(String.valueOf(score));
    }



}


