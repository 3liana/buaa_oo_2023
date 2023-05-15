import java.math.BigInteger;

public class Var extends Factor {
    private char ch;
    private int index;

    public Var(char ch, int index) {
        this.ch = ch;
        this.index = index;
    }

    public String toString() {
        if (this.index == 0) {
            return "1";
        }
        if (this.index == 1) {
            return String.valueOf(this.ch);
        }
        return String.valueOf(this.ch) + "^" + String.valueOf(this.index);
    }

    public Mono toMono() {
        Mono m = new Mono();
        //系数为1，其它都为0为空
        if (this.ch == 'x') {
            m.setIndexOfX(this.index);
            //m.x = this.index;//yz系数为0 x系数为index
        }
        if (this.ch == 'y') {
            m.setIndexOfY(this.index);
            //m.y = this.index;
        }
        if (this.ch == 'z') {
            m.setIndexOfZ(this.index);
            //m.z = this.index;
        }
        return m;
    }

    public Mono toDiffMono(char var) {
        Mono m = new Mono();
        if (this.ch != var) {
            //对不含的参数求导
            m.setCoe(BigInteger.ZERO);
        } else if (this.index == 0) {
            //指数为0 求导
            m.setCoe(BigInteger.ZERO);
        } else {
            m.setCoe(new BigInteger(String.valueOf(this.index)));
            int num = this.index - 1;
            if (var == 'x') {
                m.setIndexOfX(num);
            } else if (var == 'y') {
                m.setIndexOfY(num);
            } else if (var == 'z') {
                m.setIndexOfZ(num);
            }
        }
        return m;
    }

    public Poly toPoly() {
        Poly p = new Poly();
        p.addMono(this.toMono());
        return p;
    }

    public Poly toDiffPoly(char ch) {
        Poly p = new Poly();
        p.addMono(this.toDiffMono(ch));
        return p;
    }
}
