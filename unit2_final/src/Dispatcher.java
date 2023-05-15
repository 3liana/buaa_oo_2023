import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class Dispatcher {
    private static ArrayList<RequestsArr> allRequestArr;
    private static ArrayList<ElevatorThread> allElevator;
    private static int[] accessing = new int[100];
    //换乘100次肯定够了
    private static int findFlag = 0;

    public static void setDispatcher(ArrayList<RequestsArr> allRequestArr,
                                     ArrayList<ElevatorThread> allElevator) {
        //此函数用于初始化静态变量
        Dispatcher.allElevator = allElevator;
        Dispatcher.allRequestArr = allRequestArr;
    }

    public static Routes decideRoutes(int fromFlr, int toFlr) {
        //Routes routes = new Routes();
        //*************************************************
        //todo 待填充，确定一个请求的所乘电梯队列
        //根据电梯的access来决定走哪些路，这些路分别分配给哪个电梯
        //注意只分配给不maintain的电梯
        //routes.addFlr(fromFlr);
        /*int findEle = Dispatcher.decide(fromFlr, (toFlr - fromFlr > 0));
        routes.addFlr(toFlr);
        routes.addEle(findEle);*/
        return findBestRoutes(findRoutes(fromFlr, toFlr), fromFlr, toFlr);

        //以上是一个不换乘的routes
        //*************************************************
        //return routes;
    }

    private static boolean hasAccess(int access, int fromFlr, int toFlr) {
        int accessTo = access & (1 << (fromFlr - 1));
        int accessFrom = access & (1 << (toFlr - 1));
        return (accessTo != 0) && (accessFrom != 0);
    }

    public static void dispatch(MyPersonRequest personRequest) {
        int index = personRequest.getEle();
        if (allElevator.get(index).getMaintainTag()) {
            //如果静态确定路径的下一个电梯在进入之间被maintain了
            PersonRequest person = new PersonRequest(personRequest.getFromFloor(),
                    personRequest.getRealDst(), personRequest.getPersonID());
            MyPersonRequest myPerson = new MyPersonRequest(person);
            allRequestArr.get(myPerson.getEle()).assignRequest(personRequest);
        } else {
            allRequestArr.get(personRequest.getEle()).assignRequest(personRequest);
        }

    }

    public static ArrayList<Accessable> findRoutesByOneEle(int fromFlr, int toFlr) {
        ArrayList<Accessable> possibleRoutes = new ArrayList<>();
        for (int i = 0; i < allElevator.size(); i++) {
            if (allElevator.get(i).getMaintainTag()) {
                continue;
            }
            if (hasAccess(allElevator.get(i).getAccess(), fromFlr, toFlr)) {
                Accessable temp = new Accessable(i);
                possibleRoutes.add(temp);
            }
        }
        return possibleRoutes;
    }

    public static ArrayList<Accessable> findRoutesByTwoEle(int fromFlr, int toFlr) {
        ArrayList<Accessable> possibleRoutes = new ArrayList<>();
        for (int i = 0; i < allElevator.size(); i++) {
            if (allElevator.get(i).getMaintainTag()) {
                continue;
            }
            for (int j = 0; j < allElevator.size(); j++) {
                if (allElevator.get(j).getMaintainTag()) {
                    continue;
                }
                if (hasAccess(allElevator.get(i).getAccess() | allElevator.get(j).getAccess(),
                        fromFlr, toFlr)) {
                    int intersect = allElevator.get(i).getAccess() & allElevator.get(j).getAccess();
                    if (intersect != 0) {
                        //不一定是i，j还是j，i呢

                        if (floorHasAccess(allElevator.get(i).getAccess(), fromFlr)) {
                            Accessable temp = new Accessable(i, j);
                            possibleRoutes.add(temp);
                        } else {
                            Accessable temp = new Accessable(j, i);
                            possibleRoutes.add(temp);
                        }
                    }
                }
            }
        }
        return possibleRoutes;
    }

    public static ArrayList<Accessable> findRoutesBy3(int fromFlr, int toFlr) {
        ArrayList<Accessable> possibleRoutes = new ArrayList<>();
        for (int i = 0; i < allElevator.size(); i++) {
            if (allElevator.get(i).getMaintainTag()) {
                continue;
            }
            for (int j = 0; j < allElevator.size(); j++) {
                if (allElevator.get(j).getMaintainTag()) {
                    continue;
                }
                for (int k = 0; k < allElevator.size(); k++) {
                    if (allElevator.get(k).getMaintainTag()) {
                        continue;
                    }
                    if (hasAccess(allElevator.get(i).getAccess() |
                                    allElevator.get(j).getAccess() |
                                    allElevator.get(k).getAccess(),
                            fromFlr, toFlr)) {
                        int intersect1 = allElevator.get(i).getAccess() &
                                allElevator.get(j).getAccess();
                        int intersect2 = allElevator.get(j).getAccess() &
                                allElevator.get(k).getAccess();
                        if (intersect1 != 0 && intersect2 != 0) {
                            Accessable temp = new Accessable(i, j, k);
                            possibleRoutes.add(temp);
                        }
                    }
                }

            }
        }
        return possibleRoutes;
    }

    public static void dfsFindRoutes(int fromFlr, int toFlr, int curNum, int num) {
        //num代表换乘几次
        //curNum代表正在寻找第几段路的电梯
        if (findFlag == 1) {
            return;
            //已经找到一次之后就不找了
        }
        if (curNum == num) {
            //找到
            /*for(int k = 0;k < num;k++){
                System.out.println(accessing[k]);
            }*/
            findFlag = 1;
            return;
        }
        //找第curNum段
        if (curNum == 0) {
            for (int i = 0; i < allElevator.size(); i++) {
                if (allElevator.get(i).getMaintainTag()) {
                    continue;
                }
                if (floorHasAccess(allElevator.get(i).getAccess(), fromFlr)) {
                    //第一段需要和可以经过初始楼层
                    accessing[curNum] = i;
                    dfsFindRoutes(fromFlr, toFlr, curNum + 1, num);
                }
                if (findFlag == 1) {
                    return;
                }
            }
        } else if (curNum == num - 1) {
            for (int i = 0; i < allElevator.size(); i++) {
                if (allElevator.get(i).getMaintainTag()) {
                    continue;
                }
                if (i == accessing[curNum - 1]) {
                    //连续两座一样的电梯是没有必要的
                    continue;
                }
                int intersectFlag = getEleAccess(i) & getEleAccess(accessing[curNum - 1]);
                if (floorHasAccess(allElevator.get(i).getAccess(), toFlr) && (intersectFlag != 0)) {
                    //最后一段需要可以经过终点楼层
                    accessing[curNum] = i;
                    dfsFindRoutes(fromFlr, toFlr, curNum + 1, num);
                }
                if (findFlag == 1) {
                    return;
                }
            }
        } else {
            for (int i = 0; i < allElevator.size(); i++) {
                if (allElevator.get(i).getMaintainTag()) {
                    continue;
                }
                if (i == accessing[curNum - 1]) {
                    //连续两座一样的电梯是没有必要的
                    continue;
                }
                int intersectFlag = getEleAccess(i) & getEleAccess(accessing[curNum - 1]);
                if (intersectFlag != 0) {
                    accessing[curNum] = i;
                    dfsFindRoutes(fromFlr, toFlr, curNum + 1, num);
                }
                if (findFlag == 1) {
                    return;
                }
            }
        }
    }

    public static ArrayList<Accessable> findRoutes(int fromFlr, int toFlr) {
        ArrayList<Accessable> routesByOne = findRoutesByOneEle(fromFlr, toFlr);
        if (!(routesByOne.isEmpty())) {
            return routesByOne;
        }
        ArrayList<Accessable> routesByTwo = findRoutesByTwoEle(fromFlr, toFlr);
        if (!(routesByTwo.isEmpty())) {
            return routesByTwo;
        }
        /*ArrayList<Accessable> routesBy3 = findRoutesBy3(fromFlr, toFlr);
        if (!(routesBy3.isEmpty())) {
            return routesBy3;
        }*/
        //todo 需要换乘四次及以上要怎么写
        for (int i = 3; ; i++) {
            findFlag = 0;
            dfsFindRoutes(fromFlr, toFlr, 0, i);
            if (findFlag == 1) {
                //找到i次换乘的路径
                Accessable acc = new Accessable();
                for (int j = 0; j < i; j++) {
                    acc.addIndex(accessing[j]);
                }
                ArrayList<Accessable> temp = new ArrayList<>();
                temp.add(acc);
                return temp;
            }
        }
    }

    public static int decide(int fromFlr, boolean direction, ArrayList<Integer> chosenEle) {
        //根据from的楼层和方向决定分给哪部电梯
        ArrayList<Integer> canPassBy = new ArrayList<>();
        ArrayList<Integer> canPassByWaitNum = new ArrayList<>();
        int minNotInReq = 0;
        int minNum = 1000;
        for (int i = 0; i < chosenEle.size(); i++) {
            if (allElevator.get(chosenEle.get(i)).getMaintainTag() == false) {
                //只在不maintain的电梯里面找可以分配请求的电梯
                if (allElevator.get(chosenEle.get(i)).canPassBy((fromFlr)) &&
                        allElevator.get(chosenEle.get(i)).getDirection() == direction) {
                    canPassBy.add(chosenEle.get(i));
                    canPassByWaitNum.add(allRequestArr.get(chosenEle.get(i)).getNotInNum());
                }
                if (allRequestArr.get(chosenEle.get(i)).isPeopleExp().size() < minNum) {
                    minNum = allRequestArr.get(chosenEle.get(i)).isPeopleExp().size();
                    minNotInReq = chosenEle.get(i);
                    //找到请求最少的电梯在数组里面的编号
                }
            }
        }
        if (!(canPassBy.isEmpty())) {
            int tempMin = 100;
            int tempNum = -1;
            for (int i = 0; i < canPassByWaitNum.size(); i++) {
                if (canPassByWaitNum.get(i) < tempMin) {
                    tempMin = canPassByWaitNum.get(i);
                    tempNum = i;
                }
            }
            return canPassBy.get(tempNum);
        } else {
            return minNotInReq;
        }
    }

    public static Routes findBestRoutes(ArrayList<Accessable> arr, int fromFlr, int toFlr) {
        //只可能每一项都包含一个电梯、两个电梯、或者三个电梯
        //找到最好的Accessable，然后将它转换成routes
        int index;
        if (arr.size() == 1) {
            index = 0;
        } else {
            ArrayList<Integer> firstEle = new ArrayList<>();
            for (Accessable i : arr) {
                firstEle.add(i.firstIndex());
            }
            int ans = decide(fromFlr, (toFlr - fromFlr > 0), firstEle);
            //ans是选中电梯的编号
            index = firstEle.indexOf(ans);
            //index 是包含此选中电梯的路径在arr中的编号
        }
        Accessable accessable = arr.get(index);
        ArrayList<Integer> elIndexs = accessable.getEle();
        //System.out.println("chose " + accessable);
        Routes routes = new Routes();
        routes.addFlr(fromFlr);
        int preFloor = fromFlr;
        for (int i = 0; i < (elIndexs.size() - 1); i++) {
            routes.addEle(elIndexs.get(i));
            int tempFlr = findIntersectFloor(elIndexs.get(i), elIndexs.get(i + 1), preFloor, toFlr);
            routes.addFlr(tempFlr);
            preFloor = tempFlr;
        }
        routes.addEle(elIndexs.get(elIndexs.size() - 1));
        //todo 注意 检查逻辑
        routes.addFlr(toFlr);
        return routes;
    }

    public static int findIntersectFloor(int i, int j, int preFlr, int toFlr) {
        int access1 = allElevator.get(i).getAccess();
        int access2 = allElevator.get(j).getAccess();
        int intersect = access1 & access2;
        if (toFlr > preFlr) {
            for (int k = preFlr + 1; k <= toFlr; k++) {
                if (floorHasAccess(intersect, k)) {
                    return k;
                }
            }
        } else {
            for (int k = toFlr; k >= preFlr; k--) {
                if (floorHasAccess(intersect, k)) {
                    return k;
                }
            }
        }
        for (int k = 1; k <= 11; k++) {
            if (floorHasAccess(intersect, k)) {
                return k;
            }
        }
        return -1;//意味没找到
    }

    public static boolean floorHasAccess(int access, int floor) {
        int ans = access & (1 << (floor - 1));
        if (ans != 0) {
            return true;
        } else {
            return false;
        }
    }

    private static int getEleAccess(int index) {
        return allElevator.get(index).getAccess();
    }
}
