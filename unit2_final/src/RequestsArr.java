import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class RequestsArr {
    private ArrayList<MyPersonRequest> requestsIn;
    private ArrayList<MyPersonRequest> requestsNotIn;
    private boolean endTag;
    private boolean isDispatching;

    public RequestsArr() {
        this.endTag = false;
        this.requestsIn = new ArrayList<MyPersonRequest>();
        this.requestsNotIn = new ArrayList<MyPersonRequest>();
        this.isDispatching = false;
    }

    public synchronized boolean inLastRide() {
        notifyAll();
        for (MyPersonRequest request : requestsIn) {
            if (request.continueRequest()) {
                return false;
            }
        }
        for (MyPersonRequest request : requestsNotIn) {
            if (request.continueRequest()) {
                return false;
            }
        }
        if (isDispatching == true) {
            return false;
        }
        return true;
    }

    public synchronized ArrayList<MyPersonRequest> getOut(int floor) {
        ArrayList<MyPersonRequest> temp = new ArrayList<MyPersonRequest>();
        ArrayList<MyPersonRequest> toRemove = new ArrayList<MyPersonRequest>();
        for (MyPersonRequest ele : requestsIn) {
            if (ele.getToFloor() == floor) {
                temp.add(ele);
                //requestsIn.remove(ele);
                toRemove.add(ele);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            requestsIn.remove(toRemove.get(i));
            if (toRemove.get(i).continueRequest()) {
                //如果还有下一站
                //System.out.println("next stop");
                /*toRemove.get(i).addIndex();
                Dispatcher.dispatch(toRemove.get(i));*/
                setDispatchTag(true);
                //代表有的出的请求有下一程
            }
        }
        notifyAll();
        return temp;
    }

    private boolean checkDeirection(int fromFLr, int toFlr, boolean up) {
        if (toFlr > fromFLr && up) {
            return true;
        }
        if (toFlr < fromFLr && !up) {
            return true;
        }
        return false;
    }

    public synchronized ArrayList<MyPersonRequest> getIn(int floor, boolean up, int maxPeople) {
        ArrayList<MyPersonRequest> temp = new ArrayList<MyPersonRequest>();
        ArrayList<MyPersonRequest> toRemove = new ArrayList<MyPersonRequest>();
        for (int i = 0; i < requestsNotIn.size(); i++) {
            MyPersonRequest ele = requestsNotIn.get(i);
            if (ele.getFromFloor() == floor &&
                    checkDeirection(ele.getFromFloor(), ele.getToFloor(), up)) {
                if (requestsIn.size() >= maxPeople) {
                    continue;
                }
                temp.add(ele);
                //requestsNotIn.remove(ele);
                toRemove.add(ele);
                requestsIn.add(ele);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            requestsNotIn.remove(toRemove.get(i));
        }
        notifyAll();
        return temp;
    }

    public synchronized void assignRequest(MyPersonRequest request) {
        notifyAll();
        this.requestsNotIn.add(request);
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        if (this.requestsIn.isEmpty() && this.requestsNotIn.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isPeopleIn() {
        notifyAll();
        if (this.requestsIn.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public synchronized void wake() {
        notifyAll();
    }

    public synchronized ArrayList<Integer> isPeopleExp() {
        notifyAll();
        ArrayList<Integer> arr = new ArrayList<Integer>();
        //返回请求队列中不在电梯的人的fromFlr数组
        for (MyPersonRequest elm : requestsNotIn) {
            arr.add(elm.getFromFloor());
        }
        return arr;
    }

    public synchronized void setEndTag(boolean endTag) {
        notifyAll();
        this.endTag = endTag;
    }

    public synchronized boolean getEndTag() {
        notifyAll();
        return endTag;
    }

    public synchronized int getNotInNum() {
        notifyAll();
        return this.requestsNotIn.size();
    }

    public synchronized ArrayList<MyPersonRequest> returnNotIn() {
        //必须要重新规划路线
        ArrayList<MyPersonRequest> arr = new ArrayList<MyPersonRequest>();
        for (MyPersonRequest elm : this.requestsNotIn) {
            PersonRequest people = new PersonRequest(elm.getFromFloor(),
                    elm.getRealDst(), elm.getPersonID());
            MyPersonRequest myPeople = new MyPersonRequest(people);
            //已经重新规划了路线
            arr.add(myPeople);
        }
        notifyAll();
        return arr;
    }

    public synchronized ArrayList<MyPersonRequest> returnIn(int floor) {
        //取出来并重设了初始楼层
        ArrayList<MyPersonRequest> arr = new ArrayList<MyPersonRequest>();
        for (MyPersonRequest elm : this.requestsIn) {
            PersonRequest people = new PersonRequest(floor, elm.getRealDst(), elm.getPersonID());
            MyPersonRequest myPeople = new MyPersonRequest(people);
            //已经重新规划了路线
            arr.add(myPeople);
        }
        notifyAll();
        return arr;
    }

    public void setDispatchTag(boolean tag) {
        isDispatching = tag;
    }

    public boolean getDispatchTag() {
        return isDispatching;
    }
}
