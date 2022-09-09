import java.util.*;

/**
 *  This Class handles the calculation of the MapViewer Class
 *
 * @author Amirali Koochaki
 * @author Marouane El Moubarik Alaoui
 * @author Seyed Mohammad Reza Shahrestani
 * @author Zahra Sadat Ghorashi
 *
 * @version 26/03/2020
 */
public class MapCalculator
{
    ArrayList<AirbnbListing> airbnbList;
    AirbnbDataLoader loader;
    ArrayList<Integer> xPositions = new ArrayList<>();
    ArrayList<Integer> yPositions = new ArrayList<>();
    ArrayList arrayList = new ArrayList();
    int maxBorough,minBorough;

    /**
     * The constructor for the MapCalculator Class
     * which gives an access to AirbnbDataLoader Class
     *
     * @param airbnbList    An ArrayList on AirbnbListing
     * @param loader    AirbnbDataLoader
     */
    public MapCalculator(ArrayList<AirbnbListing> airbnbList,AirbnbDataLoader loader)
    {
        this.airbnbList = airbnbList;
        this.loader = loader;
    }

    /**
     * This method return a listing of the borough needed
     *
     * @param boroughs  getting the Borough
     * @return  loader.hashMap.get(boroughs)
     */
    public AirbnbListing returnBoroughs(String boroughs)
    {
        return loader.hashMap.get(boroughs);
    }

    /**
     * This method returns a number between 0 to 255
     * by getting the min and max population of the price selected boroughs
     * and population of each borough.
     *
     * The calculation is: ((population-max)/(max-min))*255
     *
     * @param population    getting the Population
     * @return  calc    if maxBorough is greater than minBorough
     */
    public int setColor(int population)
    {
        //for efficiency
        if (maxBorough-minBorough == 0)
        {
            return 0;
        }
        else
        {
            return ((population - minBorough)*255/(maxBorough-minBorough));
        }
    }

    /**
     *  It returns the Max price of Boroughs
     *
     * @return  loader.priceList.get(loader.priceList.size()-1)
     */
    public int returnMaxPrice()
    {
        return loader.priceList.get(loader.priceList.size()-1);
    }

    /**
     *  It returns the Min price of Boroughs@return
     *
     * @return  loader.priceList.get(0)
     */
    public int returnMinPrice()
    {
        return loader.priceList.get(0);
    }

    /**
     * Setting the name of the Boroughs by map positioning order
     *
     * @return boroughsList
     */
    public ArrayList<String> boroughsList()
    {
        return new ArrayList<>(Arrays. asList("Enfield", "Barnet", "Haringey", "Waltham Forest","Harrow","Brent","Camden","Islington","Hackney","Redbridge","Havering","Hillingdon","Ealing","Kensington and Chelsea","Westminster","Tower Hamlets","Newham","Barking and Dagenham","Hounslow","Hammersmith and Fulham", "Wandsworth","City of London","Greenwich","Bexley","Richmond upon Thames","Merton","Lambeth","Southwark","Lewisham","Kingston upon Thames","Sutton","Croydon","Bromley"));
    }

    /**
     *  get a listing of Boroughs
     *
     * @param borough   getting the Borough
     */
    public void boroughListing(String borough) {
        for (int i =0; i < loader.hashMapList.size();i++)
        {
            if(loader.hashMapList.get(i).containsKey(borough))
            {
                arrayList.add(loader.hashMapList.get(i).get(borough));
            }
        }
    }


    /**
     * This method adds all the properties in a Borough to a list
     *
     * @param borough   getting the Borough
     * @return  arrayList
     */
    public ArrayList boroughToProperty(String borough)
    {
        airbnbList = new ArrayList<>();
        ArrayList arrayList = new ArrayList();
        for(int i = 0; i<loader.priceListing.size(); i++)
        {
            if (loader.priceListing.get(i).getNeighbourhood().equals(borough))
            {
                airbnbList.add(loader.priceListing.get(i));
                arrayList.add(loader.priceListing.get(i));
            }
        }
        return arrayList;
    }

    /**
     * It returns an airbnbList
     *
     * @return airbnbList
     */
    public ArrayList<AirbnbListing> borough()
    {
        return airbnbList;
    }

    /**
     * returns an ArrayList of AirbnbListing by getting the name of the Borough
     *
     * @param boroughs  getting the Borough
     * @return  airbnbListings
     */
    public ArrayList<AirbnbListing> returnList(String boroughs)
    {
        airbnbList.clear();
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for(int i = 0; i< boroughToProperty(boroughs).size();i++)
        {
            airbnbListings.add(borough().get(i));
        }
        return airbnbListings;
    }

    /**
     * this method sorts the properties inside a Borough by Name
     * and it can sort them reversely if its required
     * By default it sorts the Names from A to Z
     *
     * @param boroughs  getting the Borough
     * @param reverse   getting the boolean reverse
     * @return  arrayList
     */
    public ArrayList<String> nameSort(String boroughs,boolean reverse)
    {
        airbnbList.clear();
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i< boroughToProperty(boroughs).size();i++)
        {
            arrayList.add(borough().get(i).getName());
            Collections.sort(arrayList);
        }

        //reverse the fort if the reverse is true.
        if(reverse)
        {
            Collections.reverse(arrayList);
        }
        return arrayList;
    }


    /**
     * this method sorts the properties inside a Borough by Price
     * and it can sort them reversely if its required
     * By default it sorts the Prices from lower to higher
     *
     * @param boroughs  getting the Borough
     * @param reverse   getting the boolean reverse
     * @return  arrayList
     */
    public ArrayList<String> priceSort(String boroughs, boolean reverse)
    {
        airbnbList.clear();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for(int i = 0; i< boroughToProperty(boroughs).size();i++)
        {
            airbnbListings.add(borough().get(i));
        }

        //sorting AirbnbListing Object using Comparator
        airbnbListings.sort(new Comparator<AirbnbListing>() {
            @Override
            public int compare(AirbnbListing o1, AirbnbListing o2) {
                int compare;
                if(reverse)
                {
                    compare = o2.getPrice() - o1.getPrice();
                }
                else
                {
                    compare = o1.getPrice() - o2.getPrice();
                }
                return compare;
            }
        });
        for(int i = 0; i< boroughToProperty(boroughs).size();i++)
        {
            arrayList.add(airbnbListings.get(i).getName());
        }
        return arrayList;
    }

    /**
     * this method sorts the properties inside a Borough by number of Reviews
     * and it can sort them reversely if its required
     * By default it sorts the Reviews from higher to lower
     *
     * @param boroughs  getting the Borough
     * @param reverse   getting the boolean reverse
     * @return  arrayList
     */
    public ArrayList<String> revSort(String boroughs, boolean reverse)
    {
        airbnbList.clear();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<AirbnbListing> airbnbListings = new ArrayList<>();
        for(int i = 0; i< boroughToProperty(boroughs).size();i++)
        {
            airbnbListings.add(borough().get(i));
        }

        //sorting AirbnbListing Object using Comparator
        airbnbListings.sort(new Comparator<AirbnbListing>() {
            @Override
            public int compare(AirbnbListing o1, AirbnbListing o2) {
                int compare;
                if (reverse)
                {
                    compare = o1.getNumberOfReviews() - o2.getNumberOfReviews();

                }
                else
                {
                    compare = o2.getNumberOfReviews() - o1.getNumberOfReviews();
                }

                return compare;
            }
        });
        for(int i = 0; i< boroughToProperty(boroughs).size();i++)
        {
            arrayList.add(airbnbListings.get(i).getName());
        }
        return arrayList;
    }

    /**
     * returns an AirbnbListing of each property asked
     *
     * @param property  getting the property
     * @return  loader.propList.get(i).get(property)
     */
    public AirbnbListing getPropDetails(String property)
    {
        for (int i = 0; i< loader.propList.size(); i++)
        {
            if(loader.propList.get(i).containsKey(property))
            {
                return (AirbnbListing) loader.propList.get(i).get(property);
            }
        }
        return null;
    }

    /**
     *  returning an ArrayList of all Boroughs population in
     *  selected price
     *  this method is used for getting the min and max population of the Boroughs
     *
     * @return  getBouroughsMinMax
     */
    public ArrayList<Integer> getBouroughsMinMax()
    {
        ArrayList<Integer> getBouroughsMinMax = new ArrayList<>();
        for (int i =0; i< boroughsList().size();i++)
        {
            getBouroughsMinMax.add(boroughToProperty(boroughsList().get(i)).size());
        }
        Collections.sort(getBouroughsMinMax);
        return getBouroughsMinMax;
    }

    /**
     *  setting the position of the map
     */
    public void setPos()
    {
        //X position
        xPositions.add(366);
        xPositions.add(271);
        xPositions.add(337);
        xPositions.add(405);
        xPositions.add(176);
        xPositions.add(242);
        xPositions.add(308);
        xPositions.add(376);
        xPositions.add(442);
        xPositions.add(509);
        xPositions.add(574);
        xPositions.add(140);
        xPositions.add(205);
        xPositions.add(271);
        xPositions.add(337);
        xPositions.add(405);
        xPositions.add(471);
        xPositions.add(538);
        xPositions.add(176);
        xPositions.add(242);
        xPositions.add(308);
        xPositions.add(376);
        xPositions.add(442);
        xPositions.add(509);
        xPositions.add(213);
        xPositions.add(279);
        xPositions.add(347);
        xPositions.add(413);
        xPositions.add(480);
        xPositions.add(250);
        xPositions.add(318);
        xPositions.add(384);
        xPositions.add(451);

        //Y Position
        yPositions.add(39);
        yPositions.add(97);
        yPositions.add(97);
        yPositions.add(97);
        yPositions.add(155);
        yPositions.add(155);
        yPositions.add(155);
        yPositions.add(155);
        yPositions.add(155);
        yPositions.add(155);
        yPositions.add(155);
        yPositions.add(213);
        yPositions.add(213);
        yPositions.add(213);
        yPositions.add(213);
        yPositions.add(213);
        yPositions.add(213);
        yPositions.add(213);
        yPositions.add(271);
        yPositions.add(271);
        yPositions.add(271);
        yPositions.add(271);
        yPositions.add(271);
        yPositions.add(271);
        yPositions.add(329);
        yPositions.add(329);
        yPositions.add(329);
        yPositions.add(329);
        yPositions.add(329);
        yPositions.add(387);
        yPositions.add(387);
        yPositions.add(387);
        yPositions.add(387);
    }
}
