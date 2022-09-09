import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import static java.lang.String.valueOf;


/**
 *
 * This Class handles displaying the MapPage and the Borough Stage
 *
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 26/03/2020
 */
public class MapViewer{

    AnchorPane mapRoot;
    BorderPane mainRoot;
    StackPane stackPane;
    Stage boroughsStage;
    Controller controller;
    VBox detailsVBox,boroughsList;
    RadioButton reverse;
    Label detailLabel, detailLabel2,numberOfProperties;
    MapCalculator mapCalc;

    /**
     *  The constructor of the MapViewer Class
     *  which gives an access to Controller and MapCalculator Classes
     *
     * @param mapCalc   Having access to MapCalculator Class
     * @param controller    Having access to Controller Class
     */
    public MapViewer(MapCalculator mapCalc,Controller controller)
    {
        this.mapCalc = mapCalc;
        this.controller = controller;
    }

    /**
     * This method returns the Main BorderPane with all the contents in it to the Controller Class
     *
     * @return  mainRoot  BorderPane
     * @throws IOException
     */
    public BorderPane show() throws IOException
    {
        //initialising variables
        VBox details = new VBox();
        mapRoot = new AnchorPane();
        mainRoot = new BorderPane();
        stackPane = new StackPane();

        //details Label located in the bottom right of BorderPane
        details.setAlignment(Pos.CENTER_LEFT);
        details.setPadding(new Insets(0,20,0,550));
        detailLabel = new Label("Name of the Borough");
        detailLabel2 = new Label("Number of properties in the Borough");
        detailLabel.setAlignment(Pos.BOTTOM_CENTER);////
        details.getChildren().addAll(detailLabel,detailLabel2);

        //setting the position of the Boroughs on the map
        mapCalc.setPos();
        boroughsSetMap();
        BorderPane.setAlignment(mainRoot,Pos.CENTER);

        //adding Labels and Map to the BorderPane
        mainRoot.setBottom(details);
        stackPane.getChildren().addAll(mapRoot);
        mainRoot.setLeft(stackPane);

        return mainRoot;
    }

    /**
     *  Setting the Map
     *  Using Circles and Labels
     */
    public void boroughsSetMap()
    {
        //initialising the minimum and maximum properties in all boroughs for calculation the borough's colour
        mapCalc.maxBorough = mapCalc.getBouroughsMinMax().get(mapCalc.getBouroughsMinMax().size()-1);
        mapCalc.minBorough = mapCalc.getBouroughsMinMax().get(0);

        //A for loop which creates the Circles and 2 Labels on them
        for (int i =0; i< mapCalc.boroughsList().size();i++)
        {
            //creating the circle
            Circle button = new Circle(30);
            button.setId(mapCalc.boroughsList().get(i));
            button.setLayoutX(mapCalc.xPositions.get(i));
            button.setLayoutY(mapCalc.yPositions.get(i));
            button.setFill(Color.rgb(mapCalc.setColor(mapCalc.boroughToProperty(mapCalc.boroughsList().get(i)).size()),100,50));  //setting the colour

            //setting action to each borough
            button.setOnMouseEntered(e-> mouseHover(button.getId()));           //on mouse hover
            button.setOnMouseClicked(e-> mouseClicked(button.getId()));         //on mose clicked

            //Creating the Label for the name of the borough
            Label label = new Label(mapCalc.boroughsList().get(i).substring(0,4).toUpperCase());  //shows the first 4 letter of the Borough's name in capital
            label.setId(mapCalc.boroughsList().get(i));
            label.setLayoutX(mapCalc.xPositions.get(i)-15);
            label.setLayoutY(mapCalc.yPositions.get(i)-12);
            label.setOnMouseClicked(e-> mouseClicked(label.getId()));       //setting action to the Label

            //Creating the Label for population of each Borough
            Label num = new Label(valueOf(mapCalc.boroughToProperty(mapCalc.boroughsList().get(i)).size()));
            num.setId(mapCalc.boroughsList().get(i));
            num.setLayoutX(mapCalc.xPositions.get(i)-12);
            num.setLayoutY(mapCalc.yPositions.get(i)+5);
            num.setOnMouseClicked(e-> mouseClicked(label.getId()));

            //adding the Circle and two Labels to the mapRoot
            mapRoot.getChildren().addAll(button,label,num);
        }
    }

    /**
     * Creates a new Stage when a Borough is clicked
     * While this Stage is open, other Stages will not be in use.
     * This Stage shows a list on=f the properties inside a chosen borough
     * The user can sort either by name, price or number of reviews. And
     * user can also reverse the search by selecting the REVERSE RadioButton
     *
     * The user can also select other boroughs from this stage, from the choice box located
     * on the top right of the Stage
     *
     * @param boroughs getting the Borough
     */
    public void boroughsStage(String boroughs)
    {
        //initialising variables
        BorderPane root = new BorderPane();
        BorderPane topRoot = new BorderPane();
        Scene scene = new Scene(root);
        ScrollPane scrollPane = new ScrollPane();
        ChoiceBox<String> sortChoice = new ChoiceBox<>();
        ChoiceBox<String> boroughChoice = new ChoiceBox<>();
        HBox sortBox = new HBox();
        HBox boroughsBox = new HBox();
        detailsVBox = new VBox();
        boroughsList = new VBox();
        boroughsStage = new Stage();

        //setting the title of the Stage to the name of the Borough
        boroughsStage.setTitle(boroughs);

        //setting the size of the Stage
        boroughsStage.setWidth(800);
        boroughsStage.setHeight(550);

        //details VBox
        detailsVBox.setSpacing(10);
        detailsVBox.setAlignment(Pos.CENTER_LEFT);
        detailsVBox.setPadding(new Insets(10,0,10,20));

        //A scrollPane for showing the property list
        scrollPane.setContent(boroughsList);
        scrollPane.setPrefSize(380,600);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);

        //Label
        numberOfProperties = new Label("Number of properties: "+ mapCalc.boroughToProperty(boroughs).size());
        numberOfProperties.setPadding(new Insets(3,5,3,5));

        //adding sort options to the ChoiceBox
        sortChoice.getItems().addAll("Name","Price","Reviews");
        sortChoice.setValue("Name");

        //adding the name of the Boroughs alphabetically to the ChoiceBox
        for(int i =0; i< mapCalc.boroughsList().size();i++)
        {
            ArrayList<String> array = new ArrayList<>();
            array = mapCalc.boroughsList();
            Collections.sort(array);
            boroughChoice.getItems().addAll(array.get(i));
        }
        boroughChoice.setValue(boroughs);

        //RadioButton which you can reverse your sort with it
        reverse = new RadioButton("_Reverse");
        reverse.setPadding(new Insets(3,0,3,0));

        //Sort Button
        Button sortButton = new Button("_Sort");
        sortButton.setOnMouseClicked(e-> sort(sortChoice.getValue(),boroughs));

        //Go Button
        //closes the current stage and opens the Stage of the selected Borough
        Button boroughButton = new Button("_GO");
        boroughButton.setOnMouseClicked(e-> setBoroughs(boroughChoice.getValue()));
        boroughButton.setOnKeyPressed(e-> {
            if(e.getCode()== KeyCode.ENTER){
                setBoroughs(boroughChoice.getValue());
            }
        });

        sort(sortChoice.getValue(),boroughs);  //sorting the property list, sorts by Name as default


        //adding some spacing, padding
        boroughsList.setSpacing(5);
        boroughsList.setPadding(new Insets(5,10,5,10));
        sortBox.setSpacing(10);
        sortBox.setPadding(new Insets(5,5,5,5));
        boroughsBox.setSpacing(10);
        boroughsBox.setPadding(new Insets(5,5,5,5));

        //adding Buttons, ChoiceBoxes, Labels and ScrollPane to the root
        sortBox.getChildren().addAll(sortChoice,reverse,sortButton,numberOfProperties);
        boroughsBox.getChildren().addAll(boroughChoice,boroughButton);
        root.setLeft(scrollPane);
        root.setTop(topRoot);
        root.setCenter(detailsVBox);
        topRoot.setLeft(sortBox);
        topRoot.setRight(boroughsBox);


        boroughsStage.setResizable(false);      //making the Stage not resizable
        boroughsStage.getIcons().add(new Image("icon-custom.png"));        //adding an icon to the Stage
        boroughsStage.setScene(scene);      //setting the Scene
        boroughsStage.initModality(Modality.APPLICATION_MODAL);     //other Stages will no be in used while this Stage is open
        boroughsStage.show();       //showing the Stage
    }

    /**
     *  Adding the boroughs to the VBox inside the ScrollPane
     *
     * @param boroughs  getting the Borough
     * @param sorted    getting the ArrayList sorted
     */
    public void setBoroughsList(String boroughs,ArrayList<String> sorted)
    {
        //removing all the current properties inside the Stage
        boroughsList.getChildren().removeAll(boroughsList.getChildren());

        //adding add the properties as a Label followed by a Separator
        //and adding action to it
        for(int i = 0; i< mapCalc.boroughToProperty(boroughs).size();i++)
        {
            Label label = new Label(sorted.get(i));
            Separator separator = new Separator(Orientation.HORIZONTAL);
            int finalI = i;
            label.setOnMouseClicked(e-> setDetailsVBox(sorted.get(finalI)));
            boroughsList.getChildren().addAll(label,separator);
        }
    }

    /**
     * This method gets the Property by its parameter and
     * gets all the necessary data from the MapCalculator Class and
     * displays all the information about the property
     *
     * @param property  getting the Property
     */
    public void setDetailsVBox(String property)
    {
        //removing the current information in the Stage
        detailsVBox.getChildren().removeAll(detailsVBox.getChildren());

        //getting data from mapCalculator Class
        AirbnbListing prop = mapCalc.getPropDetails(property);

        //Creating the Labels
        Label id = new Label("ID: " + prop.getId());
        Label name = new Label("Name: " +prop.getName());
        Label hostId = new Label("Host ID: "+ prop.getHost_id());
        Label hostName = new Label("Host Name: "+ prop.getHost_name());
        Label neigh = new Label("Neighbourhood: "+prop.getNeighbourhood() );
        Label latitude = new Label("Latitude: "+ prop.getLatitude());
        Label longitude = new Label("Longitude: "+prop.getLatitude());
        Label roomType = new Label("Room Type: "+ prop.getRoom_type());
        Label price = new Label("Price: "+ prop.getPrice());
        Label minNights = new Label("Minimum Nights: "+ prop.getMinimumNights());
        Label reviews = new Label("Number of Reviews: "+ prop.getNumberOfReviews());
        Label lastRev = new Label("Date of Last Review: "+ prop.getLastReview());
        Label revPerMonth = new Label("Average Review per Month: "+ prop.getReviewsPerMonth());
        Label hostNumberOfProp = new Label("Number of "+prop.getHost_name()+ "'s Other Properties: "+ prop.getCalculatedHostListingsCount());
        Label availability365 = new Label("Available days in Year: "+ prop.getAvailability365());

        //adding a Button to display the current property's location on map
        Button map = new Button("Open Map!");
        map.setOnMouseClicked(e-> {
            try {
                googleMapStage(property);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //adding all the Labels and Button to the VBox
        detailsVBox.getChildren().addAll(id,name,hostId,hostName,neigh,latitude,longitude,roomType,price,minNights,reviews,lastRev,revPerMonth,hostNumberOfProp,availability365,map);
    }

    /**
     * This Method gets the sort type and the Borough name by its parameter, and
     * decides which method to call
     *
     * @param sort  getting the sort type
     * @param borough   getting the name of the Borough
     */
    public void sort(String sort,String borough) {
        if(sort.equals("Name"))
        {
            setBoroughsList(borough,mapCalc.nameSort(borough,reverse.isSelected()));
        }
        if(sort.equals("Price"))
        {
            setBoroughsList(borough,mapCalc.priceSort(borough,reverse.isSelected())); /////to return
        }
        if(sort.equals("Reviews"))
        {
            setBoroughsList(borough,mapCalc.revSort(borough,reverse.isSelected()));
        }
    }

    /**
     * This method closes the current Stage and opens a new one when
     * the user selects a new Borough from the Borough Stage
     *
     * @param boroughs  getting the Borough
     */
    public void setBoroughs(String boroughs) {
        boroughsStage.close();
        boroughsStage(boroughs);
    }

    /**
     * This method is called when the user clicks a Borough
     * it opens the Borough Stage for the fist time
     *
     * @param borough   getting the Borough
     */
    public void mouseClicked(String borough)
    {
        mapCalc.returnBoroughs(borough);
        mapCalc.boroughListing(borough);
        boroughsStage(borough);
    }

    /**
     * This method is called when the user hovers over a Borough
     * it displays the Borough the user is showing
     *
     * @param borough   getting the Borough
     */
    public void mouseHover(String borough)
    {
        detailLabel.setText("Name of the Borough: "+ borough);
        detailLabel2.setText("Number of properties in the Borough: "+ mapCalc.boroughToProperty(borough).size());
    }

    /**
     * This method opens the default web browser of the user and shows the location of the property selected
     *
     * @param property  getting the Property
     * @throws Exception
     */
    public void googleMapStage(String property) throws Exception
    {
        AirbnbListing prop = mapCalc.getPropDetails(property);
        URI uri = new URI("https://google.com/maps/place/"+prop.getLatitude()+","+prop.getLongitude());
        java.awt.Desktop.getDesktop().browse(uri);
    }
}
