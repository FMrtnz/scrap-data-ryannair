//import java.lang.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Flight {
    public static void getFlights(String departure, String destination, String dateOut, String dateIn){
      String url2 = "http://www.ryanair.com/api/booking/v4/fr-fr/availability?ADT=1&CHD=0&DateIn="+dateIn+"&DateOut="+dateOut+"&Destination="+destination+"&FlexDaysIn=0&FlexDaysOut=0&INF=0&Origin="+departure+"&RoundTrip=true&promoCode=&tpAdults=1&tpChildren=0&tpDestinationIata="+destination+"&tpDiscount=0&tpEndDate="+dateOut+"&tpInfants=0&tpIsConnectedFlight=false&tpIsReturn=true&tpOriginIata="+departure+"&tpPromoCode=&tpStartDate="+dateIn+"&tpTeens=0&FlexDaysBeforeOut=0&FlexDaysBeforeIn=0&ToUs=AGREED";
      try{
        List<Map<String,String>> flights = URLReader.getData(url2);
        System.out.print(url2);
        display_flight(flights, dateIn, dateOut, destination);
      } catch(Exception e){
    	  System.out.println(e);
      }
    }

    public static void display_flight(List<Map<String,String>> flights, String dateIn, String dateOut, String destination) {

      flights.removeIf(flight -> !valide_flight(flight, dateOut, dateIn, destination));

       for (Map<String,String> flight : flights){
        String date_map = flight.get("Dprt_date").toString();

        System.out.print(flight.get("Departure"));
        System.out.print(" ---> ");
        System.out.println(flight.get("Arrival"));
        System.out.print("Time : ");
        System.out.print(flight.get("Dprt_time"));
        System.out.print("  ");
        System.out.println(date_map);
        System.out.print("Amount : ");
        System.out.println(flight.get("Amount"));
        if(flight.get("Departure").toString().contains(destination)){
          System.out.println("--------------------------------------------------");
        }
      }
    }

    public static boolean valide_flight(Map<String,String> flight, String dateOut, String dateIn, String destination){
      boolean check_dprt = flight.get("Arrival").toString().contains(destination);
      boolean check_back = flight.get("Departure").toString().contains(destination);
      String date_map = flight.get("Dprt_date").toString();

      try {
        boolean condtion1 = compare_date(date_map, dateOut) && check_dprt;
        boolean condtion2 = compare_date(date_map, dateIn) && check_back;
        return condtion1 || condtion2;
      }catch(Exception e){
    	  System.out.println(e);
        return false;
      }
    }

    public static boolean compare_date(String date_map, String date_write ) throws Exception{
      try {
        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(date_map);
        Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(date_write);
        return date1.equals(date2);
      }catch(Exception e){
    	  System.out.println(e);
          return false;
      }
      
    }

    public static boolean compare_hours(String hour1, String hour2){

      String[] parts = hour1.split(":");
      Calendar cal1 = Calendar.getInstance();
      cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
      cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
      cal1.set(Calendar.SECOND, 0);

      parts = hour2.split(":");
      Calendar cal2 = Calendar.getInstance();
      cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
      cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
      cal2.set(Calendar.SECOND, 0);

      // Add 1 day because you mean 00:16:23 the next day
      cal2.add(Calendar.DATE, 1);

      return cal1.before(cal2);
    }

}
