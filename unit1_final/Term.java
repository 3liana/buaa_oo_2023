
import java.util.ArrayList;

public class Term {
    private ArrayList<Factor> factors;
    private int sign;

    public Term() {
        this.factors = new ArrayList<Factor>();
        this.sign = 1;
    }

    public int getSign() {
        return this.sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Poly toPoly() {
        Poly p = new Poly();
        for (Factor factor : factors) {
            Poly temp = p;
            p = Poly.mulPoly(temp, factor.toPoly());
        }
        return p;
    }

    public Poly toDiffPoly(char ch) {
        Poly p = new Poly();
        for (Factor factor : factors) {
            //将factor取导
            Poly tempPoly = new Poly();
            tempPoly = Poly.mulPoly(tempPoly, factor.toDiffPoly(ch));
            for (Factor temp : factors) {
                //判断temp和factor不是同一片地址空间
                //可能需要优化
                if (!(temp.equals(factor))) {
                    tempPoly = Poly.mulPoly(tempPoly, temp.toPoly());
                    ;
                }
            }
            //上面便利了其它项
            p.addPoly(tempPoly);
        }
        return p;
    }
}
