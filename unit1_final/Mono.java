import java.math.BigInteger;
import java.util.HashMap;

public class Mono {
    private BigInteger coe;//正负
    private int indexOfX;
    private int indexOfY;
    private int indexOfZ;

    private HashMap<Factor, Integer> sinMap;
    private HashMap<Factor, Integer> cosMap;

    public BigInteger getCoe() {
        return this.coe;
    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;
    }

    public int getIndexOfX() {
        return this.indexOfX;
    }

    public void setIndexOfX(int indexOfX) {
        this.indexOfX = indexOfX;
    }

    public int getIndexOfY() {
        return this.indexOfY;
    }

    public void setIndexOfY(int indexOfY) {
        this.indexOfY = indexOfY;
    }

    public int getIndexOfZ() {
        return this.indexOfZ;
    }

    public void setIndexOfZ(int indexOfZ) {
        this.indexOfZ = indexOfZ;
    }

    public Mono() {
        this.indexOfX = 0;
        this.indexOfY = 0;
        this.indexOfZ = 0;
        this.coe = BigInteger.valueOf(1);
        this.sinMap = new HashMap<>();
        this.cosMap = new HashMap<>();
    }

    public Mono(int indexOfX, int indexOfY, int indexOfZ, String coe) {
        this.coe = new BigInteger(coe);
        this.indexOfX = indexOfX;
        this.indexOfY = indexOfY;
        this.indexOfZ = indexOfZ;
        this.sinMap = new HashMap<>();
        this.cosMap = new HashMap<>();
    }

    public Mono(int indexOfX,
                int indexOfY,
                int indexOfZ,
                String coe,
                HashMap<Factor, Integer> sinMap,
                HashMap<Factor, Integer> cosMap) {
        this.coe = new BigInteger(coe);
        this.indexOfX = indexOfX;
        this.indexOfY = indexOfY;
        this.indexOfZ = indexOfZ;
        this.sinMap = sinMap;
        this.cosMap = cosMap;
    }

    private String toPower() {
        StringBuilder sb = new StringBuilder();
        if (this.indexOfX == 0 && this.indexOfY == 0 && this.indexOfZ == 0) {
            return "1";
        }
        int tag = 0;
        if (this.indexOfX == 1) {
            sb.append("x");
            tag = 1;
        } else if (this.indexOfX > 1) {
            sb.append("x**");
            sb.append(indexOfX);
            tag = 1;
        }

        if (this.indexOfY == 1) {
            if (tag == 1) {
                sb.append("*");
            }
            sb.append("y");
            tag = 1;
        } else if (this.indexOfY > 1) {
            if (tag == 1) {
                sb.append("*");
            }
            sb.append("y**");
            sb.append(indexOfY);
            tag = 1;
        }

        if (this.indexOfZ == 1) {
            if (tag == 1) {
                sb.append("*");
            }
            sb.append("z");
        } else if (this.indexOfZ > 1) {
            if (tag == 1) {
                sb.append("*");
            }
            sb.append("z**");
            sb.append(indexOfZ);
        }
        return sb.toString();
    }

    private String toCosSin() {
        StringBuilder sb = new StringBuilder();
        this.sinMap.forEach((factor, value) -> {
            if (value != 0) {
                sb.append("*");
                sb.append("sin(");
                sb.append(factor.toString());
                sb.append(")");
                if (value > 1) {
                    sb.append("**");
                    sb.append(value);
                }
            }
        });
        this.cosMap.forEach((factor, value) -> {
            if (value != 0) {
                sb.append("*");
                sb.append("cos(");
                sb.append(factor.toString());
                sb.append(")");
                if (value > 1) {
                    sb.append("**");
                    sb.append(value);
                }
            }
        });
        /*if(sb.toString().length() > 1){
            return sb.toString().substring(1);
        }else{
            return "";
        }*/
        return sb.toString();
    }

    private static HashMap<Factor, Integer> mulMap(HashMap<Factor, Integer> first,
                                                   HashMap<Factor, Integer> second) {
        HashMap<Factor, Integer> t = (HashMap<Factor, Integer>) second.clone();
        HashMap<Factor, Integer> sinMap = (HashMap<Factor, Integer>) first.clone();
        for (Factor i : sinMap.keySet()) {
            //依次加sinMap中的每一项
            int tag = 0;
            for (Factor j : t.keySet()) {
                if (i.toString().equals(j.toString())) {
                    //如果找到了就改t中那一项的系数
                    //Poly的相等可以优化
                    //this.sinMap.get(j) += sinMap.get(i);
                    t.replace(j, t.get(j) + sinMap.get(i));
                    tag = 1;
                    break;
                }
            }
            if (tag == 0) {
                //如果每找到就在t中增加一项
                t.put(i, sinMap.get(i));
            }
        }
        return t;
    }

    public Mono negate() {
        int indexOfX = this.indexOfX;
        int indexOfY = this.indexOfY;
        String coe = String.valueOf(this.coe.negate());
        HashMap<Factor, Integer> sinMap = this.sinMap;
        HashMap<Factor, Integer> cosMap = this.cosMap;
        //Mono m = new Mono(this.indexOfX, this.indexOfY, this.indexOfZ,
        // String.valueOf(this.coe.negate()), this.sinMap, this.cosMap);
        Mono m = new Mono(indexOfX, indexOfY, indexOfZ, coe, sinMap, cosMap);
        return m;
    }

    public Mono mulMono(Mono mono) {
        if (this.coe.equals(BigInteger.ZERO) | mono.coe.equals(BigInteger.ZERO)) {
            return new Mono(0, 0, 0, "0");
        } else {
            return new Mono(this.indexOfX + mono.getIndexOfX(),
                    this.indexOfY + mono.getIndexOfY(),
                    this.indexOfZ + mono.getIndexOfZ(),
                    String.valueOf(this.coe.multiply(mono.getCoe())),
                    mulMap(this.sinMap, mono.sinMap),
                    mulMap(this.cosMap, mono.cosMap));
        }
    }

    public boolean equalPowered(Mono m) {
        if (this.indexOfX == m.indexOfX &
                this.indexOfY == m.indexOfY &
                this.indexOfZ == m.indexOfZ &
                this.sinMap.toString().equals(m.sinMap.toString()) &
                this.cosMap.toString().equals(m.cosMap.toString())) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        //cos sin前面无论如何都有num*所以在Judge中判断括号的时候return false代表需要加括号
        StringBuilder sb = new StringBuilder();
        if (this.coe.equals(BigInteger.ZERO)) {
            //系数为0
            return "0";
        }
        if (this.coe.equals(BigInteger.ONE)) {
            //系数为1
            return this.toPower() + this.toCosSin();
        }
        if (indexOfX == 0 & indexOfY == 0 & indexOfZ == 0) {
            //指数全为0
            sb.append(coe);//*todo*
            //可以加入判断coe为1不加系数
            if (!(this.toCosSin().equals(""))) {
                sb.append(this.toCosSin());
                return sb.toString();
            } else {
                return sb.toString();
            }
        }
        sb.append(coe);
        sb.append("*");
        sb.append(this.toPower());
        sb.append(this.toCosSin());
        return sb.toString();
    }
}
