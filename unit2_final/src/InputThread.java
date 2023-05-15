import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class InputThread extends Thread {
    private final ArrayList<RequestsArr> allRequestArr;
    private final ArrayList<ElevatorThread> allElevator;
    private final WaitingRequests waitingRequests;
    private MyInt num;
    private MyInt[] serviceNum;
    private MyInt[] pickNum;

    private boolean allInLastRide() {
        for (RequestsArr i : allRequestArr) {
            if (!(i.inLastRide())) {
                return false;
            }
        }
        //System.out.println("true");
        return true;
        //true的意思是所有队列里的所有请求都在自己的最后一程
    }

    public InputThread(ArrayList<RequestsArr> allRequestArr, ArrayList<ElevatorThread> allElevator,
                       WaitingRequests waitingRequests, MyInt num, MyInt[] serviceNum,
                       MyInt[] pickNum) {
        this.allRequestArr = allRequestArr;
        this.allElevator = allElevator;
        this.waitingRequests = waitingRequests;
        this.num = num;
        this.serviceNum = serviceNum;
        this.pickNum = pickNum;
    }

    //读入然后分配给六个RequestArr
    public void run() {
        while (true) {
            //TimableOutput.println("debug1");
            if (waitingRequests.endSign() && allInLastRide()) {
                for (RequestsArr requestsArr : allRequestArr) {
                    requestsArr.setEndTag(true);
                }
                break;
            }
            Request request = waitingRequests.get();
            if (request == null) {
                synchronized (waitingRequests) {
                    try {
                        waitingRequests.wait();
                        //等待waitingRequest有元素之后再被唤醒
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //从等待序列里取出一个最先加入的请求（但是maintain和add比人的请求优先）
            //有可能是null哦
            if (request instanceof PersonRequest) {

                PersonRequest temp = (PersonRequest) request;
                //**************************************
                //以下是把请求分配给电梯队列的过程
                MyPersonRequest personRequest = new MyPersonRequest(temp);
                Dispatcher.dispatch(personRequest);
                //***************************************
            } else if (request instanceof MyPersonRequest) {
                //会直接得到MyPersonRequest只有maintain后重新分配的情况，所以可以直接处理
                MyPersonRequest temp = (MyPersonRequest) request;
                Dispatcher.dispatch(temp);
            } else if (request instanceof ElevatorRequest) {
                ElevatorRequest temp = (ElevatorRequest) request;
                RequestsArr tempRequests = new RequestsArr();
                ElevatorThread tempEleThread = new ElevatorThread(temp.getElevatorId(),
                        tempRequests, temp.getCapacity(),
                        temp.getFloor(),
                        (int) (temp.getSpeed() * 1000),
                        waitingRequests, num, temp.getAccess(), serviceNum, pickNum);
                allRequestArr.add(tempRequests);
                allElevator.add(tempEleThread);
                tempEleThread.start();
            } else if (request instanceof MaintainRequest) {
                num.addOne();
                MaintainRequest temp = (MaintainRequest) request;
                int id = temp.getElevatorId();
                //找到该id的电梯并setMaintainTag
                for (int i = 0; i < allElevator.size(); i++) {
                    if (allElevator.get(i).getEleId() == id) {
                        ElevatorThread ele = allElevator.get(i);
                        ele.setMaintainTag(true);
                        allRequestArr.get(i).wake();
                        //如果requestArr在等待状态就会被唤醒去执行maintain
                        break;
                    }
                }
            }
            //TimableOutput.println("debug4");
        }
        /*//todo debug
        System.out.println("Input Thread end");*/
    }

}
