package startupGui;

import domein.Dobbelsteen;
import dtos.SpelDTO;
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
import javafx.scene.layout.Priority;

import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Kleur;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static startupGui.QwixxApplication.dc;

public class SpelController {

    private int ronde = 1;
    private String actieveSpeler;
    private boolean wachtOpVolgendeBeurt = false;
    private boolean heeftGerold = false;

    @FXML
    private Label lblTotaleScore,lblTitelSPEL, lblFeedback5kruisjes;

    @FXML
    private Button btnAnnuleren, btnRolDobbelstenen;

    @FXML
    private GridPane grpMislukteWorpen, grpScorekaart, grpDobbelstenen;

    @FXML
    private void initialize() {

        grpScorekaart.setVgap(5);
        grpScorekaart.setHgap(2);
        vulScorekaart(dc.isAlternatiefModus());
        refreshSpelerAanBeurt();
        zetFeedback5("", true);
    }

    @FXML
    void annuleer(ActionEvent event) throws Exception {
        dc.resetSpel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localisatie");
        loader.setResources(bundle);

        Stage stage = (Stage) btnAnnuleren.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    private void vulScorekaart(boolean isAlternatief) {
        String[] kleuren = {"ROOD", "GEEL", "GROEN", "BLAUW"};
        int[][] waarden;

        if (isAlternatief) {
            waarden = new int[][]{
                    {10,6,2,8,3,4,12,5,9,7,11},
                    {9,12,4,6,2,5,8,11,3,10,7},
                    {8,2,10,12,6,9,7,4,5,11,3},
                    {5,7,11,9,12,3,8,10,2,6,4}
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
            String kleurNaam = kleuren[rij].toLowerCase();
            String hexAchtergrond;
            String hexTekst = switch (kleurNaam) {
                case "rood" -> {
                    hexAchtergrond = "#fADBD8";
                    yield "#C0392B";
                }
                case "geel" -> {
                    hexAchtergrond = "#FCF3CF";
                    yield "#D4AC0D";
                }
                case "groen" -> {
                    hexAchtergrond = "#D5F5E3";
                    yield "#1E8449";
                }
                case "blauw" -> {
                    hexAchtergrond = "#D6EAF8";
                    yield "#2E86C1";
                }
                default -> {
                    hexAchtergrond = "#E5E8E8";
                    yield "#7F8C8D";
                }
            };

            String buttonStyle = "-fx-background-color: " + hexAchtergrond + "; " +
                    "-fx-text-fill: " + hexTekst + "; " +
                    "-fx-background-radius: 8; " +
                    "-fx-border-color: rgba(0,0,0,0.05); " +
                    "-fx-border-radius: 8; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 23px;";


            for (int kolom = 0; kolom < waarden[rij].length; kolom++) {
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
            lock.setOnAction(this::handleLockClick);

            lock.setMaxWidth(Double.MAX_VALUE);
            lock.setMaxHeight(Double.MAX_VALUE);
            grpScorekaart.add(lock, waarden[rij].length, rij);

            GridPane.setHalignment(lock, HPos.CENTER);
            GridPane.setValignment(lock, VPos.CENTER);
            GridPane.setHgrow(lock, Priority.ALWAYS);
            GridPane.setVgrow(lock, Priority.ALWAYS);
        }
    }

    private void refreshSpelerAanBeurt() {
        SpelDTO status = dc.geefSpelStatus();
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        lblTitelSPEL.setText(String.format(bundle.getString("LabelBeurtVan"), status.spelerAanDeBeurt()));
        lblTotaleScore.setText(String.valueOf(status.totaleScore()));
        updateMislukteWorpen(status.aantalMislukteWorpen());
    }


    private void gaNaarVolgendeBeurt() throws Exception {
        if (ronde == 1) {
            dc.toonVolgendeSpeler();
            if (dc.geefSpelerAanDeBeurt().equals(actieveSpeler)) {
                ronde = 2;
                volgendeBeurt(false);
                omrandMogelijkeVakjes("G");
            } else {
                volgendeBeurt(false);
                omrandMogelijkeVakjes("W");
            }
        } else {
            if (dc.isEindeSpelDoorRijDoorkruising()) {
                controleerEindeSpel(actieveSpeler);
            } else {
                dc.toonVolgendeSpeler();
                heeftGerold = false;
                volgendeBeurt(true);
            }
        }
    }

    @FXML
    void rolDobbelstenen(ActionEvent event) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        Button knop = (Button) event.getSource();

        if (wachtOpVolgendeBeurt) {
            wachtOpVolgendeBeurt = false;
            zetFeedback5("", true);
            gaNaarVolgendeBeurt();
            return;
        }

        if (!heeftGerold) {
            actieveSpeler = dc.geefSpelerAanDeBeurt();
            ronde = 1;

            knop.setDisable(true);
            dc.startBeurt();
            toonDobbelstenen(dc.geefSpelStatus().dobbelstenen());
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
                dc.toonVolgendeSpeler();

                if (dc.geefSpelerAanDeBeurt().equals(actieveSpeler)) {
                    ronde = 2;
                    volgendeBeurt(false);
                    omrandMogelijkeVakjes("G");
                } else {
                    volgendeBeurt(false);
                    omrandMogelijkeVakjes("W");
                }

            }else{
                dc.voegMislukteWorpToe(actieveSpeler);
                if (dc.isEindeSpelDoorMislukteWorpen(actieveSpeler)) {
                    controleerEindeSpel(actieveSpeler);
                } else {
                    dc.toonVolgendeSpeler();
                    ronde = 1;
                    volgendeBeurt(true);
                }
            }

        }
    }


    public void toonDobbelstenen(List<Dobbelsteen> dobbelstenen) {
        grpDobbelstenen.getChildren().clear();

        for (int i = 0; i < dobbelstenen.size(); i++) {
            Dobbelsteen ds = dobbelstenen.get(i);
            Button dbsteen = new Button(String.valueOf(ds.getWaarde()));
            String kleurNaam = String.valueOf(ds.getKleur()).toLowerCase();
            String achtergrondKleur;
            String tekstKleur = "white";

            switch (kleurNaam) {
                case "rood":   achtergrondKleur = "#e74c3c"; break;
                case "geel":   achtergrondKleur = "#f1c40f"; tekstKleur = "black"; break;
                case "groen":  achtergrondKleur = "#27ae60"; break;
                case "blauw":  achtergrondKleur = "#2980b9"; break;
                case "wit":    achtergrondKleur = "#ffffff"; tekstKleur = "black"; break;
                default:       achtergrondKleur = "#ecf0f1"; tekstKleur = "black";
            }

            dbsteen.setStyle(
                    "-fx-background-color: " + achtergrondKleur + "; " +
                            "-fx-text-fill: " + tekstKleur + "; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 24px; " +
                            "-fx-background-radius: 12; " +
                            "-fx-min-width: 60px; " +
                            "-fx-min-height: 60px; " +
                            "-fx-border-color: rgba(0,0,0,0.2); " +
                            "-fx-border-radius: 12;"
            );

            grpDobbelstenen.add(dbsteen, i, 0);

            GridPane.setHalignment(dbsteen, HPos.CENTER);
            GridPane.setValignment(dbsteen, VPos.CENTER);
            GridPane.setMargin(dbsteen, new Insets(10));
        }
    }


    private void omrandMogelijkeVakjes(String type) {
        resetScorekaartRanden();

        List<Dobbelsteen> dobbelstenen = dc.geefSpelStatus().dobbelstenen();
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

        omrandMogelijkLockKnoppen();
    }

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


    @FXML
    private void handleScoreClick(ActionEvent event) {
        Button geklikteKnop = (Button) event.getSource();
        if ("LOCK".equals(geklikteKnop.getProperties().get("type"))) return;
        if (!heeftGerold || !geklikteKnop.getStyle().contains("-fx-border-width: 3px")) {
            return;
        }
        String kleur = (String) geklikteKnop.getProperties().get("kleur");
        int getal = (Integer) geklikteKnop.getProperties().get("getal");

        dc.kiesRij(dc.geefSpelerAanDeBeurt(), kleur, getal);

        geklikteKnop.setText("X");
        geklikteKnop.setDisable(true);

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
        omrandMogelijkLockKnoppen();
        refreshSpelerAanBeurt();

        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        if (dc.magRijSluitenViaLockKnop(dc.geefSpelerAanDeBeurt(), kleur)) {
            zetFeedback5(bundle.getString("lblFeedback5kruisjes"), true);
        } else {
            zetFeedback5("", true);

        }
        wachtOpVolgendeBeurt = true;
        btnRolDobbelstenen.setText(bundle.getString("VolgendeBeurt"));
        btnRolDobbelstenen.setDisable(false);

    }


    private void volgendeBeurt(boolean isEindeBeurt) {
        ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
        zetFeedback5("", true);
        resetScorekaartRanden();
        refreshSpelerAanBeurt();
        updateScorekaartVisueel();
        omrandMogelijkLockKnoppen();

        if (isEindeBeurt){
            heeftGerold = false;
            grpDobbelstenen.getChildren().clear();
            btnRolDobbelstenen.setText(bundle.getString("RolDobbelstenen"));
        }else{
            btnRolDobbelstenen.setText(bundle.getString("BeurtOverslaan"));
        }

    }

    private void controleerEindeSpel(String actieveSpeler) throws Exception {
        if (dc.isEindeSpelDoorMislukteWorpen(actieveSpeler) || dc.isEindeSpelDoorRijDoorkruising()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/GameOver.fxml"));
            ResourceBundle bundle = ResourceBundle.getBundle("localisatie", Locale.getDefault());
            loader.setResources(bundle);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) lblTitelSPEL.getScene().getWindow();
            stage.setScene(scene);
        }

    }

    private void updateScorekaartVisueel() {
        String huidigeSpeler = dc.geefSpelerAanDeBeurt();
        int[][] status = dc.geefStatus(huidigeSpeler);
        String[] kleuren = {"ROOD", "GEEL", "GROEN", "BLAUW"};

        for (Node node : grpScorekaart.getChildren()) {
            if (node instanceof Button btn) {
                Integer r = GridPane.getRowIndex(node);
                Integer k = GridPane.getColumnIndex(node);

                if (r != null && k != null && k <= 11) {
                    boolean rijGesloten = dc.geefIsRijGesloten(huidigeSpeler, kleuren[r]);

                    if (k == 11) {
                        if (rijGesloten) {
                            btn.setText("X");
                            btn.setDisable(true);
                            btn.setOpacity(1.0);
                        }
                    }else if(rijGesloten){
                        if (status[r][k] == 1) {
                            btn.setText("X");
                            btn.setOpacity(1.0);
                        }else {
                            btn.setText(String.valueOf(btn.getProperties().get("getal")));
                            btn.setOpacity(0.3);
                        }
                        btn.setDisable(true);
                    }else{
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
    }

    private void resetScorekaartRanden() {
        grpScorekaart.getChildren().forEach(node -> {
            if (node instanceof Button btn) {
                String huidigeStyle = btn.getStyle();
                String schoneStyle = huidigeStyle.replaceAll("-fx-border-width:.*", "");
                btn.setStyle(schoneStyle.trim());
            }
        });
    }


    private void updateMislukteWorpen(int aantalMislukt) {
        grpMislukteWorpen.getChildren().clear();
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

    private void handleLockClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String kleurNaam = (String) btn.getProperties().get("kleur");
        String speler = dc.geefSpelerAanDeBeurt();
        if (!dc.magRijSluitenViaLockKnop(speler, kleurNaam)) return;
        Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
        dc.sluitRij(k);
        btn.setText("X");
        btn.setDisable(true);
        btn.setOpacity(1.0);
        updateScorekaartVisueel();
        refreshSpelerAanBeurt();
        omrandMogelijkLockKnoppen();
        zetFeedback5("", true);
        if (dc.isEindeSpelDoorRijDoorkruising()) {
            try {
                controleerEindeSpel(actieveSpeler);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        if (wachtOpVolgendeBeurt) {
            wachtOpVolgendeBeurt = false;
            try { gaNaarVolgendeBeurt(); }
            catch (Exception ex) { throw new RuntimeException(ex); }
        }
    }

    private void omrandMogelijkLockKnoppen() {
        String speler = dc.geefSpelerAanDeBeurt();
        for (Node node : grpScorekaart.getChildren()) {
            if (!(node instanceof Button btn)) continue;
            if (!"LOCK".equals(btn.getProperties().get("type"))) continue;
            if (btn.isDisable()) continue;
            String kleurNaam = (String) btn.getProperties().get("kleur");
            if (dc.magRijSluitenViaLockKnop(speler, kleurNaam)) {
                String donker = switch (kleurNaam.toUpperCase()) {
                    case "ROOD"  -> "#c0392b";
                    case "GEEL"  -> "#f39c12";
                    case "GROEN" -> "#1e8449";
                    case "BLAUW" -> "#1a5276";
                    default      -> "#34495e";
                };
                btn.setStyle(btn.getStyle()
                        + String.format("-fx-border-width: 3px; -fx-border-color: %s;", donker));
            }
        }
    }

    private void zetFeedback5(String tekst, boolean success) {
        boolean leeg = (tekst == null || tekst.isBlank());
        lblFeedback5kruisjes.setText(tekst);
        lblFeedback5kruisjes.setVisible(!leeg);
        lblFeedback5kruisjes.setManaged(!leeg);
        lblFeedback5kruisjes.getStyleClass().removeAll("feedbackSuccess", "feedbackError");
        if (!leeg) {
            lblFeedback5kruisjes.getStyleClass().add(success ? "feedbackSuccess" : "feedbackError");
        }
    }


}


