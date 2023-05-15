import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TimableOutput.initStartTimestamp();
        MyInt maintainNum = new MyInt(0);
        MyInt[] serviceNum = new MyInt[12];
        MyInt[] pickNum = new MyInt[12];
        //初始化MyInt数组
        for (int i = 0; i < 12; i++) {
            serviceNum[i] = new MyInt();
            pickNum[i] = new MyInt();
        }
        ArrayList<RequestsArr> allRequestArr = new ArrayList<>();
        ArrayList<ElevatorThread> allElevator = new ArrayList<>();
        WaitingRequests waitingRequests = new WaitingRequests(maintainNum);
        RealInputThread riThread = new RealInputThread(waitingRequests);
        riThread.start();
        //电梯开启
        for (int i = 0; i < 6; i++) {
            allRequestArr.add(new RequestsArr());
        }
        for (int i = 0; i < 6; i++) {
            allElevator.add(new ElevatorThread(i + 1,
                    allRequestArr.get(i), waitingRequests,
                    maintainNum, serviceNum, pickNum));

        }
        for (int i = 0; i < 6; i++) {
            allElevator.get(i).start();
        }
        Dispatcher.setDispatcher(allRequestArr, allElevator);
        new InputThread(allRequestArr, allElevator, waitingRequests,
                maintainNum, serviceNum, pickNum).start();

    }
}