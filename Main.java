//import java.lang.*;
import java.util.*;
import java.text.SimpleDateFormat;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Main {
  public static void main(String[] args) throws InterruptedException {

    // Set a city like MRS
    String dprt_airport = "XXX";
    List<String> airports = listAirports(dprt_airport);
    /*List<String> airportsFullName = AirportsCompletname(dprt_airport);*/
    int per_week = 4;
    int ix = 0;
    for(String airport : airports){
      int n = (int)(new Random().nextInt(10));
      Thread.sleep(n * 1000);
      // Set a date AAAA-MM-DD
      Date date_dprt = parseDate("XXXX-XX-XX");
      Date date_back = parseDate("XXXX-XX-XX");
      displayLoadState(airport, airports, "");
      ix++;
      for(int x = 0; x <= per_week; x++){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        Flight.getFlights(dprt_airport, airport, DATE_FORMAT.format(date_dprt),DATE_FORMAT.format(date_back));
        date_dprt = setDate(date_dprt, 7);
        date_back = setDate(date_back, 7);
      }
    }
  }
  
  public static List<String> listAirports(String airport){
    List<String> airports = new ArrayList<String>();
    try{
      airports = Arrays.asList(URLReader.getAirportfrom(airport, "airport"));
    } catch(Exception e){
      System.out.println(e);
    }
    return airports;
  }

  public static List<String> listAirportsFullName(String airport){
    List<String> airports = new ArrayList<String>();
    try{
      airports = Arrays.asList(URLReader.getAirportfrom(airport, "city"));
    } catch(Exception e){
      System.out.println(e);
    }
    return airports;
  }

  public static List<String> AirportsCompletname(String airport){
    List<String> airports = new ArrayList<String>();
    try{
      airports = Arrays.asList(URLReader.getAirportfrom(airport, "name"));
      System.out.println(airports);
    } catch(Exception e){
      System.out.println(e);
    }
    return airports;
  }

  public static void displayLoadState(String airport, List<String> airports, String fullName){
    System.out.println("------------------------");
    System.out.println("Airport : " + airport + " " + fullName);
    System.out.println((airports.indexOf(airport)+1) +"/" + airports.size());
    System.out.println("------------------------");
  }

  public static Date parseDate(String date){
    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public static Date setDate(Date date, int days){
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, days); 
    return cal.getTime();
  }
}