import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class MyPersonRequest extends Request {
    private Routes routes = new Routes();
    private int index;
    private int personID;
    private String string;

    public MyPersonRequest(PersonRequest request) {
        ;
        //****************************************
        //需要确定换乘队列
        this.routes = Dispatcher.decideRoutes(request.getFromFloor(), request.getToFloor());
        //System.out.println("set routes for" + request);
        //System.out.println(request+" get routes "+routes);
        //****************************************
        index = 0;
        personID = request.getPersonId();
        string = request.toString();
    }

    public int getRealDst() {
        return this.routes.getFloors().get(this.routes.getFloors().size() - 1);
    }

    public int getFromFloor() {
        //短程的fromFloor
        return routes.getFloors().get(index);
    }

    public int getToFloor() {
        //短程的toFloor
        return routes.getFloors().get(index + 1);
    }

    public int getEle() {
        return routes.getEles().get(index);
    }

    public int getPersonID() {
        return personID;
    }

    public void addIndex() {
        this.index++;
    }

    public boolean continueRequest() {
        /* System.out.println(index);
        System.out.println(routes.getFloors().size());*/
        return !(index + 2 == routes.getFloors().size());
    }

    public String toString() {
        return string;
    }
}
