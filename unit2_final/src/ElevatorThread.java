import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;

public class ElevatorThread extends Thread {
    private final int id;
    private int floor;
    private boolean up;
    private final RequestsArr requestsArray;
    private final int maxPeople;
    private final int moveTime;
    private boolean maintainTag;
    private final WaitingRequests waitingRequests;
    private final int access;
    private MyInt num;
    private MyInt[] serviceNum;
    private MyInt[] pickNum;

    public boolean getDirection() {
        return up;
    }

    public ElevatorThread(int id, RequestsArr requestsArray,
                          WaitingRequests waitingRequests,
                          MyInt num, MyInt[] serviceNum, MyInt[] pickNum) {
        this.id = id;
        this.floor = 1;
        this.up = true;
        this.requestsArray = requestsArray;
        this.maxPeople = 6;
        this.moveTime = 400;
        this.maintainTag = false;
        this.waitingRequests = waitingRequests;
        this.num = num;
        this.access = 0x7FF;
        this.serviceNum = serviceNum;
        this.pickNum = pickNum;
    }

    public ElevatorThread(int id, RequestsArr requestsArray, int maxPeople,
                          int floor, int moveTime, WaitingRequests waitingRequests,
                          MyInt num, int access, MyInt[] serviceNum, MyInt[] pickNum) {
        this.id = id;
        this.floor = floor;
        this.up = true;
        this.requestsArray = requestsArray;
        this.maxPeople = maxPeople;
        this.moveTime = moveTime;
        this.maintainTag = false;
        this.waitingRequests = waitingRequests;
        this.num = num;
        this.access = access;
        this.serviceNum = serviceNum;
        this.pickNum = pickNum;
    }

    public void run() {
        while (true) {
            //开门关门
            try {
                checkOpen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //是否有人
            checkPeople();
            //结束线程
            //是否该维护
            if (this.maintainTag) {
                maintain();
                break;
            }
            if ((requestsArray.isEmpty() && requestsArray.getEndTag())) {
                if (this.maintainTag) {
                    maintain();
                    break;
                }
                break;
            }
        }
        //System.out.println("elevator Thread " + this.id + " end");
    }

    private void checkOpen() throws InterruptedException {
        ArrayList<MyPersonRequest> out = requestsArray.getOut(floor);
        ArrayList<MyPersonRequest> in = requestsArray.getIn(floor, up, maxPeople);
        if (in.size() > 0 || out.size() > 0) {
            //确实出了人再wake重新判断inputThread的结束
            //todo 其实应该确实出人并且不是最后一程再wake
            waitingRequests.wake();
            if (serviceNum[floor].getNum() == 4) {
                synchronized (serviceNum[floor]) {
                    serviceNum[floor].wait();
                }
            }
            if (out.isEmpty()) {
                System.out.println(pickNum[floor].getNum() + "floors:" + floor);
                if (pickNum[floor].getNum() == 2) {
                    synchronized (pickNum[floor]) {
                        pickNum[floor].wait();
                    }
                }
            }
            open(out.isEmpty());
            //out.isEmpty()就是只接人
            prinInOut(out, "OUT");
            prinInOut(in, "IN");
            close(out.isEmpty());
            if (requestsArray.getDispatchTag()) {
                for (MyPersonRequest elm : out) {
                    if (elm.continueRequest()) {
                        elm.addIndex();
                        Dispatcher.dispatch(elm);
                    }
                }
                requestsArray.setDispatchTag(false);
            }

        }
    }

    private void checkPeople() {
        if (requestsArray.isPeopleIn()) {
            //电梯有人
            move();
        } else {
            //电梯没有人，检查请求队列
            ArrayList<Integer> temp = requestsArray.isPeopleExp();
            //请求队列为空
            if (temp.size() == 0) {
                //todo 这里比较重要需要好好处理
                //电梯没人且请求队列为空
                if (requestsArray.getEndTag() || maintainTag) {
                    return;
                }
                synchronized (requestsArray) {
                    try {
                        requestsArray.wait();
                        //等待
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                //请求队列不为空，检查请求方向上
                boolean checkResult = checkDirection(temp, floor, up);
                if (checkResult) {
                    //有请求与方向一致
                    move();
                } else {
                    //无请求与方向一致
                    up = !up;
                    try {
                        checkOpen();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    checkPeople();
                }
            }
        }
    }

    private void move() {
        int tag = 0;
        if (up && floor < 11) {
            floor++;
            tag = 1;
        }
        if (!up && floor > 1) {
            floor--;
            tag = 1;
        }
        if (tag == 1) {
            try {
                sleep(moveTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            TimableOutput.println("ARRIVE-" + floor + "-" + id);
        }
    }

    private void maintain() {
        ArrayList<MyPersonRequest> tempNotIn = requestsArray.returnNotIn();
        //tempNotIn
        for (MyPersonRequest elm : tempNotIn) {
            waitingRequests.assign(elm);
        }
        ArrayList<MyPersonRequest> tempIn = requestsArray.returnIn(floor);
        //tempIn
        for (MyPersonRequest elm : tempIn) {
            if (floor != elm.getToFloor()) {
                waitingRequests.assign(elm);
            }
        }
        if (!(tempIn.isEmpty())) {
            open(false);
            prinInOut(tempIn, "OUT");
            //只有已经进来的人需要再出去
            close(false);
        }
        TimableOutput.println("MAINTAIN_ABLE-" + id);
        num.subOne();
        waitingRequests.wake();
    }

    private void prinInOut(ArrayList<MyPersonRequest> arr, String inout) {
        for (MyPersonRequest elm : arr) {
            TimableOutput.println(inout + "-" + elm.getPersonID() + "-" + floor + "-" + id);
        }
    }

    private void open(boolean noOut) {
        TimableOutput.println("OPEN" + "-" + floor + "-" + id);
        serviceNum[floor].addOne();
        if (noOut) {
            pickNum[floor].addOne();
        }
        try {
            sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void close(boolean noOut) {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TimableOutput.println("CLOSE" + "-" + floor + "-" + id);
        serviceNum[floor].subOne();
        if (noOut) {
            pickNum[floor].subOne();
        }
    }

    public boolean canPassBy(int fromFlr) {
        if (fromFlr > floor && up) {
            return true;
        }
        if (fromFlr < floor && !up) {
            return true;
        }
        return false;
    }

    private boolean checkDirection(ArrayList<Integer> arr, int floor, boolean up) {
        if (up) {
            //向上
            for (Integer i : arr) {
                if (i > floor) {
                    return true;
                }
            }
        } else {
            //向下
            for (Integer i : arr) {
                if (i < floor) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getEleId() {
        return id;
    }

    public void setMaintainTag(boolean tag) {
        this.maintainTag = tag;
    }

    public boolean getMaintainTag() {
        return maintainTag;
    }

    public int getAccess() {
        return access;
    }
}
