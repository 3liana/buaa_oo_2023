import java.util.ArrayList;

public class Poly {
    private ArrayList<Mono> monos;

    public Poly() {
        this.monos = new ArrayList<>();
    }

    public void addMono(Mono mono) {
        //Poly + Mono
        for (Mono i : this.monos) {
            if (i.equalPowered(mono)) {
                i.setCoe(i.getCoe().add(mono.getCoe()));
                //i.coe += mono.coe;
                return;
            }
        }
        this.monos.add(mono);
    }

    public void addPoly(Poly poly) {
        for (Mono i : poly.monos) {
            this.addMono(i);
        }
    }

    public void subPoly(Poly poly) {
        for (Mono i : poly.monos) {
            this.addMono(i.negate());
        }
    }

    public static Poly mulPoly(Poly x, Poly y) {
        Poly p = new Poly();
        if (x.monos.isEmpty()) {
            return y;
        }
        if (y.monos.isEmpty()) {
            return x;
        }
        for (Mono i : x.monos) {
            for (Mono j : y.monos) {
                p.addMono(i.mulMono(j));
            }
        }
        return p;
    }

    public static Poly powPoly(Poly x, int y) {
        if (y == 0) {
            Poly p = new Poly();
            Mono m = new Mono(0, 0, 0, "1");
            p.addMono(m);
            return p;
        }
        Poly p = x;
        //Poly temp = x;
        for (int i = 0; i < y - 1; i++) {
            p = mulPoly(x, p);
        }
        return p;
    }

    public String toString() {
        if (this.monos == null) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (Mono i : this.monos) {
            sb.append(i.toString());
            sb.append("+");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
