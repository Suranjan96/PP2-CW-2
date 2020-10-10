import java.util.LinkedList;

public class PassengerQueue extends Object{

    private static LinkedList<Passenger> queueArray = new LinkedList<>();
    private int maxStayInQueue;
    private int maxLength;

    public static void add(int i, Passenger passenger) {
        queueArray.add(i, passenger);
    }

    public static Passenger remove(int i){
        return  queueArray.get(i);
    }

    public static LinkedList<Passenger> getQueueArray() {
        return queueArray;
    }

    public static boolean isEmpty(){
        if (queueArray.size()==0){
            return true;
        }
        return false;
    }

    public static boolean isFull(){
        if (queueArray.size()>=42){
            return true;
        }
        return false;
    }

    public void setLength(int maxLength){
        this.maxLength = maxLength;
    }

    public int getLength(){
        return maxLength;
    }

    public void setMaxStay(int maxStayInQueue){
        this.maxStayInQueue = maxStayInQueue;
    }

    public int getMaxStay(){
        return maxStayInQueue;
    }
}
