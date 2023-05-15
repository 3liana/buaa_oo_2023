import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inout {
    private static Scanner sc = new Scanner(System.in);

    private static int numOfD = 0;

    public static int getNumOfD() {
        return Inout.numOfD;
    }

    public static String myReadString() {
        String input = sc.nextLine();
        String regexOfD = "d";
        Pattern patternOfD = Pattern.compile(regexOfD);
        Matcher m = patternOfD.matcher(input);
        while (m.find()) {
            numOfD++;
        }
        input = Change.rep(input, "\\*\\*", "^");// 把两个星星换个符号表示

        input = Change.deleteWhitespace(input);//去掉空白符

        input = Change.rep(input, "\\^\\+", "\\^");//把**换成了^

        String str = Change.deletePlusMinusStr(input);//去掉连着的正负

        str = Change.deleteForeheadZero(str);//去掉前导0
        return str;
    }
}
