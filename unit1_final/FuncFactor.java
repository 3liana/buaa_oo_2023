import java.util.ArrayList;

public class FuncFactor extends Factor {
    private String newFunc;
    private Expr expr;

    public FuncFactor(String name, ArrayList<Factor> actualParas) {
        this.newFunc = Define.repFunction(name, actualParas);
        String str = Change.rep(this.newFunc, "\\*\\*", "^");
        //因为在toString替换那里把^又换成**了
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        this.expr = parser.parseExpr();
    }

    public Poly toPoly() {
        return this.expr.toPoly();
    }

    public Poly toDiffPoly(char ch) {
        return this.expr.toDiffPoly(ch);
    }

    public String toString() {
        if (Judge.judgeStringOfFactor(this.toPoly().toString())) {
            return this.toPoly().toString();
        } else {
            return "(" + this.toPoly().toString() + ")";//有问题
        }
    }
}
