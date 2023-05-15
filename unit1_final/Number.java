import java.math.BigInteger;

public class Number extends Factor {
    private BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    public Mono toMono() {
        Mono m = new Mono(0, 0, 0, String.valueOf(this.num));
        return m;
    }

    public Poly toPoly() {
        Poly p = new Poly();
        p.addMono(this.toMono());
        return p;
    }

    public Poly toDiffPoly(char ch) {
        Mono m = new Mono();
        m.setCoe(BigInteger.ZERO);
        Poly p = new Poly();
        p.addMono(m);
        return p;
    }

}
