import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.io.IOException;

/**
 * This Class handles displaying the Statistics
 *
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 26/03/2020
 */
public class StatisticsViewer
{
    StatisticsCalculator statCalc;
    ChoiceBox statChoice;
    Label topLeftTitle,topLeftText,topRightTitle,topRightText,bottomLeftTitle,bottomLeftText,bottomRightTitle,bottomRightText;

    /**
     * The constructor for the StatisticsViewer Class
     * which gives an access to StatisticViewer Class
     *
     * @param statCalc  StatisticsCalculator
     */
    public StatisticsViewer(StatisticsCalculator statCalc)
    {
        this.statCalc = statCalc;
    }

    /**
     * This method return the Statistics BorderPane with all the content in it to the Controller Class
     *
     * @return root BorderPane
     */
    public BorderPane show()
    {
        statCalc.setQueue();

        //initialising the variables
        BorderPane root = new BorderPane();
        GridPane gridRoot = new GridPane();
        BorderPane topLeft = new BorderPane();
        BorderPane topRight = new BorderPane();
        BorderPane bottomLeft = new BorderPane();
        BorderPane bottomRight = new BorderPane();
        VBox topLeftVBox = new VBox();
        VBox topRightVBox = new VBox();
        VBox bottomLeftVBox = new VBox();
        VBox bottomRightVBox = new VBox();

        //TOP_LEFT_BOX
        //Labels
        topLeftTitle = new Label(statCalc.allStatTitles.get(0));
        topLeftText = new Label(statCalc.allStatTexts.get(0));
        statCalc.inUse.add(statCalc.allStatTitles.get(0));

        //Buttons
        Button topLeftL = new Button("<");
        topLeftL.setOnMouseClicked(e->previousStat(topLeftTitle,topLeftText));
        BorderPane.setAlignment(topLeftL, Pos.CENTER);
        Button topLeftR = new Button(">");
        topLeftR.setOnMouseClicked(e->nextStat(topLeftTitle,topLeftText));
        BorderPane.setAlignment(topLeftR, Pos.CENTER);

        //adding it to its root
        topLeftVBox.setAlignment(Pos.CENTER);
        topLeftVBox.getChildren().addAll(topLeftTitle,topLeftText);
        topLeft.setCenter(topLeftVBox);
        topLeft.setLeft(topLeftL);
        topLeft.setRight(topLeftR);

        //TOP_RIGHT_BOX
        topRightTitle = new Label(statCalc.allStatTitles.get(1));
        topRightText = new Label(statCalc.allStatTexts.get(1));
        statCalc.inUse.add(statCalc.allStatTitles.get(1));

        //Buttons
        Button topRightL = new Button("<");
        topRightL.setOnMouseClicked(e->previousStat(topRightTitle,topRightText));
        BorderPane.setAlignment(topRightL, Pos.CENTER);
        Button topRightR = new Button(">");
        topRightR.setOnMouseClicked(e->nextStat(topRightTitle,topRightText));
        BorderPane.setAlignment(topRightR, Pos.CENTER);

        //adding it to its root
        topRightVBox.setAlignment(Pos.CENTER);
        topRightVBox.getChildren().addAll(topRightTitle,topRightText);
        topRight.setCenter(topRightVBox);
        topRight.setLeft(topRightL);
        topRight.setRight(topRightR);

        //BOTTOM_LEFT_BOX
        bottomLeftTitle = new Label(statCalc.allStatTitles.get(2));
        bottomLeftText = new Label(statCalc.allStatTexts.get(2));
        statCalc.inUse.add(statCalc.allStatTitles.get(2));

        //Buttons
        Button bottomLeftL = new Button("<");
        bottomLeftL.setOnMouseClicked(e->previousStat(bottomLeftTitle,bottomLeftText));
        BorderPane.setAlignment(bottomLeftL, Pos.CENTER);
        Button bottomLeftR = new Button(">");
        bottomLeftR.setOnMouseClicked(e->nextStat(bottomLeftTitle,bottomLeftText));
        BorderPane.setAlignment(bottomLeftR, Pos.CENTER);

        //adding it to its root
        bottomLeftVBox.setAlignment(Pos.CENTER);
        bottomLeftVBox.getChildren().addAll(bottomLeftTitle,bottomLeftText);
        bottomLeft.setCenter(bottomLeftVBox);
        bottomLeft.setLeft(bottomLeftL);
        bottomLeft.setRight(bottomLeftR);

        //BOTTOM_RIGHT_BOX
        bottomRightTitle = new Label(statCalc.allStatTitles.get(3));
        bottomRightText = new Label(statCalc.allStatTexts.get(3));
        statCalc.inUse.add(statCalc.allStatTitles.get(3));

        //Buttons
        Button bottomRightL = new Button("<");
        bottomRightL.setOnMouseClicked(e->previousStat(bottomRightTitle ,bottomRightText));
        BorderPane.setAlignment(bottomRightL, Pos.CENTER);
        Button bottomRightR = new Button(">");
        bottomRightR.setOnMouseClicked(e->nextStat(bottomRightTitle,bottomRightText));
        BorderPane.setAlignment(bottomRightR, Pos.CENTER);

        //adding it to its root
        bottomRightVBox.setAlignment(Pos.CENTER);
        bottomRightVBox.getChildren().addAll(bottomRightTitle,bottomRightText);
        bottomRight.setCenter(bottomRightVBox);
        bottomRight.setLeft(bottomRightL);
        bottomRight.setRight(bottomRightR);

        //root for top of the BorderPane
        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5,10,5,10));
        topHBox.setSpacing(10);

        //Label
        Label chooseStat = new Label("Choose the Statistic: ");
        chooseStat.setPadding(new Insets(3));
        Label chooseBox = new Label("Choose the Box: ");
        chooseBox.setPadding(new Insets(3));

        //ChoiceBox for selecting the Statistic
        statChoice = new ChoiceBox();
        statChoice.setPrefWidth(250);
        updateChoiceBox(statChoice);

        //ChoiceBox for selecting the Box
        ChoiceBox boxChoice = new ChoiceBox();
        boxChoice.getItems().addAll("TOP_LEFT","TOP_RIGHT","BOTTOM_LEFT","BOTTOM_RIGHT");

        //GO Button
        Button go = new Button("GO");
        go.setOnMouseClicked(e->{
            if (statChoice.getValue() == null  || boxChoice.getValue() == null) {
                statCalc.userCalc.controller.errorHandler("INPUT REQUIRED","Please Select both fields, Thanks ;)", Alert.AlertType.ERROR);
            }
            else {
                forceStatSet(statChoice.getValue().toString(), boxChoice.getValue().toString());

            }
        });

        //adding Labels,ChoiceBoxes and Button to the top root
        topHBox.setPadding(new Insets(8));
        topHBox.getChildren().addAll(chooseStat,statChoice,chooseBox,boxChoice,go);

        //setting position of each Box
        GridPane.setConstraints(topLeft,0,0);
        GridPane.setConstraints(topRight,1,0);
        GridPane.setConstraints(bottomLeft,0,1);
        GridPane.setConstraints(bottomRight,1,1);

        //TOP_LEFT_BOX adding style, size
        topLeft.setMinSize(420,200);
        topLeft.setPadding(new Insets(10));
        topLeft.setStyle("-fx-background-color: #33cca6;");
        topLeft.setPadding(new Insets(10));

        //TOP_RIGHT_BOX adding style, size
        topRight.setMinSize(420,200);
        topRight.setPadding(new Insets(10));
        topRight.setStyle("-fx-background-color: #33cca6;");
        topRight.setPadding(new Insets(10));

        //BOTTOM_LEFT_BOX adding style, size
        bottomLeft.setMinSize(420,200);
        bottomLeft.setPadding(new Insets(10));
        bottomLeft.setStyle("-fx-background-color: #33cca6;");
        bottomLeft.setPadding(new Insets(10));

        //BOTTOM_RIGHT_BOX adding style, size
        bottomRight.setMinSize(420,200);
        bottomRight.setPadding(new Insets(10));
        bottomRight.setStyle("-fx-background-color: #33cca6;");
        bottomRight.setPadding(new Insets(10));

        //adding the Boxes to the GridPane
        gridRoot.getChildren().addAll(topLeft,topRight,bottomLeft,bottomRight);
        gridRoot.setVgap(10);
        gridRoot.setHgap(10);
        gridRoot.setPadding(new Insets(10,10,10,10));

        //adding the GridPane and top HBox to the main root
        root.setTop(topHBox);
        root.setLeft(gridRoot);

        return root;
    }


    /**
     * shows the next Statistic
     * then updates the ChoiceBox
     *
     * @param title Gets the titleLabel
     * @param text  Gets the textLabel
     */
    public void nextStat(Label title ,Label text )
    {
        statCalc.nextStat(title,text);
        updateChoiceBox(statChoice);
    }

    /**
     * shows the previous Statistics
     * then updates the ChoiceBox
     *
     * @param title Gets the titleLabel
     * @param text  Gets the textLabel
     */
    public void previousStat(Label title, Label text)
    {
        statCalc.prevStat(title,text);
        updateChoiceBox(statChoice);
    }

    /**
     * Shows the asked Property to the asked Box
     *
     * @param statTitle Gets the Statistic selected by the user
     * @param box   Gets the Box selected by the user
     */
    public void forceStatSet(String statTitle ,String box)
    {
        Label title = null;
        Label text = null;
        if(box.equals("TOP_LEFT"))
        {
            title = topLeftTitle;
            text = topLeftText;
        }
        if(box.equals("TOP_RIGHT"))
        {
            title = topRightTitle;
            text = topRightText;
        }
        if (box.equals("BOTTOM_LEFT"))
        {
            title = bottomLeftTitle;
            text = bottomLeftText;
        }
        if(box.equals("BOTTOM_RIGHT"))
        {
            title = bottomRightTitle;
            text = bottomRightText;
        }
        statCalc.forceStatSet(statTitle,title,text);
        updateChoiceBox(statChoice);
    }

    /**
     * updates the Statistic ChoiceBox
     *
     * @param choiceBox gets the ChoiceBox
     */
    public void updateChoiceBox(ChoiceBox choiceBox)
    {
        statCalc.updateChoiceBox(choiceBox);
    }

}
