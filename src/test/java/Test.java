import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Test {
   public static void main(String[] args) {
      String regex = "<script>(.*?)</script>";
      String input = "232 This is a sample Text, <div> <script>alert`1`</script> </div>, with numbers in between. "
         + "\n This is the second line in the text "
         + "\n This is third line in the text 2322";
      //Creating a pattern object
      Pattern pattern = Pattern.compile(regex);
      //Creating a Matcher object
      Matcher matcher = pattern.matcher(input);
      //System.out.println("Current range: "+input.substring(regStart, regEnd));
      if(matcher.find()) {
         System.out.println("Match found");
      } else {
         System.out.println("Match not found");
      }
   }
}