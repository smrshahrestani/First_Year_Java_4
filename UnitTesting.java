
// import static org.junit.Assert.*;
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.net.URISyntaxException;
// import java.util.ArrayList;

// /**
// * The test class UnitTesting.
// *
// * @author Amirali Koochaki
// * @author Marouane El Moubarik Alaoui
// * @author Seyed Mohammad Reza Shahrestani
// * @author Zahra Sadat Ghorashi
// *
// * @version 29/03/2020
// */
// public class UnitTesting
// {
   // AirbnbDataLoader airbnbDataLoader;
   // Controller controller;
   // UserLoginViewer userLoginViewer;
   // UserLoginCalculator userLoginCalculator;
   // StatisticsViewer statisticsViewer;
   // StatisticsCalculator statisticsCalculator;
   // MapViewer mapViewer;
   // MapCalculator mapCalculator;
   // ArrayList<AirbnbListing> airbnbListings;
   // /**
    // * Default constructor for test class UnitTesting
    // */
   // public UnitTesting()
   // {
       // controller = new Controller();
       // airbnbListings = new ArrayList<>();
       // airbnbDataLoader = new AirbnbDataLoader(controller);
       // userLoginCalculator = new UserLoginCalculator(controller);
       // userLoginViewer = new UserLoginViewer(controller,userLoginCalculator);
       // statisticsCalculator = new StatisticsCalculator(airbnbDataLoader,userLoginCalculator);
       // statisticsViewer = new StatisticsViewer(statisticsCalculator);
       // mapCalculator = new MapCalculator(airbnbListings,airbnbDataLoader);
       // mapViewer = new MapViewer(mapCalculator,controller);
   // }

   // /**
    // * Sets up the test fixture.
    // *
    // * Called before every test case method.
    // */
   // @Before
   // public void setUp()
   // {
   // }

   // /**
    // * Tears down the test fixture.
    // *
    // * Called after every test case method.
    // */
   // @After
   // public void tearDown()
   // {
   // }

   // @Test
   // public boolean testCreatingAccount() throws IOException, URISyntaxException {
       // airbnbDataLoader.load();
       // userLoginCalculator.load();
       // userLoginViewer.createAccount();
       // userLoginCalculator.createAccount("","","","");
       // assertFalse(false);
       // return false;
   // }

   // @Test
   // public boolean testRestorePass()
   // {
       // airbnbDataLoader.load();
       // userLoginCalculator.load();
       // userLoginViewer.resetPass();
       // return false;
   // }

   // @Test
   // public boolean testLogin()
   // {
       // airbnbDataLoader.load();
       // userLoginCalculator.load();
       // userLoginViewer.isLoggedIn = true;
       // controller.updateScreen();
       // if (controller.currentPage == userLoginViewer.afterLogin())
       // {
           // return true;
       // }
       // else
       // {
           // return false;
       // }
   // }

   // @Test
   // public boolean testLogout()
   // {
       // airbnbDataLoader.load();
       // userLoginCalculator.load();
       // userLoginViewer.isLoggedIn = false;
       // controller.updateScreen();
       // if (controller.currentPage == userLoginViewer.beforeLogin())
       // {
           // return true;
       // }
       // else
       // {
           // return false;
       // }
   // }


// }
