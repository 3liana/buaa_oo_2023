import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class RealInputThread extends Thread {
    //此线程主要负责增加waitingRequest
    private final WaitingRequests waitingRequests;

    public RealInputThread(WaitingRequests waitingRequests) {
        this.waitingRequests = waitingRequests;
    }

    public void run() {
        ElevatorInput eleInput = new ElevatorInput(System.in);
        while (true) {
            //*********************************************************
            //******这一部分是处理从输入中得到请求***************************
            Request in = eleInput.nextRequest();
            //可能会导致等待
            if (in == null) {
                waitingRequests.setEndTag(true);
                //waitingRequest不会再有新增
                break;
            } else {
                //get a request
                waitingRequests.assign(in);
                //从输入得到一个请求，加入等待序列
            }
            //******这一部分是处理从输入中得到请求***************************
            //*********************************************************
        }
        try {
            eleInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("realInputThread end");
    }
}
