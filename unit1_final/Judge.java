import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Judge {
    public static boolean judgeStringOfFactor(String str) {
        String regexVar = "[xyz](\\*\\*\\d)?";
        String regexNum = "(\\+|-)?\\d+";
        Pattern patternVar = Pattern.compile(regexVar);
        Pattern patternNum = Pattern.compile(regexNum);
        Matcher matcherVar = patternVar.matcher(str);
        Matcher matcherNum = patternNum.matcher(str);
        if (matcherVar.matches()) {
            return true;
        } else if (matcherNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /*public static void main(String[] args) {
        String str = "+4";
        System.out.println(Judge.judgeStringOfFactor(str));
    }*/

}
