import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


/**
 * This Class handles the Loading the data from the csv file
 *
 * @author Michael Kölling, Josh Murphy, Seyed Mohammad Reza Shahrestani
 * @version 27/03/2020
 */
public class AirbnbDataLoader {
    Controller controller;
    AirbnbListing listing;
    ArrayList<AirbnbListing> listings;
    ArrayList<ArrayList> priceSorted = new ArrayList<ArrayList>();
    ArrayList<AirbnbListing> priceListing = new ArrayList<>();
    HashMap<String,AirbnbListing> hashMap = new HashMap<>();
    ArrayList<HashMap> hashMapList = new ArrayList();
    ArrayList<String> boroughs =new ArrayList<>();
    ArrayList<Integer> priceList = new ArrayList<>();
    ArrayList<AirbnbListing> searchList = new ArrayList<>();
    ArrayList<HashMap> propList = new ArrayList<>();
    HashMap<String,AirbnbListing> propHash = new HashMap<>();

    /**
     * The constructor of the AirbnbDataLoader CLass
     * which gives an access to Controller Class
     *
     * @param controller Controller
     */
    public AirbnbDataLoader(Controller controller)
    {
        this.controller = controller;
    }

    /**
     * Return an ArrayList containing the rows in the AirBnB London data set csv file.
     */
    public ArrayList<AirbnbListing> load() {
        System.out.print("Begin loading Airbnb london dataset...");
        listings = new ArrayList<AirbnbListing>();
        searchList.clear();
        priceListing.clear();
        priceSorted.clear(); //added neww
        try{
            URL url = getClass().getResource("airbnb-london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                String name = line[1];
                String host_id = line[2];
                String host_name = line[3];
                String neighbourhood = line[4];
                double latitude = convertDouble(line[5]);
                double longitude = convertDouble(line[6]);
                String room_type = line[7];
                int price = convertInt(line[8]);
                int minimumNights = convertInt(line[9]);
                int numberOfReviews = convertInt(line[10]);
                String lastReview = line[11];
                double reviewsPerMonth = convertDouble(line[12]);
                int calculatedHostListingsCount = convertInt(line[13]);
                int availability365 = convertInt(line[14]);

                listing = new AirbnbListing(id, name, host_id,
                        host_name, neighbourhood, latitude, longitude, room_type,
                        price, minimumNights, numberOfReviews, lastReview,
                        reviewsPerMonth, calculatedHostListingsCount, availability365);

                listings.add(listing);
                priceList.add(price);
                hashMap.put(neighbourhood,listing);
                hashMapList.add(hashMap);

                //if the price of the property was between the minimum and maximum
                //selected in the PriceSpinner:
                if (price<=controller.maxPrice && price>=controller.minPrice)
                {
                    propHash.put(name,listing);
                    propList.add(propHash);
                    priceSorted.add(listings);
                    priceListing.add(listing);
                    searchList.add(listing);
                }

                boroughs.add(neighbourhood);
                Set<String> boroughsTemp = new LinkedHashSet<String>(boroughs);
                boroughs.clear();
                boroughs.addAll(boroughsTemp);


            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        System.out.println("Success! Number of loaded records: " + listings.size());

        Set<Integer> priceTemp = new LinkedHashSet<Integer>(priceList);
        priceList.clear();
        priceList.addAll(priceTemp);
        Collections.sort(priceList);
        return listings;
    }

    /**
     *
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is
     * either empty or just whitespace
     */
    private Double convertDouble(String doubleString){
        if(doubleString != null && !doubleString.trim().equals("")){
            return Double.parseDouble(doubleString);
        }
        return -1.0;
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }

    /**
     * Returns Listing
     *
     * @return an ArrayList of AirbnbListing
     */
    public ArrayList<AirbnbListing> getListings() {
        return listings;
    }
}
