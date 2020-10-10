public class Passenger implements Comparable {

    private String firstName;
    private String surName;
    private int orderSeat;
    private int SecondsInQueue;

    public void setName(String firstName, String surName){
        this.firstName = firstName;
        this.surName = surName;
    }

    public String getName(){
        return firstName +" "+ surName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setSeat(int oderSeat){
        this.orderSeat = oderSeat;
    }

    public int getSeat() {
        return orderSeat;
    }

    @Override
    public int compareTo(Object comparing)  {
        int Seat=((Passenger)comparing).getSeat();
        return this.orderSeat-Seat;
    }

    public int getSeconds(){
        return SecondsInQueue;
    }

    public void setSecondsInQueue(int SecondsInQueue){
        this.SecondsInQueue = SecondsInQueue;
    }

}
