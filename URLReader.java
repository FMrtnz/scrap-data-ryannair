  import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.*;

public class URLReader {
    public static String getContent(URL urlSt) throws Exception {
      URLConnection connection = urlSt.openConnection();
      String redirect = connection.getHeaderField("Location");
      if (redirect != null){
          connection = new URL(redirect).openConnection();
      }

      connection.setRequestProperty("User-Agent", RandomUserAgent.getRandomUserAgent());

      BufferedReader in = new BufferedReader(new InputStreamReader(
                                  connection.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null)
          return inputLine;
      in.close();
      return "error";
    }

    public static List<Map<String,String>> getData(String url) throws Exception {
      List<Map<String,String>> list = new ArrayList<Map<String,String>>();
      URL oracle = new URL(url);
      list = flights_to_array(getContent(oracle));
      return list;
    }

    public static String[] getAirportfrom(String airport, String field) throws Exception {
      String url = "https://www.ryanair.com/api/locate/4/common?embedded=airports,countries,cities,regions&market=fr-fr";
      try {
        URL oracle = new URL(url);   
        
        BufferedReader in = new BufferedReader( 
        new InputStreamReader(oracle.openStream()));
        String regex = "iataCode\":\"" + airport + "\"(.*?)priority\"";
        String inputLine = in.readLine();
        String[] airports = Pattern.compile("((?<="+field+"\\:).*?(?=\\\"))")
                .matcher(get_by_regex(inputLine,regex))
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        in.close();
        return airports;
      } catch (Exception e){
        System.out.println(e);
      }
      String[] airports = {};
      return airports;
    }

    public static List<Map<String,String>> flights_to_array(String string) {
      String[] matches = Pattern.compile(".flights.:\\[\\{(.*?)\\}\\]")
                          .matcher(string)
                          .results()
                          .map(MatchResult::group)
                          .toArray(String[]::new);
      List<Map<String,String>> maps = new ArrayList<Map<String,String>>();
      for(String match : matches){
        maps.add(map_a_flight(match));
      }
      return maps;
    }

    public static Map<String, String> map_a_flight(String flight) {
      Map<String,String> flight_map = new HashMap<String,String>();
      Map<String,String> details_map = new HashMap<String,String>();
       
      details_map.put("FlightKey","((?<=\\\"flightKey\\\":).*?(?=,))");
      details_map.put("Amount","((?<=\\\"amount\\\":)\\d*\\.*\\d*)");
      details_map.put("Departure","((?<=~~)[A-Z]{3}(?=~))");
      details_map.put("Dprt_date","((?<=~~[A-Z]{3}~)\\d+/\\d+/\\d+)");
      details_map.put("Dprt_time","(\\d+:\\d+(?=\\~[A-Z]{3}\\~))");
      details_map.put("Arrival","((?<=\\d~)[A-Z]{3}(?=~))");
      //details_map.put("Arr_date","((?<=\\d~[A-Z]{3}~)\\d+\\/\\d+\\/\\d+)");
      //details_map.put("Arr_time","((?<=\\d~)[A-Z]{3}(?=~))");

      details_map.forEach((k, v) -> flight_map.put(k, get_by_regex(flight, v)));
      return flight_map;
    }

    public static String get_by_regex(String string,String regex){
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(string);
      return matcher.find() ? matcher.group(1) : "erro found";
    }
}