import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class test {
    public static void main(String[] args)  {
        String inputString = "Hello World";

        // Finding the index of the first whitespace
        int spaceIndex = inputString.indexOf(' ');

        if (spaceIndex != -1) {
            // Extracting the first and second words
            String firstWord = inputString.substring(0, spaceIndex);
            String secondWord = inputString.substring(spaceIndex + 1);

            System.out.println("First word: " + firstWord);
            System.out.println("Second word: " + secondWord);
            System.out.println(spaceIndex);
        } else {
            System.out.println("No whitespace found in the input string.");
        }
    }

    public static long current_timestamp(){
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String formattedTimestamp = currentDateTime.format(formatter);
        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedTimestamp, formatter);
        long unixTimestamp = parsedDateTime.atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
        return unixTimestamp;
    }

}

