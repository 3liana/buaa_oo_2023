import java.math.BigInteger;

public class ExprFactor extends Factor {
    private Expr base;
    private int index;

    public ExprFactor(Expr base, int index) {
        this.base = base;
        this.index = index;
    }

    public Poly toPoly() {
        return Poly.powPoly(base.toPoly(), this.index);
    }

    public Poly toDiffPoly(char ch) {
        if (this.index == 0) {
            Mono temp = new Mono();
            temp.setCoe(BigInteger.ZERO);
            Poly tempPoly = new Poly();
            tempPoly.addMono(temp);
            return tempPoly;
        }
        Poly poly1 = Poly.powPoly(base.toPoly(), this.index - 1);
        Mono m = new Mono();
        m.setCoe(new BigInteger(String.valueOf(this.index)));
        Poly numPoly = new Poly();
        numPoly.addMono(m);
        Poly poly2 = base.toDiffPoly(ch);
        Poly ansPoly = Poly.mulPoly(poly1, numPoly);
        ansPoly = Poly.mulPoly(ansPoly, poly2);
        return ansPoly;
    }

    public String toString() {
        if (Judge.judgeStringOfFactor(this.toPoly().toString())) {
            return this.toPoly().toString();
        } else {
            return "(" + this.toPoly().toString() + ")";//有问题
        }
    }

}
