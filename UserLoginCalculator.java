import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.scene.control.Alert;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


/**
 * This class handles the calculations of the UserLoginViewer Class
 *
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 27/03/2020
 */
public class UserLoginCalculator
{
    ArrayList<UserLoginViewer> userListing;
    public String name,email, password;
    ArrayList<String> users = new ArrayList<>();
    HashMap<String,String> userData = new HashMap<>();
    HashMap<String,String> nameUser = new HashMap<>();
    Controller controller;
    String path = "user-login-data.csv";
    boolean isCreated =false;

    /**
     * The constructor for the UserLoginCalculator Class
     * which gives an access to Controller Class
     *
     * @param controller Controller
     */
    public UserLoginCalculator(Controller controller)
    {
        this.controller=controller;
    }

    /**
     * This method loads the data from the csv file
     * and adds the data to different variables
     *
     * @return userListing ArrayList of type UserLoginViewer
     */
    public ArrayList<UserLoginViewer> load()
    {
        try{
            userListing = new ArrayList<UserLoginViewer>();
            URL url = getClass().getResource(path);
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;

            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
//                id = line[0];
                name = line[1];
                email = line[2];
                password = line[3];

                userData.put(email,password);           //for checking the Password
                nameUser.put(email,name);               //for displaying the User's name
                users.add(email);                       //for having a list of all users
            }
            reader.close();
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }

        //avoiding duplicates in the ArrayList of users.
        Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(users);
        users.clear(); // copying elements but without any duplicates
        users.addAll(primesWithoutDuplicates);

        return userListing;
    }


    /**
     * This method gets a username and a password by its parameter
     * and checks if the details entered is correct or not
     * it can distinguish and show the correct error
     *
     * @param user  getting username
     * @param password  getting password
     * @return  true if the provided details were correct
     */
    public boolean authenticate(String user, String password)
    {
        load();     //loading data

        //checks if the UserName is correct or not
        if (users.contains(user)) {

            //checks if the password matches the UserName
            if(userData.get(user).equals(password))
            {
                //display confirmation of the login
                errorHandler("Login Successful","You have successfully logged in", Alert.AlertType.INFORMATION);

                //displaying the name of the user logged in, in the bottom center og the Stage
                returnLoggedUser(user);
                controller.mainPage.welcomeLabel.setText("Your Password Is: "+ userData.get(user));

                return true;
            }

            //displays an error of incorrect password
            else
            {
                errorHandler("Password ERROR","The password you entered does'nt mach", Alert.AlertType.ERROR);
                return false;
            }
        }
        //displays an error of Invalid UserName
        else{
            errorHandler("UseName Invalid","This UserName does'nt exist", Alert.AlertType.ERROR);
        }
        return false;
    }

    /**
     * This method is for authenticating the Restore Password Stage
     * it shows the password if the UserName machs followed by a
     * confirmation massage
     * and it shows an error massage if the UserName does'nt mach
     *
     * @param user  gets the UserName
     */
    public void resetPassAuthenticate(String user)
    {
        load();     //loading data

        //checks if the UserName is correct or not
        if(users.contains(user))
        {
            //displays a confirmation massage
            errorHandler("PASS IS NOW VISIBLE","You can see your Password by going to Welcome Page and clicking and holding the picture", Alert.AlertType.CONFIRMATION);
            //adds the password to the Welcome Page hidden Label
            controller.mainPage.welcomeLabel.setText("Your Password Is: "+ userData.get(user));
            //it closes the Restore Password Stage
            controller.userLoginViewer.forgotPass.close();
        }
        //displays an error of Invalid UserName
        else
        {
            controller.mainPage.welcomeLabel.setText("Please enter a valid Email!");
            errorHandler("Email Invalid","This email does'nt exist", Alert.AlertType.ERROR);
        }
    }

    /**
     * This method creates a new account by getting
     * name,UserName and Password from the user
     * and it shows error if there is any
     *
     * @param name  getting the name
     * @param user  gettong the UseName
     * @param password1 getting the Password
     * @param password2 getting the Confirmation Password
     * @throws IOException
     * @throws URISyntaxException
     */
    public void createAccount(String name, String user, String password1,String password2) throws IOException, URISyntaxException {

        load();    //loads the data

        //if the UserName exists, it shows an error
        if(users.contains(user))
        {
            isCreated = false;
            errorHandler("Used Email Address", "This email address has been used before!", Alert.AlertType.ERROR);
        }
        else
            {
            String passwordFinal;
            //if the Length of the Password was greater than or equal to 5
            if (password1.length() >= 5)
            {
                //if the two Passwords mach
                if (password1.equals(password2))
                {
                    passwordFinal = password1;

                    //checking if the name field is empty
                    if(name.isEmpty())
                    {
                        //displays an Error if the name filed was blank
                        errorHandler("Name ERROR","You can't leave the name field blank", Alert.AlertType.ERROR);
                    }
                    else {
                        addToFile(name, user, passwordFinal);       //write to csv
                        isCreated = true;

                        //displaying a Confirmation massage of creating the account
                        errorHandler("Account Created!", "Your Account has been successfully created :)", Alert.AlertType.INFORMATION);

                        //displaying an Alert
//                        errorHandler("LOGIN ALERT", "Due to a technical issue, please close the application and open it again to be able to use your new account.", Alert.AlertType.WARNING);
                    }
                }
                // if the Passwords didn't mach
                else {
                    isCreated = false;
                    errorHandler("Password ERROR", "Passwords does'nt match", Alert.AlertType.ERROR);
                }

            }
            //if the length of the Password was less than 5
            else {
                isCreated = false;
                errorHandler("Password ERROR", "Minimum 5 characters is required for password", Alert.AlertType.ERROR);

            }
        }
    }


    /**
     * This method writes the data to the csv File
     *
     * @param name  gets the name
     * @param user  gets the UserName
     * @param password  gets the Password
     * @throws IOException
     */
    public void addToFile(String name,String user, String password) throws IOException
    {
        File file = new File(path);
        FileWriter outputFile = new FileWriter(file,true);
        CSVWriter writer = new CSVWriter(outputFile);
        ArrayList<String[]> data = new ArrayList<String[]>();

        //the actual content
        data.add(new String[]{"", name, user, password});

        writer.writeAll(data);
        writer.flush();
        writer.close();
    }


    /**
     * returns the name of the user logged in.
     *
     * @param user gets userName
     * @return  String nameUser.get(user)
     */
    public String returnLoggedUser(String user)
    {
        return nameUser.get(user);
    }

    /**
     *
     * This method is used to display errors, alerts and confirmations
     *
     * @param errorHeader A Header for the Error
     * @param errorName A Text for the Error
     * @param type  The type of the error
     */
    public void errorHandler(String errorHeader, String errorName, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(errorHeader);
        alert.setContentText(errorName);
        alert.showAndWait();
    }

    /**
     * returns an AirbnbListing of each property asked
     *
     * @param property  getting the property
     * @return  controller.load.propList.get(i).get(property) of type AirbnbListing
     */
    public AirbnbListing getPropDetails(String property)
    {
        for (int i = 0; i< controller.load.propList.size(); i++)
        {
            if(controller.load.propList.get(i).containsKey(property))
            {
                return (AirbnbListing) controller.load.propList.get(i).get(property);
            }
        }
        return null;
    }

    /**
     *
     */
    public ArrayList<AirbnbListing> searchName(String name)
    {
        ArrayList<AirbnbListing> search = new ArrayList<>();
        for(int i = 0; i< controller.load.searchList.size();i++)
        {
            if(controller.load.searchList.get(i).getName().toLowerCase().contains(name.toLowerCase()))
            {
                search.add(controller.load.searchList.get(i));
            }
        }
        return search;
    }

    /**
     * This method searches in the Host Ids
     *
     * @param hostId gets HostId
     * @return  an ArrayList of type AirbnbListing
     */
    public ArrayList<AirbnbListing> searchHostId(String hostId)
    {
        ArrayList<AirbnbListing> search = new ArrayList<>();
        for(int i = 0; i< controller.load.searchList.size();i++)
        {
            if(controller.load.searchList.get(i).getHost_id().contains(hostId))
            {
                search.add(controller.load.searchList.get(i));
            }
        }
        return search;
    }

    /**
     * This method searches in the Host Names
     *
     * @param hostName gets the HostName
     * @return an ArrayList of type AirbnbListing
     */
    public ArrayList<AirbnbListing> searchHostName(String hostName)
    {
        ArrayList<AirbnbListing> search = new ArrayList<>();
        for(int i = 0; i< controller.load.searchList.size();i++)
        {
            if(controller.load.searchList.get(i).getHost_name().toLowerCase().contains(hostName.toLowerCase()))
            {
                search.add(controller.load.searchList.get(i));
            }
        }
        return search;
    }
}