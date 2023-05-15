
import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;//每个parser有自己的一套记录了位置找东西的lexer

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        int num = 1;
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            num = -1;
            lexer.next();
        }
        expr.addTerm(parseTerm(num));

        while (lexer.peek().equals("+") | lexer.peek().equals("-")) {
            int sign = 1;
            if (lexer.peek().equals("-")) {
                sign = -1;
            }
            lexer.next();
            expr.addTerm(parseTerm(sign));
        }
        return expr;
    }

    public Term parseTerm(int sign) {
        Term term = new Term();
        term.setSign(sign);
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();//将curToken变为下一个parse时要分析的第一个符号
            term.addFactor(parseFactor());
        }
        return term;
    }

    private int testIndex() {
        int index = 1;
        if (lexer.peek().charAt(0) == '^') {
            //curToken是幂次方
            index = 0;
            for (int i = 1; i < lexer.peek().length(); i++) {
                if (lexer.peek().charAt(i) >= '0') {
                    //index = lexer.peek().charAt(1) - '0';
                    index = index * 10 + lexer.peek().charAt(i) - '0';
                }
            }
            lexer.next();//如果是幂次方就需要读下一个符号，否则就不需要
        }
        return index;
    }

    private Factor parseExprFactor() {
        //如果需要返回的是ExprFactor
        lexer.next();
        Expr base = parseExpr();
        lexer.next();
        //what in testIndex
        return new ExprFactor(base, this.testIndex());
    }

    private Factor parseSinFactor() {
        lexer.next();
        Factor f = parseFactor();
        lexer.next();
        return new SinFactor(f, this.testIndex());
    }

    private Factor parseCosFactor() {
        lexer.next();
        Factor f = parseFactor();
        lexer.next();
        return new CosFactor(f, this.testIndex());
    }

    private Factor parseVar() {
        char ch = lexer.peek().charAt(0);
        if (lexer.peek().length() == 1) {
            lexer.next();
            return new Var(ch, 1);
        }
        int num = 0;
        for (int i = 2; i < lexer.peek().length(); i++) {
            num *= 10;
            num = num + lexer.peek().charAt(i) - '0';//对char的处理
        }
        lexer.next();
        return new Var(ch, num);
    }

    public Factor parseFactor() {
        //返回一个符合factor接口的东西
        if (lexer.peek().equals("(")) {
            return parseExprFactor();
        } else if (lexer.peek().charAt(0) == 's') {
            return parseSinFactor();
        } else if (lexer.peek().charAt(0) == 'c') {
            return parseCosFactor();
        } else if (lexer.peek().charAt(0) == 'x' |
                lexer.peek().charAt(0) == 'y' |
                lexer.peek().charAt(0) == 'z') {
            //如果需要返回的是变量Var
            return parseVar();
        } else if (lexer.peek().charAt(0) == 'g' ||
                lexer.peek().charAt(0) == 'h' ||
                lexer.peek().charAt(0) == 'f') {
            ArrayList<Factor> actualParas = new ArrayList<Factor>();
            String str = lexer.peek().substring(2, lexer.peek().length() - 1);
            str = Change.repComaInFunction(str);
            for (String s : str.split(",")) {
                String temp = Change.rep(s, "#", ",");
                Lexer l = new Lexer(temp);
                Parser p = new Parser(l);
                Factor f = p.parseFactor();
                actualParas.add(f);
            }
            String name = String.valueOf(lexer.peek().charAt(0));
            lexer.next();
            return new FuncFactor(name, actualParas);
        } else if (lexer.peek().charAt(0) == 'd') {
            char ch = lexer.peek().charAt(1);
            lexer.next();
            Expr e = parseExpr();
            //之后curToken会变为）
            lexer.next();
            //跳过)
            return new DeFactor(e, ch);
        } else {
            //如果需要返回的是Number
            BigInteger num;
            if (lexer.peek().equals("-")) {
                lexer.next();
                num = new BigInteger("-" + lexer.peek());
            } else if (lexer.peek().equals("+")) {
                lexer.next();
                num = new BigInteger(lexer.peek());
            } else {
                num = new BigInteger(lexer.peek());//Integer.parseInt(lexer.peek());
            }
            lexer.next();
            return new Number(num);
        }
    }
}
