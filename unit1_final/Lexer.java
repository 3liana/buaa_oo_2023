public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        //get 45 2 44
        //从当前位置起获得一个数字（可能是多位）
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;//这个地方让pos++了（重要）
        }

        return sb.toString();
    }

    private String getVar() {
        // get x x^5 y y^5
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && ("+-*()".indexOf(input.charAt(pos)) == -1)) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    private String getFuncFactor() {
        StringBuilder sb = new StringBuilder();
        int leftBracketNum = 1;
        int rightBracketNum = 0;
        while (pos < input.length() && rightBracketNum < leftBracketNum) {
            char ch = input.charAt(pos);
            if (ch == '(') {
                leftBracketNum++;
            }
            if (ch == ')') {
                rightBracketNum++;
            }
            sb.append(ch);
            pos++;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = this.getNumber();


        } else if ("()*+-".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);//curToken为（）*+-之中的一个


        } else if (c == '^') {
            pos++;
            String num = this.getNumber();
            curToken = "^" + num;//cutToken为一串幂


        } else if (c == 's') {
            pos = pos + 4;
            curToken = "sin(";
        } else if (c == 'c') {
            pos = pos + 4;
            curToken = "cos(";
        } else if (c == 'd') {
            pos++;
            char temp = input.charAt(pos);
            pos += 2;
            curToken = "d" + String.valueOf(temp) + "(";
        } else if (c == 'g' || c == 'h' || c == 'f') {
            pos++;//c
            pos++;//"("
            curToken = c + "(" + this.getFuncFactor();
        } else {
            curToken = this.getVar();//cutToken为单个变量或变量的几次方
        }
    }

    public String peek() {
        return this.curToken;
    }
}
