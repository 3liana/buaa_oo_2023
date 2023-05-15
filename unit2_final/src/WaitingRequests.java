import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;

public class WaitingRequests {
    private final ArrayList<Request> arr;
    private boolean endTag;
    private MyInt num;

    public WaitingRequests(MyInt num) {
        arr = new ArrayList<>();
        endTag = false;
        this.num = num;
    }

    public synchronized void setEndTag(boolean tag) {
        notifyAll();
        endTag = tag;
    }

    public synchronized boolean endSign() {
        notifyAll();
        return endTag && arr.isEmpty() && num.equalZero();
    }

    public synchronized void assign(Request request) {
        notifyAll();
        if (request instanceof MaintainRequest || request instanceof ElevatorRequest) {
            arr.add(0, request);
            //maintain请求要插到最前面
        } else {
            arr.add(request);
        }

    }

    public synchronized Request get() {
        notifyAll();
        if (arr.isEmpty()) {
            return null;
        }
        Request request = arr.get(0);
        arr.remove(request);
        return request;
    }

    public synchronized void wake() {
        notifyAll();
    }
}
