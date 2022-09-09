import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.util.*;

/**
 * This class handles the calculations of StatisticsViewer Class
 *
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 26/03/2020
 */
public class StatisticsCalculator {
    ArrayList<String> allStatTitles, allStatTexts,inUse;
    Deque<String> waitingQueue;
    AirbnbDataLoader loader;
    UserLoginCalculator userCalc;

    /**
     * The constructor of the StatisticsCalculator Class
     * which gives an access to AirbnbDataLoader and UserLoginCalculator Classes
     *
     * @param loader    AirbnbDataLoader
     * @param userCalc  UserLoginCalculator
     */
    public StatisticsCalculator(AirbnbDataLoader loader, UserLoginCalculator userCalc) {
        this.loader =loader;
        this.userCalc =userCalc;
    }

    /**
     * Setting the Lists and Queues
     */
    public void setQueue()
    {
        allStatTitles = new ArrayList<>();
        allStatTexts = new ArrayList<>();
        inUse = new ArrayList<>();
        waitingQueue = new ArrayDeque<>();

        allStatTitles.add("Average review per Property");
        allStatTexts.add(String.valueOf(averageReviewPerProp()));

        allStatTitles.add("Total available Properties");
        allStatTexts.add(String.valueOf(totalAvailableProp()));

        allStatTitles.add("Number of Homes/Apartments");
        allStatTexts.add(numberOfHomesApartments());

        allStatTitles.add("The most expensive Property");
        allStatTexts.add(mostExpensiveProperty());

        allStatTitles.add("Number of Created Accounts");
        allStatTexts.add(String.valueOf(numberOfUsers()));

        allStatTitles.add("Total number of Hosts");
        allStatTexts.add(String.valueOf(numberOfHosts()));

        allStatTitles.add("Host with the most Property");
        allStatTexts.add(hostWithMostProp());

        allStatTitles.add("The most available Property / lowest Price");
        allStatTexts.add(propWithMost365());

        allStatTitles.add("The most Reviewed Property");
        allStatTexts.add(theMostReviewedProp());

        waitingQueue.add(allStatTitles.get(4));
        waitingQueue.add(allStatTitles.get(5));
        waitingQueue.add(allStatTitles.get(6));
        waitingQueue.add(allStatTitles.get(7));
        waitingQueue.add(allStatTitles.get(8));
    }

    /**
     * this method will add the Boxes' current Statistic to a queue
     * then it will remove it from the inUse ArrayList,
     * and then adds the top of the queue to the Box
     * and removes it from the queue and adds it to inUse ArrayList
     *
     * @param title Gets the titleLabel
     * @param text  Gets the textLabel
     */
    public void nextStat(Label title , Label text )
    {
        waitingQueue.add(inUse.get(inUse.indexOf(title.getText())));                      //adding the current text to end of waitingQueue
        inUse.remove(inUse.indexOf(title.getText()));                                     //removing it from inUse list
        title.setText(waitingQueue.peek());                                               //setting the title, (from top of the queue)
        text.setText(allStatTexts.get(allStatTitles.indexOf(waitingQueue.peek())));       //setting the text
        inUse.add(waitingQueue.remove());                                                 //removing the head of the queue and adding it to inUse list
    }

    /**
     * this method will add the Boxes' current Statistic to a first of the queue
     * then it will remove it from the inUse ArrayList,
     * and then adds the bottom of the queue to the Box
     * and removes the last element from the queue and adds it to inUse ArrayList
     *
     * @param title Gets the titleLabel
     * @param text  Gets the textLabel
     */
    public void prevStat(Label title, Label text)
    {
        waitingQueue.addFirst(inUse.get(inUse.indexOf(title.getText())));                 //adding the current text to first of waitingQueue
        inUse.remove(inUse.indexOf(title.getText()));                                     //removing it from inUse list
        title.setText(waitingQueue.getLast());                                            //setting the title, (from bottom of the queue)
        text.setText(allStatTexts.get(allStatTitles.indexOf(waitingQueue.getLast())));    //setting the text
        inUse.add(waitingQueue.removeLast());                                             //removing the end of the queue and adding it to inUse list
    }

    /**
     * This method will add the Boxes' current Statistic to a queue
     * then it removes is from the inUse ArrayList,
     * then sets the selected Statistic to selected Box,
     * and adds the selected Statistic to inUse ArrayList,
     * and finds the selected Statistic in the queue using a loop and removes it.
     *
     * @param statTitle Selected Statistic by the user
     * @param title Gets the titleLabel of the Box
     * @param text  Gets the textLabel of the Box
     */
    public void forceStatSet(String statTitle,Label title , Label text)
    {
        waitingQueue.add(inUse.get(inUse.indexOf(title.getText())));
        inUse.remove(inUse.indexOf(title.getText()));
        title.setText(statTitle);
        text.setText(allStatTexts.get(allStatTitles.indexOf(statTitle)));
        inUse.add(statTitle);

        //finding the selected Statistic
        for (int i = 0; i<waitingQueue.size();i++)
        {
            String temp = waitingQueue.remove();
            if (waitingQueue.peek().equals(statTitle))
            {
                //removing the selected statistic from the queue
                waitingQueue.remove();
            }
            waitingQueue.add(temp);
        }
    }

    /**
     * Updates the Statistics ChoiceBox by
     * differentiating the List of all titles and inUse ArrayList using
     * removeAll() method
     *
     * @param choiceBox getting the ChoiceBox
     */
    public void updateChoiceBox(ChoiceBox choiceBox)
    {
        //clearing the ChoiceBox
        choiceBox.getItems().clear();

        //creating a new ArrayList with the same element in allStatTitles
        ArrayList<String> choiceBoxList = new ArrayList<>(allStatTitles);

        //removing the items that exists in inUse ArrayList
        choiceBoxList.removeAll(inUse);

        //adding the new ArrayList to the ChoiceBox
        for (int i = 0; i< choiceBoxList.size();i++)
        {
            choiceBox.getItems().add(choiceBoxList.get(i));
        }
    }

    /**
     * This method returns an ArrayList of AirbnbListing
     *
     * @return  loader.priceListing
     */
    public ArrayList<AirbnbListing> getListings()
    {
        return loader.priceListing;
    }


    /**
     * First Statistics
     * This method returns the average review per property by getting
     * all the property prices and dividing the Sum of the
     * numbers by total number of properties
     *
     * @return totalReview/getListings().size()
     */
    public int averageReviewPerProp()
    {
        int average = 0;
        int totalReview = 0;
        for (int i =0; i< getListings().size();i++)
        {
            totalReview = getListings().get(i).getNumberOfReviews() + totalReview;
        }
        return totalReview/getListings().size();
    }

    /**
     * Second Statistics
     * This method returns the total available properties
     * which we have calculated from getListing() method
     *
     * @return  getListings().size()
     */
    public int totalAvailableProp()
    {
        return getListings().size();
    }

    /**
     * Third Statistic
     * This method will have a count of apartments and entire houses of
     * all the properties
     *
     * @return String homes, apartments
     */
    public String numberOfHomesApartments()
    {
        int homes = 0;
        int apartments = 0;
        for (int i =0; i< getListings().size();i++)
        {
            //increment homes if found
            if(getListings().get(i).getRoom_type().equals("Private room"))
            {
                homes++;
            }
            //increment apartments if found
            if (getListings().get(i).getRoom_type().equals("Entire home/apt"))
            {
                apartments++;
            }
        }
        return "Homes: "+ homes+" / "+ "Apartments: "+ apartments;
    }

    /**
     * Fourth Statistic
     * This method will return name and price of the most expensive property
     * by using Comparator
     *
     * @return String name and price
     */
    public String mostExpensiveProperty()
    {
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for (int i =0; i< getListings().size();i++)
        {
            airbnbListings.add(getListings().get(i));
        }

        airbnbListings.sort(new Comparator<AirbnbListing>() {
            @Override
            public int compare(AirbnbListing o1, AirbnbListing o2) {
                int compare = 0;
                int totalO1 =0;
                int totalO2 = 0;
                totalO1 = o1.getPrice()*o1.getMinimumNights();
                totalO2 = o2.getPrice()*o2.getMinimumNights();
                compare = totalO1 - totalO2;
                return compare;
            }
        });
        return airbnbListings.get(airbnbListings.size()-1).getName()+" / "+"Price: "+ airbnbListings.get(airbnbListings.size()-1).getPrice();
    }

    /**
     * Fifth Statistics
     * This method will return the number of users in the user-login-data DataBase
     *
     * @return  userCalc.users.size()
     */
    public int numberOfUsers()
    {
        userCalc.load();
        return userCalc.users.size();
    }

    /**
     * Sixth Statistic
     * This method will return the total number of hosts
     *
     * @return hostNames.size()
     */
    public int numberOfHosts()
    {
        ArrayList<String> hostNames = new ArrayList<>();
        for (int i =0; i< getListings().size(); i++)
        {
            //if the host is not in the current ArrayList, add it to the ArrayList
            if(!hostNames.contains(getListings().get(i).getHost_name()))
            {
                hostNames.add(getListings().get(i).getHost_name());
            }
        }
        return hostNames.size();
    }

    /**
     * Seventh Statistic
     * This method will find the host with the most property
     * by using Comparator
     *
     * @return String hostName, Id, NumberOfProperties
     */
    public String hostWithMostProp()
    {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for (int i =0; i< getListings().size();i++)
        {
            airbnbListings.add(getListings().get(i));
        }

        airbnbListings.sort(new Comparator<AirbnbListing>() {
            @Override
            public int compare(AirbnbListing o1, AirbnbListing o2) {
                int compare = 0;
                int property1 =0;
                int property2 = 0;
                property1 = o1.getCalculatedHostListingsCount();
                property2 = o2.getCalculatedHostListingsCount();
                compare = property1 - property2;
                return compare;
            }
        });
        return "Host Name: " + airbnbListings.get(airbnbListings.size()-1).getHost_name()+" / "+"ID: " +airbnbListings.get(airbnbListings.size()-1).getHost_id()+" / "+ "Number of Properties: "+ airbnbListings.get(airbnbListings.size()-1).getCalculatedHostListingsCount();
    }


    /**
     * Eights Statistic
     * This method will find the property with the most availability in year
     * as there are so many properties with availability 365,
     * this method will calculates the most availability with the lowest price
     * using Comparator
     *
     * @return String Name, Availability, Price
     */
    public String propWithMost365()
    {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for (int i =0; i< getListings().size();i++)
        {
            airbnbListings.add(getListings().get(i));
        }

        airbnbListings.sort(new Comparator<AirbnbListing>() {
            @Override
            public int compare(AirbnbListing o1, AirbnbListing o2) {
                int compare = 0;
                int property1 =0;
                int property2 = 0;
                property1 = o1.getAvailability365();
                property2 = o2.getAvailability365();
                if (compare==0)
                {
                    property2 = o1.getPrice();
                    property1 = o2.getPrice();
                }
                compare = property1 - property2;
                return compare;
            }
        });
        return airbnbListings.get(airbnbListings.size()-1).getName()+ " / "+ "Availability: "+ airbnbListings.get(airbnbListings.size()-1).getAvailability365()+ " / "+"Price: "+airbnbListings.get(airbnbListings.size()-1).getPrice();
    }

    /**
     * Ninth Statistic
     * This method will return the name and number of reviews of the most
     * reviewed property, using Comparator
     *
     * @return String Name, NumberOfReviews
     */
    public String theMostReviewedProp()
    {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for (int i =0; i< getListings().size();i++)
        {
            airbnbListings.add(getListings().get(i));
        }

        airbnbListings.sort(new Comparator<AirbnbListing>() {
            @Override
            public int compare(AirbnbListing o1, AirbnbListing o2) {
                int compare = 0;
                int property1 =0;
                int property2 = 0;
                property1 = o1.getNumberOfReviews();
                property2 = o2.getNumberOfReviews();
                compare = property1 - property2;
                return compare;
            }
        });
        return airbnbListings.get(airbnbListings.size()-1).getName()+ " / "+ "Reviews: " + airbnbListings.get(airbnbListings.size()-1).getNumberOfReviews();
    }
}