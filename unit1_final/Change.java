import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Change {
    public static String rep(String input, String reg, String r) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(input);
        String str = m.replaceAll(r);
        return str;
    }

    public static String repComaInFunction(String input) {
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        for (pos = 0; pos < input.length(); ) {
            char c = input.charAt(pos);
            if (c == 'f' || c == 'g' || c == 'h') {
                sb.append(c);
                pos++;
                sb.append(input.charAt(pos));//"("
                pos++;
                int leftBracket = 1;
                int rightBracket = 0;
                while (pos < input.length() && rightBracket < leftBracket) {
                    char temp = input.charAt(pos);
                    if (temp == '(') {
                        leftBracket++;
                    } else if (temp == ')') {
                        rightBracket++;
                    }
                    if (temp == ',') {
                        sb.append("#");
                    } else {
                        sb.append(temp);
                    }
                    pos++;
                }
            } else {
                sb.append(c);
                pos++;
            }
        }
        return sb.toString();
    }

    public static String deleteWhitespace(String input) {
        char[] charArr = input.toCharArray();
        char[] charArr2 = new char[charArr.length];
        int k = 0;
        for (char c : charArr) {
            if (!(Character.isWhitespace(c))) {
                charArr2[k++] = c;
            }
        }
        char[] charArr3 = new char[k];
        for (int i = 0; i < k; i++) {
            charArr3[i] = charArr2[i];
        }
        String str0 = String.valueOf(charArr3);
        return str0;
    }

    public static String deletePlusMinusStr(String str1) {
        int k;
        char[] charArr4 = str1.toCharArray();
        char[] charArr5 = new char[charArr4.length];
        k = 0;
        for (int i = 0; i < charArr4.length; ) {
            if ("+-".indexOf(charArr4[i]) != -1) {
                char c = charArr4[i];
                i++;
                while (charArr4[i] == '-' | charArr4[i] == '+') {
                    if (charArr4[i] == '-') {
                        if (c == '+') {
                            c = '-';
                        } else if (c == '-') {
                            c = '+';
                        }
                    }
                    i++;
                }
                charArr5[k++] = c;
            } else {
                charArr5[k++] = charArr4[i];
                i++;
            }
        }
        char[] charArr6 = new char[k];

        for (int i = 0; i < k; i++) {
            charArr6[i] = charArr5[i];
        }

        String str = String.valueOf(charArr6);
        return str;
    }

    public static String deleteForeheadZero(String str) {
        char[] ch = str.toCharArray();
        char[] ans = new char[ch.length];
        int i;
        int k = 0;
        for (i = 0; i < ch.length; ) {
            if (Character.isDigit(ch[i])) {
                while (ch[i] == '0') {
                    i++;
                    if (i == ch.length) {
                        ans[k++] = '0';
                        break;
                    }
                }
                if (i < ch.length) {
                    if (Character.isDigit(ch[i])) {
                        while (Character.isDigit(ch[i])) {
                            ans[k++] = ch[i++];
                            if (i >= ch.length) {
                                break;
                            }
                        }
                    } else {
                        ans[k++] = '0';
                    }
                } else {
                    break;
                }
            } else {
                ans[k++] = ch[i++];
            }
        }
        char[] charArr = new char[k];
        for (i = 0; i < k; i++) {
            charArr[i] = ans[i];
        }
        String s = String.valueOf(charArr);
        return s;
    }
}