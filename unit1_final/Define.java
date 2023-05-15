import java.util.ArrayList;
import java.util.HashMap;

public class Define {
    private static HashMap<String, String> defines = new HashMap<>();
    private static HashMap<String, ArrayList<String>> paras = new HashMap<>();

    public static void addFunc(String str) {
        //将函数定义式解析为定义部分和形式参数部分记录下来
        ArrayList<String> arrPara = new ArrayList<>();
        /*String str1 = Change.deleteWhitespace(str);
        String str2 = Change.deleteForeheadZero(str1);
        String str3 = Change.deletePlusMinusStr(str2);
        String str4 = Change.rep(str3, "\\*\\*", "^");
        String input = Change.rep(str4, "\\^\\+", "\\^");//把**换成了^*/
        String input = str;
        String name = String.valueOf(input.charAt(0));
        int indexOfEqual = input.indexOf('=');
        String exprStr = input.substring(indexOfEqual + 1);
        Lexer lexer = new Lexer(exprStr);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        String ans = expr.toPoly().toString();//hw3
        int indexOfLeft = input.indexOf('(');
        int indexOfRight = input.indexOf(')');
        char[] charArr = input.substring(indexOfLeft + 1, indexOfRight).toCharArray();
        for (char c : charArr) {
            if (c == 'x' || c == 'y' || c == 'z') {
                arrPara.add(String.valueOf(c));
            }
        }
        defines.put(name, ans);
        paras.put(name, arrPara);
    }

    public static String repFunction(String name, ArrayList<Factor> actualParas) {
        //传入函数名和实际参数，返回改变后的String
        String expr = defines.get(name);//表达式
        ArrayList<String> formalParas = paras.get(name);//形式参数
        int keyOfX = -1;
        int keyOfY = -1;
        int keyOfZ = -1;//x是第几个形参，对应第几个实参
        for (int i = 0; i < formalParas.size(); i++) {
            if (formalParas.get(i).equals("x")) {
                keyOfX = i;
            }
            if (formalParas.get(i).equals("y")) {
                keyOfY = i;
            }
            if (formalParas.get(i).equals("z")) {
                keyOfZ = i;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == 'x' && keyOfX != -1) {
                sb.append("(");
                sb.append(actualParas.get(keyOfX).toString());
                sb.append(")");
            } else if (expr.charAt(i) == 'y' && keyOfY != -1) {
                sb.append("(");
                sb.append(actualParas.get(keyOfY).toString());
                sb.append(")");
            } else if (expr.charAt(i) == 'z' && keyOfZ != -1) {
                sb.append("(");
                sb.append(actualParas.get(keyOfZ)).toString();
                sb.append(")");
            } else {
                sb.append(expr.charAt(i));
            }
        }
        return sb.toString();
    }

}
