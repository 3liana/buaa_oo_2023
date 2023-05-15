
import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        String n = Inout.myReadString();
        int num = parseInt(n);
        for (int i = 0; i < num; i++) {
            String input = Inout.myReadString();
            Define.addFunc(input);
        }
        String str = Inout.myReadString();
        if (Inout.getNumOfD() > 1) {
            System.out.println("Wrong Format");
            return;
        }
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        Poly poly = expr.toPoly();
        String ans = poly.toString();
        ans = Change.deletePlusMinusStr(ans);
        String result = Change.rep(ans, "\\^", "**");

        System.out.println(result);
    }
}