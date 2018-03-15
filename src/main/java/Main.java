import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class Main
{
    public static void main(String[] args)
    {
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyBqIHo3LmmfthV8q0tpibDuNy7DfBlgUE0").build();
        GeocodingResult[] results =  GeocodingApi.geocode(context,"1600 Amphitheatre Parkway Mountain View, CA 94043").await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(results[0].addressComponents));
    }
}