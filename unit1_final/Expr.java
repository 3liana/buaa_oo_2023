
import java.util.ArrayList;

public class Expr {
    private ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Poly toPoly() {
        Poly p = new Poly();
        for (Term term : terms) {
            if (term.getSign() == -1) {
                p.subPoly(term.toPoly());
            } else {
                p.addPoly(term.toPoly());
            }
        }
        return p;
    }

    public Poly toDiffPoly(char ch) {
        Poly p = new Poly();
        for (Term term : terms) {
            if (term.getSign() == -1) {
                p.subPoly(term.toDiffPoly(ch));
            } else {
                p.addPoly(term.toDiffPoly(ch));
            }
        }
        return p;
    }
}
