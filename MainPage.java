import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 26/03/2020
 *
 * This Class shows the Welcome Page content
 */
public class MainPage
{
    Label welcomeLabel;
    VBox welcomeDetails;

    /**
     * This method return a BorderPane and all its content to the controller class
     *
     * @return welcomeRoot BorderPane
     */
    public BorderPane show()
    {
        //initialising variables
        ImageView img1 = new ImageView();
        Image image1 = new Image("mainPagePic.jpg");  //getting the image from the path
        welcomeDetails = new VBox(10);
        StackPane stackRoot = new StackPane();
        BorderPane welcomeRoot = new BorderPane();

        //Welcome hidden Label
        welcomeLabel = new Label(" Welcome to BOYOUT ");
        welcomeLabel.setFont(new Font(30));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setPadding(new Insets(10,10,10,10));
        welcomeLabel.setMinWidth(20);
        welcomeLabel.setVisible(false);


        img1.setImage(image1);      //setting the Image


        //Label
        Label titleLabel = new Label("Welcome to BOYOUT:");
        titleLabel.setFont(new Font(40));
        titleLabel.setTextFill(Color.SPRINGGREEN);
        titleLabel.setStyle("-fx-font-weight: bold");

        //Label
        Label label1 = new Label("Your friendly interface in finding a home");
        label1.setFont(new Font(30));
        label1.setTextFill(Color.FORESTGREEN);

        //Label
        Label label2 = new Label("Panel 2: An interactive map that helps you decide where you want to rent");
        label2.setFont(new Font(24));
        label2.setTextFill(Color.ORANGE);

        //Label
        Label label3 = new Label("Panel 3: Statistics of all our available properties");
        label3.setFont(new Font(24));
        label3.setTextFill(Color.valueOf("#33cca6"));       //same colour used for Statistic boxes

        //Label
        Label label4 = new Label("Panel 4: Move on to this panel to Login/Create your own BOYOUT account");
        label4.setFont(new Font(24));
        label4.setTextFill(Color.STEELBLUE);

        //Label
        Label label5 = new Label("/ and search in the entire universe of BOYOUT");
        label5.setFont(new Font(24));
        label5.setTextFill(Color.STEELBLUE);

        //Label
        Label label6 = new Label("Please select a price range and press GO, to continue.");
        label6.setFont(new Font(22));
        label6.setTextFill(Color.AQUA);

        //adding Labels to the WelcomeDetails VRBox
        welcomeDetails.setVisible(true);
        welcomeDetails.setPadding(new Insets(10,10,10,40));
        welcomeDetails.setAlignment(Pos.CENTER_LEFT);
        welcomeDetails.getChildren().addAll(titleLabel,label1,label2,label3,label4,label5,label6);

        //adding the Image and Labels to its root
        stackRoot.getChildren().addAll(img1,welcomeLabel,welcomeDetails);

        //adding action event to the StackPane
        stackRoot.setOnMousePressed(e-> setPassVisible());
        stackRoot.setOnMouseReleased(e-> setPassInVisible());

        //adding the StackRoot the center of the WelcomeRoot BorderPane
        welcomeRoot.setCenter(stackRoot);

        return welcomeRoot;
    }

    /**
     * making the welcome Label visible and the Current Label invisible
     */
    public void setPassVisible()
    {
        welcomeLabel.setVisible(true);
        welcomeDetails.setVisible(false);
    }

    /**
     * making the Current Label visible and the Main Label invisible
     */
    public void setPassInVisible()
    {
        welcomeLabel.setVisible(false);
        welcomeDetails.setVisible(true);
    }
}