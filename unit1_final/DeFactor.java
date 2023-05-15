public class DeFactor extends Factor {
    private char ch;
    private Expr expr;

    public DeFactor(Expr expr, char ch) {
        this.expr = expr;
        this.ch = ch;
    }

    public Poly toPoly() {
        return expr.toDiffPoly(ch);
    }

    public String toString() {
        if (Judge.judgeStringOfFactor(expr.toDiffPoly(ch).toString())) {
            return expr.toDiffPoly(ch).toString();
        } else {
            return "(" + expr.toDiffPoly(ch).toString() + ")";//有问题
        }
        //return expr.toDiffPoly(ch).toString();
    }
}
