import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This Class handles displaying the UserLoginViewer, CreateAccount, ForgotPassword Stages
 *
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * version: 27/03/2020
 */
public class UserLoginViewer
{
    Controller controller;
    TextField user,searchField;
    PasswordField userPassword;
    UserLoginCalculator userLoginCalc;
    VBox detailsVBox,searchList;
    Label availableProps;
    Stage forgotPass;
    public Boolean isLoggedIn = false, accountCreated= false, passwordReset = false;

    /**
     * The constructor of the UserLoginViewer CLass
     * which gives an access to Controller and UserLoginCalculator Classes
     *
     * @param controller    Controller
     * @param userLoginCalc UserLoginCalculator
     */
    public UserLoginViewer(Controller controller,UserLoginCalculator userLoginCalc)
    {
        this.userLoginCalc = userLoginCalc;
        this.controller = controller;
    }

    /**
     * This method returns a BorderPane with all the contents in it to the Controller Class
     * This method shows the before login screen
     * which is a screen with a username and password field and
     * create an account and forgot password buttons.
     *
     * @return userLoginRoot BorderPane
     */
    public BorderPane beforeLogin()
    {
        //initialising variables
        BorderPane userLoginRoot = new BorderPane();
        HBox userBox = new HBox();
        HBox passBox = new HBox();
        HBox loginBox = new HBox();
        HBox cButtonF = new HBox();
        HBox orBox = new HBox();
        VBox vRoot = new VBox();

        //set position, spacing and padding
        userBox.setPadding(new Insets(0,0,0,100));
        userBox.setSpacing(30);
        passBox.setPadding(new Insets(0,0,0,100));
        passBox.setSpacing(30);
        loginBox.setPadding(new Insets(0,0,0,290));
        cButtonF.setSpacing(20);
        cButtonF.setPadding(new Insets(0,0,0,130));
        orBox.setPadding(new Insets(0,0,0,245));
        vRoot.setAlignment(Pos.CENTER);
        vRoot.setSpacing(10);

        //adding the Labels and Buttons to the vRoot
        vRoot.getChildren().addAll(userBox,passBox,loginBox,orBox,cButtonF);

        //Password field
        userPassword = new PasswordField();
        userPassword.setOnKeyPressed(e-> {
            if(e.getCode()== KeyCode.ENTER){
                login();
            }
        });

        //username Filed
        user = new TextField();
        Label userNameLabel = new Label("Username: ");
        userNameLabel.setPadding(new Insets(0,0,0,0));

        //Password Label
        Label passwordLabel = new Label("Password: ");
        passwordLabel.setPadding(new Insets(0,3,0,0));
        Label or = new Label("or");

        //Login Button
        Button login = new Button("Login");
        login.setOnMouseClicked(e-> login());

        //Create Account Button
        Button createAccount = new Button("Create an account");
        createAccount.setOnMouseClicked(e-> {
            try {
                createAccount();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        //Forgot password Button
        Button forgotPass  = new Button("Forgot password");
        forgotPass.setOnMouseClicked(e->resetPass());

        //adding all the content to its roots
        userBox.getChildren().addAll(userNameLabel,user);
        passBox.getChildren().addAll(passwordLabel,userPassword);
        loginBox.getChildren().addAll(login);
        orBox.getChildren().addAll(or);
        cButtonF.getChildren().addAll(createAccount,forgotPass);
        userLoginRoot.setCenter(vRoot);

        return userLoginRoot;
    }

    /**
     * This method will return a BorderPane with all its content to the Controller Class
     * This BorderPane will be showed when the user loges in
     * Which is a screen with a search menu at the top and a ScrollPane at the left
     * which is similar to the one we had in the Map Borough Stage
     * The user can search between Names, HostNames and Host ids
     *
     * @return  root BorderPane
     */
    public BorderPane afterLogin()
    {
        //Initialising variables
        BorderPane root = new BorderPane();
        BorderPane topRoot = new BorderPane();
        HBox topHRoot = new HBox();
        ScrollPane scrollPane = new ScrollPane();
        ChoiceBox<String> searchChoiceBox = new ChoiceBox<>();
        HBox topRight = new HBox(10);
        detailsVBox = new VBox();
        searchList = new VBox();

        //setting position, spacing and padding
        detailsVBox.setSpacing(8);
        detailsVBox.setAlignment(Pos.CENTER_LEFT);
        detailsVBox.setPadding(new Insets(10,0,10,20));
        topHRoot.setSpacing(15);
        topHRoot.setPadding(new Insets(5,5,5,5));
        searchList.setSpacing(5);
        searchList.setPadding(new Insets(5,10,5,10));

        //setting the content on the ScrollPane and giving it a size
        scrollPane.setContent(searchList);
        scrollPane.setPrefSize(400,600);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);

        //Search Label
        Label searchTypeLabel = new Label("Search in: ");
        searchTypeLabel.setPadding(new Insets(3,0,0,0));

        //adding content to the search ChoiceBox
        searchChoiceBox.getItems().addAll("Name","Host_Name","Host_ID");
        searchChoiceBox.setValue("Name");

        //Property found Label
        //Invisible by default
        availableProps = new Label("Properties Found: ");
        availableProps.setPadding(new Insets(3,0,0,120));
        availableProps.setVisible(false);
        searchField = new TextField();

        //Search Button
        Button searchGo = new Button("Search");
        searchGo.setOnMouseClicked(e->{
            //shows an error if the input length is less than 3.
            if(searchField.getText().length()<3)
            {
                controller.errorHandler("Input Too Short!!!","Minimum 3 digits is required", Alert.AlertType.ERROR);
            }
            else{
                search(searchChoiceBox.getValue(),searchField.getText());
            }
        });

        //Logout Button
        Button logout = new Button("Logout");
        logout.setOnMouseClicked(e-> logout() );

        //Password Button
        Button seePassword = new Button("Password");
        seePassword.setOnMouseClicked(e-> userLoginCalc.errorHandler("YOUR PASS IS VISIBLE","You can see it by going to Welcome Page and clicking and holding the picture", Alert.AlertType.CONFIRMATION));

        //Adding Logout and Password Buttons to the HBox
        topRight.getChildren().addAll(seePassword,logout);

        //Name of the User
        //Which was invisible in the controller class
        //and becomes visible when the user logs in
        //and it shows the name of the user all in capital
        //and by clicking this label, user will be asked if he wants to logout or not
        //By clicking Ok, the user will be logged out.
        controller.userName.setText("Hello "+userLoginCalc.returnLoggedUser(user.getText()).toUpperCase()+"!");
        controller.userName.setVisible(true);
        controller.userName.setOnMouseClicked(e-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("LOGOUT CONFIRMATION");
            alert.setContentText("By pressing Ok you will be logged out!");
            ButtonType confirmLogout = new ButtonType("Logout :(");
            Optional<ButtonType> action = alert.showAndWait();
            if(action.get()==ButtonType.OK)
            {
                logout();
            }
        });

        //adding the top content to the topHRoot
        topHRoot.getChildren().addAll(searchTypeLabel,searchChoiceBox,searchField,searchGo,availableProps);
        topRoot.setLeft(topHRoot);
        topRoot.setRight(topRight);

        //adding all the content to the main root
        root.setLeft(scrollPane);
        root.setCenter(detailsVBox);
        root.setTop(topRoot);

        return root;
    }

    /**
     * This method gets the Property by its parameter and
     * gets all the necessary data from the UserLoginCalculator Class and
     * displays all the information about the property
     *
     * @param property getting the Property
     */
    public void setDetailsVBox(String property)
    {
        detailsVBox.getChildren().removeAll(detailsVBox.getChildren());
        AirbnbListing prop = userLoginCalc.getPropDetails(property);
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
     * This Method gets the search type and the searched text by its parameter, and
     * decides which method to call
     *
     * @param sort  Sort type
     * @param name  getting the searched text
     */
    public void search(String sort,String name) {
        if(sort.equals("Name"))
        {
            setSearchList(userLoginCalc.searchName(name));
        }
        if(sort.equals("Host_Name"))
        {
            setSearchList(userLoginCalc.searchHostName(name));
        }
        if(sort.equals("Host_ID"))
        {
            setSearchList(userLoginCalc.searchHostId(name));
        }
    }

    /**
     * This method adds the found search results to the VBox inside the ScrollPane
     *
     * @param list  an ArrayList of type AirbnbListing
     */
    public void setSearchList(ArrayList<AirbnbListing> list) {

        //removing all the current properties inside the Stage
        searchList.getChildren().removeAll(searchList.getChildren());
        //checks the results, shows an error if nothing found
        if(list.isEmpty())
        {
            controller.errorHandler("No Mach Found", "No property found with these specifications :( ", Alert.AlertType.INFORMATION);
        }
        else {
            availableProps.setVisible(true);        //making this label visible
            availableProps.setText("Properties Found: "+String.valueOf(list.size()));

            //adding add the properties as a Label followed by a Separator
            //and adding action to it
            for (int i = 0; i < list.size(); i++) {
                Label label = new Label(list.get(i).getName());
                Separator separator = new Separator(Orientation.HORIZONTAL);
                int finalI = i;
                label.setOnMouseClicked(e -> setDetailsVBox(list.get(finalI).getName()));
                searchList.getChildren().addAll(label, separator);
            }
        }
    }

    /**
     * This method opens the default web browser of the user and shows the location of the property selected
     *
     * @param property  getting the Property
     * @throws Exception
     */
    public void googleMapStage(String property) throws Exception {
        AirbnbListing prop = userLoginCalc.getPropDetails(property);
        URI uri = new URI("https://google.com/maps/place/" + prop.getLatitude() + "," + prop.getLongitude());
        java.awt.Desktop.getDesktop().browse(uri);
    }

    /**
     * this private method is called when the user tries to login
     * it calls a method in the UserCalculator and gets and stores the result in a boolean variable
     *
     * @return isLoggedIn a boolean which shows the result of the login request
     */
    private boolean login() {
        isLoggedIn = userLoginCalc.authenticate(user.getText(),userPassword.getText());
        controller.updateScreen();
        return isLoggedIn;
    }

    /**
     * This method loges the user out
     * and updates the screen if the user is in the Login Page
     * and it also changes the isLoggedIn boolean to false
     *
     * @return  always returns false
     */
    public boolean logout() {
        controller.userName.setVisible(false);
        isLoggedIn = false;
        if(controller.currentPageNO == 3)
        {
            controller.updateScreen();
        }
        controller.mainPage.welcomeLabel.setText("Login to see your Password :)");
        return false;
    }

    /**
     *  This method creates a new Stage
     *  which user can create account, by entering its Name, UserName and Password
     *
     * @return  boolean accountCreated
     * @throws FileNotFoundException
     */
    public boolean createAccount() throws FileNotFoundException
    {
        //Initializing variables
        VBox vRoot = new VBox();
        HBox button = new HBox();
        Stage newAccount = new Stage();
        BorderPane name = new BorderPane();
        BorderPane email = new BorderPane();
        BorderPane pass2 = new BorderPane();
        BorderPane pass1 = new BorderPane();
        TextField nameField = new TextField();
        PasswordField pass1Field = new PasswordField();
        PasswordField pass2Field = new PasswordField();
        TextField emailField = new TextField();
        Scene scene = new Scene(vRoot);

        //setting the size of the Stage
        newAccount.setWidth(400);
        newAccount.setHeight(300);

        //Name Label
        Label nameLabel = new Label("Name: ");
        newAccount.setTitle("Create a new Account");

        //UserName Label
        Label emailLabel = new Label("UserName: ");


        //Password, Confirm Password Label
        Label pass1Label = new Label("Password: ");
        Label pass2Label = new Label("Confirm password: ");

        //Create Account Button
        Button create = new Button("Create my account");
        create.setOnMouseClicked(e->{
            try {
                accountCreator(nameField.getText(),emailField.getText().toLowerCase(),pass1Field.getText(),pass2Field.getText());
                //if the account was created successfully, it closes the Stage
                if(userLoginCalc.isCreated)
                {
                    newAccount.close();
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        //adding the Labels, Buttons ,... to its roots
        vRoot.getChildren().addAll(name,email,pass1,pass2,button);
        button.getChildren().addAll(create);
        name.setLeft(nameLabel);
        name.setRight(nameField);
        email.setLeft(emailLabel);
        email.setRight(emailField);
        pass1.setLeft(pass1Label);
        pass1.setRight(pass1Field);
        pass2.setLeft(pass2Label);
        pass2.setRight(pass2Field);

        //add positions, spacing and padding
        button.setSpacing(20);
        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(10));
        name.setPadding(new Insets(10,20,10,20));
        email.setPadding(new Insets(10,20,10,20));
        pass1.setPadding(new Insets(10,20,10,20));
        pass2.setPadding(new Insets(10,20,10,20));
        vRoot.setPadding(new Insets(10,10,10,10));

        newAccount.initModality(Modality.APPLICATION_MODAL);        //other Stages can't be in use while this stage is open
        newAccount.initStyle(StageStyle.UTILITY);                   //setting the style of the Stage
        newAccount.setScene(scene);                                 //setting the Scene
        newAccount.show();                                          //Showing the Stage

        return accountCreated;
    }

    /**
     * This Method gets the new UserName details and passes it to createAccount() method
     * in the UserLoginCalculator Class
     *
     * @param name gets the name
     * @param user  gets the Username
     * @param password1 gets the Password
     * @param password2 gets the Confirmation of the Password
     * @return  always returns false
     * @throws IOException
     * @throws URISyntaxException
     */
    public boolean accountCreator(String name, String user, String password1,String password2) throws IOException, URISyntaxException {
        userLoginCalc.createAccount(name,user,password1,password2);
        return false;
    }

    /**
     * This method creates a new Stage and asks a user
     * for their username
     *
     * @return passwordReset boolean
     */
    public boolean resetPass()
    {
        //Initialising variables
        forgotPass = new Stage();
        BorderPane root = new BorderPane();
        BorderPane email = new BorderPane();
        TextField emailField = new TextField();
        Scene scene = new Scene(root);

        //setting the size of the Stage
        forgotPass.setWidth(270);
        forgotPass.setHeight(120);

        forgotPass.setTitle("Restore your password");       //setting the title of the Stage

        //UserName Label
        Label emailLabel = new Label("UserName: ");

        //RestorePass Label
        Button restore = new Button("Restore My Password :)");
        restore.setOnMouseClicked(e-> userLoginCalc.resetPassAuthenticate(emailField.getText().toLowerCase()));

        //set padding
        emailLabel.setPadding(new Insets(5,10,5,5));
        emailField.setPadding(new Insets(5,5,5,10));
        root.setPadding(new Insets(10,10,10,10));

        //adding Labels and Buttons and Panes to its roots
        email.setLeft(emailLabel);
        email.setRight(emailField);
        email.setBottom(restore);
        BorderPane.setAlignment(restore,Pos.CENTER);
        root.setLeft(email);

        forgotPass.initModality(Modality.APPLICATION_MODAL);                //other Stages can't be in use while this stage is open
        forgotPass.initStyle(StageStyle.UTILITY);                           //setting the style of the Stage
        forgotPass.setScene(scene);                                         //setting the Scene
        forgotPass.show();                                                  //showing the Stage

        return passwordReset;
    }
}

