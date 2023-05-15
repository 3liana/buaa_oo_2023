import java.math.BigInteger;
import java.util.HashMap;

public class CosFactor extends Factor {
    private int exp;
    private Factor factor;

    public CosFactor(Factor factor, int exp) {
        this.factor = factor;
        this.exp = exp;
    }

    public Mono toMono() {
        HashMap<Factor, Integer> sinMap = new HashMap<>();
        HashMap<Factor, Integer> cosMap = new HashMap<>();
        cosMap.put(this.factor, this.exp);
        return new Mono(0, 0, 0, "1", sinMap, cosMap);
    }

    public Poly toPoly() {
        Poly p = new Poly();
        p.addMono(this.toMono());
        return p;
    }

    public Poly toDiffPoly(char ch) {
        if (this.exp == 0) {
            Mono temp = new Mono();
            temp.setCoe(BigInteger.ZERO);
            Poly tempPoly = new Poly();
            tempPoly.addMono(temp);
            return tempPoly;
        }
        Mono m1 = new Mono();
        m1.setCoe(new BigInteger(String.valueOf(exp)));
        Poly p1 = new Poly();
        p1.addMono(m1);//含系数的Poly
        HashMap<Factor, Integer> sinMap = new HashMap<>();
        HashMap<Factor, Integer> cosMap = new HashMap<>();
        cosMap.put(this.factor, this.exp - 1);
        sinMap.put(this.factor, 1);
        Mono m2 = new Mono(0, 0, 0, "-1", sinMap, cosMap);
        Poly p2 = new Poly();
        p2.addMono(m2);//含三角函数项的Poly
        Poly p3 = this.factor.toDiffPoly(ch);
        Poly ans = Poly.mulPoly(p1, p2);
        ans = Poly.mulPoly(ans, p3);
        return ans;
    }

    public String toString() {
        if (Judge.judgeStringOfFactor(this.toPoly().toString())) {
            return this.toPoly().toString();
        } else {
            return "(" + this.toPoly().toString() + ")";//有问题
        }
        //return this.toPoly().toString();
    }
}
