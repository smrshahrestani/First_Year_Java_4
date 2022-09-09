import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 26/03/2020
 *
 *  This Class handles controlling all of the main Stage
 *  and passing necessary data to other Classes
 */
public class Controller extends Application {
    //Associating Classes
    ArrayList<AirbnbListing> airbnbList;
    MainPage mainPage;
    AirbnbDataLoader load;
    MapCalculator mapCalc;
    MapViewer mapViewer;
    UserLoginCalculator userLoginCalc;
    StatisticsCalculator statCalc;
    StatisticsViewer statViewer;
    UserLoginViewer userLoginViewer;
    ArrayList<BorderPane> pages;
    Button pageLeft,pageRight,go;
    Label propertyPriceLabel , welcomePageLabel,mapPageLabel, statPageLabel, userLoginLabel,userName;
    Spinner minPriceSpinner, maxPriceSpinner;
    Scene scene;
    BorderPane pageCenter, currentPage;
    boolean goButton= false;
    int currentPageNO = 0,minPrice = 0, maxPrice = 7000;


    /**
     * The constructor for the Controller Class
     */
    public Controller()
    {
        mainPage = new MainPage();
        load = new AirbnbDataLoader(this);
        load.load();
        mapCalc = new MapCalculator(airbnbList,load);
        mapViewer = new MapViewer(mapCalc,this);
        userLoginCalc= new UserLoginCalculator(this);
        userLoginViewer = new UserLoginViewer(this,userLoginCalc);
        pages = new ArrayList<>();
        statCalc = new StatisticsCalculator(load, userLoginViewer.userLoginCalc);
        statViewer = new StatisticsViewer(statCalc);

        currentPage = mainPage.show();      //setting the current page to MainPage
        airbnbList = load.getListings();
    }

    /**
     * @param stage Stage
     * @throws Exception
     *
     * The Main method for starting the Application and opening the main Stage
     *
     */
    @Override
    public void start(Stage stage) throws Exception
    {

        //adding the all the pages to pages list
        pages.add(mainPage.show());
        pages.add(mapViewer.show());
        pages.add(statViewer.show());
        pages.add(userLoginViewer.beforeLogin());

        stage.setTitle(" BOYOUT");   //Name of the project, title of the stage

        //setting the size of the stage
        stage.setWidth(900);
        stage.setHeight(615);

        //initialising the Borders, Boxes, Spinners
        BorderPane root = new BorderPane();
        scene = new Scene(root);  //creating a new Scene
        root.setPadding(new Insets(10, 10, 10, 10));
        BorderPane topBorder = new BorderPane();
        BorderPane bottomBorder = new BorderPane();
        HBox priceControl = new HBox();
        HBox currentPageStatus = new HBox();
        pageCenter = new BorderPane();
        minPriceSpinner = new Spinner<>();
        maxPriceSpinner = new Spinner<>();
        HBox left = new HBox();

        //Minimum price spinner
        SpinnerValueFactory<Integer> minValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(mapCalc.returnMinPrice() - 1, mapCalc.returnMaxPrice(), 20);
        minPriceSpinner.setValueFactory(minValueFactory);
        int intValue = Integer.parseInt(minPriceSpinner.getValue().toString());
        minPriceSpinner.setPrefWidth(100);
        minPriceSpinner.setMinWidth(80);
        minPriceSpinner.setMaxWidth(150);
        minPriceSpinner.setEditable(true);

        //Maximum price spinner
        SpinnerValueFactory<Integer> maxValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(mapCalc.returnMinPrice(),mapCalc.returnMaxPrice(),50);
        maxPriceSpinner.setValueFactory(maxValueFactory);
        maxPriceSpinner.setPrefWidth(100);
        maxPriceSpinner.setMinWidth(80);
        maxPriceSpinner.setMaxWidth(150);
        maxPriceSpinner.setEditable(true);

        //Username label which is invisible by default
        //It will be visible when the user loges in
        userName = new Label("Admin");
        HBox.setMargin(userName,new Insets(5,0,0,350));
        userName.setVisible(false);

        //From Label
        Label fromLabel = new Label("From:");
        fromLabel.setFont(new Font(15));
        fromLabel.setPadding(new Insets(2,0,2,0));
        fromLabel.setMinWidth(40);

        //To Label
        Label toLabel = new Label("To:");
        toLabel.setFont(new Font(15));
        toLabel.setPadding(new Insets(2,0,2,0));
        toLabel.setMinWidth(20);

        //WelcomPage Label
        welcomePageLabel = new Label("Welcome");
        welcomePageLabel.setFont(new Font(15));
        welcomePageLabel.setStyle("-fx-font-weight: bold");
        welcomePageLabel.setPadding(new Insets(0,0,3,0));
        welcomePageLabel.setMinWidth(20);

        //WelcomePage Label
        mapPageLabel = new Label("Maps");
        mapPageLabel.setFont(new Font(12));
        mapPageLabel.setPadding(new Insets(2,0,2,0));
        mapPageLabel.setMinWidth(20);

        //Statistics Page label
        statPageLabel = new Label("Statistics");
        statPageLabel.setFont(new Font(12));
        statPageLabel.setPadding(new Insets(2,0,2,0));
        statPageLabel.setMinWidth(20);

        //UserLogin Label
        userLoginLabel = new Label("User Login");
        userLoginLabel.setFont(new Font(12));
        userLoginLabel.setPadding(new Insets(2,0,2,0));
        userLoginLabel.setMinWidth(20);

        //Available properties Label, which is invisible by default
        //It will be visible when the user clicks the GO button
        propertyPriceLabel = new Label("Available properties: ");
        propertyPriceLabel.setFont(new Font(12));
        propertyPriceLabel.setPadding(new Insets(5,10,2,0));
        propertyPriceLabel.setMinWidth(20);
        propertyPriceLabel.setVisible(false);

        //Left Page Button
        pageLeft = new Button("_<");
        pageLeft.setFont(new Font(15));

        //Right Page Button
        pageRight = new Button("_>");
        pageRight.setFont(new Font(15));

        //Go Button
        go = new Button("GO");
        pageLeft.setFont(new Font(15));
        go.setOnAction(e->{
            goButton = true;
            setPriceRange();
        });

        //Initialising Separators
        Separator hSeparator = new Separator(Orientation.HORIZONTAL);
        hSeparator.setPadding(new Insets(0,0,10,0));
        Separator hSeparator2 = new Separator(Orientation.HORIZONTAL);
        hSeparator2.setPadding(new Insets(10,0,0,0));
        Separator vSeparator1 = new Separator(Orientation.VERTICAL);
        Separator vSeparator2 = new Separator(Orientation.VERTICAL);
        Separator vSeparator3 = new Separator(Orientation.VERTICAL);

        //adding Labels and Buttons to the top of the stage
        priceControl.getChildren().addAll(propertyPriceLabel,fromLabel,minPriceSpinner,toLabel,maxPriceSpinner,go);
        priceControl.setSpacing(10);

        //adding page names Label to the top left of the stage
        currentPageStatus.getChildren().addAll(welcomePageLabel,vSeparator1,mapPageLabel,vSeparator2,statPageLabel,vSeparator3,userLoginLabel);
        currentPageStatus.setSpacing(10);


        root.setTop(topBorder);          //adding the top border to the top of the stage
        root.setBottom(bottomBorder);    //adding the bottom border to the bottom of the stage
        root.setCenter(pageCenter);      //adding the center border to the center of the stage

        pageCenter.setCenter(currentPage);    //Current Stage //this will be changes when we change between the screens

        topBorder.setRight(priceControl);       //adding the price control panel to the top right of the stage
        topBorder.setLeft(currentPageStatus);   //adding the Current Page panel to the top right of the stage
        bottomBorder.setRight(pageRight);       //adding the right Button to the bottom right of the stage
        left.getChildren().addAll(pageLeft,userName);       //adding the username and left Button to the HBox
        bottomBorder.setLeft(left);             //adding the left panel to the bottom left of the Stage
        bottomBorder.setTop(hSeparator);        //adding a separator to top of the screen
        topBorder.setBottom(hSeparator2);       //adding a separator to bottom of the screen

        //getting values from the spinner
        maxPrice = Integer.parseInt(maxPriceSpinner.getValue().toString());
        minPrice = Integer.parseInt(minPriceSpinner.getValue().toString());

        stage.setResizable(false);      //making the Stage not resizable
        stage.getIcons().add(new Image("icon-custom.png"));        //adding an icon to the Stage
        stage.setScene(scene);      //setting the Scene
        stage.show();               //Showing the scene
    }

    /**
     *  this method is called when the Go button is pressed
     *  this method handles the price sorting
     *  it shows an error if the price range is not acceptable
     */
    public void setPriceRange()
    {
        //if the Min price is greater than Max price
        //displays error
        if (Integer.parseInt(maxPriceSpinner.getValue().toString())<Integer.parseInt(minPriceSpinner.getValue().toString()))
        {
            errorHandler("Price Range Unacceptable","Minimum price is greater than maximum price!", Alert.AlertType.ERROR);
        }
        else
        {
            setButtonsAction();           //setting other buttons on action
            propertyPriceLabel.setVisible(true);            //making the price Label visible.
            maxPrice = Integer.parseInt(maxPriceSpinner.getValue().toString());    //setting the max price
            minPrice = Integer.parseInt(minPriceSpinner.getValue().toString());    //setting the min price
//            load.priceListing.clear();      //clearing the priceList
            load.load();                    //loading data from AirbnbDataLoader
            propertyPriceLabel.setText("Available properties: "+load.priceSorted.size());       //showing available properties
//            load.priceSorted.clear();
        }
        mapViewer.boroughsSetMap();     //setting the map
    }

    /**
     * setting the action on the Buttons and Labels
     */
    public void setButtonsAction()
    {
        //go to Welcome Page
        welcomePageLabel.setOnMouseClicked(e->{
            currentPageNO = 0;
            currentPage = pages.get(currentPageNO);
            pageCenter.setCenter(currentPage);
            currentPage();
        });

        //go to Map Page
        mapPageLabel.setOnMouseClicked(e->{
            currentPageNO = 1;
            currentPage = pages.get(currentPageNO);
            pageCenter.setCenter(currentPage);
            currentPage();
        });

        //go to Statistics Page
        statPageLabel.setOnMouseClicked(e->{
            currentPageNO = 2;
            currentPage = pages.get(currentPageNO);
            pageCenter.setCenter(currentPage);
            currentPage();
        });

        //go to UserLogin Page
        userLoginLabel.setOnMouseClicked(e->{
            currentPageNO = 3;
            currentPage = updateScreen();
            pageCenter.setCenter(currentPage);
            currentPage();
        });

        //go to Left Page
        pageLeft.setOnAction(e-> {
            try {
                previousPage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //go to Right Page
        pageRight.setOnAction(e-> {
            try {
                nextPage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * going to next page by calling this method
     *
     * @throws IOException
     */
    public void nextPage() throws IOException {

        currentPageNO++;          //incrementing the number of current page

        // if the page number reaches the end of the list, the page number will be 0;
        if (currentPageNO % pages.size() == 0)
        {
            currentPageNO = 0;
        }

        //if the page number was 3, which is the Login Page, it will update the screen
        if(currentPageNO==3)
        {
            currentPage = updateScreen();
        }

        currentPage = pages.get(currentPageNO);             //setting the Current Page
        pageCenter.setCenter(currentPage);                  //replacing the current Center Screen
        currentPage();                                      //setting the navigation label fonts
    }

    /**
     * going to previous page by calling this method
     *
     * @throws IOException
     */
    public void previousPage() throws IOException
    {
        currentPageNO--;

        if (currentPageNO == -1)
        {
            currentPageNO = pages.size()-1;
        }
        currentPage = pages.get(currentPageNO);

        if(currentPageNO==3)
        {
            currentPage = updateScreen();
        }
        pageCenter.setCenter(currentPage);
        currentPage();
    }

    /**
     * updating the login screen
     * it checks the isLoggedIn boolean in the userLogin Class
     * and updates the screen
     *
     * @return  currentPage
     */
    public BorderPane updateScreen()
    {
        //if the user is loggedIn
        if(userLoginViewer.isLoggedIn)
        {
            currentPage = userLoginViewer.afterLogin();
        }

        //if the user is not loggedIn
        else
        {
            currentPage = userLoginViewer.beforeLogin();
        }

        pageCenter.setCenter(currentPage);
        return currentPage;
    }

    /**
     *  setting all the menu labels to normal font
     */
    public void normalFont()
    {
        welcomePageLabel.setFont(new Font(12));
        welcomePageLabel.setStyle("-fx-font-weight: NORMAL");

        mapPageLabel.setFont(new Font(12));
        mapPageLabel.setStyle("-fx-font-weight: NORMAL");

        statPageLabel.setFont(new Font(12));
        statPageLabel.setStyle("-fx-font-weight: NORMAL");

        userLoginLabel.setFont(new Font(12));
        userLoginLabel.setStyle("-fx-font-weight: NORMAL");
    }

    /**
     *  it bolds the current page menu Label
     */
    public void currentPage()
    {
        normalFont();
        if(currentPageNO==0)
        {
            welcomePageLabel.setFont(new Font(15));
            welcomePageLabel.setStyle("-fx-font-weight: bold");
            welcomePageLabel.setPadding(new Insets(0,0,3,0));
        }
        if(currentPageNO==1)
        {
            mapPageLabel.setFont(new Font(15));
            mapPageLabel.setStyle("-fx-font-weight: bold");
            mapPageLabel.setPadding(new Insets(0,0,3,0));
        }
        if(currentPageNO==2)
        {
            statPageLabel.setFont(new Font(15));
            statPageLabel.setStyle("-fx-font-weight: bold");
            statPageLabel.setPadding(new Insets(0,0,3,0));
        }
        if(currentPageNO==3)
        {
            userLoginLabel.setFont(new Font(15));
            userLoginLabel.setStyle("-fx-font-weight: bold");
            userLoginLabel.setPadding(new Insets(0,0,3,0));
        }

    }

    /**
     *
     * This method is used to display errors, alerts and confirmations
     *
     * @param errorHeader A Header for the Error
     * @param errorName A Text for the Error
     * @param type  The type of the error
     */
    public void errorHandler(String errorHeader, String errorName, Alert.AlertType type)
    {
        Alert alert = new Alert(type);
        alert.setHeaderText(errorHeader);
        alert.setContentText(errorName);
        alert.showAndWait();
    }
}