import com.mongodb.BasicDBObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.mongodb.client.*;
import java.io.*;
import java.util.*;
import org.bson.Document;

public class TrainStation extends Application {

    private static Passenger[] waitingRoom = new Passenger[42];
    private static  ArrayList<Passenger> randomQueue = new ArrayList<>();
    private static ArrayList<Passenger> Train = new ArrayList();
    private static ArrayList<Integer> MaxLength = new ArrayList<>();
    private static ArrayList<Integer> MaxStay = new ArrayList<>();
    private static int TotalTime = 0;

    public static void main(String[] args) {

        MongoClient mongoClient = MongoClients.create("mongodb://Localhost:27017");  //create a client
        MongoDatabase database = mongoClient.getDatabase("TrainBookingSystem");   //get database
        MongoCollection<Document> collection = database.getCollection("DenuwaraManike");  //in database create a collection
        FindIterable<Document> data = collection.find();//find data in records
        for(Document record:data){  //get collection of data from records
            int date = Integer.parseInt(record.get("date").toString())-1;
            int seat = Integer.parseInt(record.get("SeatNum").toString())-1;
            String fname = record.get("fname").toString();
            String sname = record.get("sname").toString();
            String trip = record.get("trip").toString();
            if (trip.equals("1") && date==0) {
                Passenger passengerObj = new Passenger();
                passengerObj.setName(fname,sname);
                passengerObj.setSeat(seat+1);
                waitingRoom[seat]=passengerObj;
            }
        }
        System.out.println("Successfully data added to the waiting room");
        System.out.print("Waiting Room - ");
        for (Passenger passenger : waitingRoom){
            if(passenger!=null){
                System.out.print(passenger.getName()+" | ");}
        }
        launch();
    }
    @Override
    public void start (Stage primaryStage) throws Exception {
        menu:
        while (true) {
                Scanner input = new Scanner(System.in);
                System.out.println("");      //Main menu
                System.out.println("");
                System.out.println("|----------------MENU BAR------------------|");
                System.out.println("|'A'- Add customer to a seat               |");
                System.out.println("|'V'- View all seats                       |");
                System.out.println("|'D'- Delete customer from seat            |");
                System.out.println("|'R'- Run the simulation and produce report|");
                System.out.println("|'S'- Store program data in to file        |");
                System.out.println("|'L'- Load the program data from file      |");
                System.out.println("|'Q'- Quit                                 |");
                System.out.println("|------------------------------------------|");
                System.out.print("Enter letter: ");
                String option = input.next();
                System.out.println();
                switch (option) {
                    case "A":
                    case "a":
                        AddCustomer();
                        break;
                    case "V":
                    case "v":
                        ViewQueue();
                        break;
                    case "D":
                    case "d":
                        DeletePassenger();
                        break;
                    case "R":
                    case "r":
                        Report();
                        break;
                    case "S":
                    case "s":
                        storeData();
                        break;
                    case "L":
                    case "l":
                        loadData();
                        break;
                    case "Q":
                    case "q":
                        System.out.println();
                        break menu;
                    default:
                        System.out.println("I'm not sure what you said, please try again");
                }
            }
        }
    private static void AddCustomer(){
        ArrayList<Passenger> temp = new ArrayList();

        if (PassengerQueue.isFull()){
            System.out.println("The passenger queue is full");
            return;
        }

        boolean flag=false;
        for (Passenger passenger : waitingRoom){
            if (passenger!=null) {
                flag = true;
                break;
            }
        }

        if (!flag && randomQueue.isEmpty()) {
            System.out.println("No passengers in the waiting room");
            return;
        }

        int count1 = 0;
        for (int x = 0; x < 42; x++) {
            if (waitingRoom[x] != null) {
                count1++;
            }
        }

        if (randomQueue.size()==0){
            int num1;
            Random rand = new Random();
            num1 = rand.nextInt(6)+1;
            System.out.println("You can only add "+num1+" members to the queue.");

            if (count1<=6){ num1=count1; }
            if (randomQueue.isEmpty()){
                int count=0;
                for (int i =0 ;i<42;i++ ){
                    if(waitingRoom[i] != null){
                        randomQueue.add(waitingRoom[i]);
                        waitingRoom[i] = null;
                        count++;
                    }
                    if(count==num1) break;
                }
            }

            Button[] buttons = new Button[6];    //button array
            GridPane gridPane = new GridPane();

            Button btnbook = new Button("OK");
            gridPane.add(btnbook, 3, 7);
            btnbook.setPrefHeight(40);
            btnbook.setPrefWidth(80);
            btnbook.setStyle("-fx-background-color:#2980B9");

            Button btnbord = new Button("Confirm");
            gridPane.add(btnbord, 4, 7);
            btnbord.setPrefHeight(40);
            btnbord.setPrefWidth(80);
            btnbord.setStyle("-fx-background-color:#2980B9");
            gridPane.setStyle("-fx-background-image:url('img1.jpg');-fx-background-size: stretch;");
            Stage stage = new Stage();
            stage.setTitle("Train Queue");

            int n = 0;
            while (n < num1) {        //creating GUI on gridpane
                buttons[n] = new Button("" + (n + 1));
                int finalX = n;
                buttons[n].setOnAction(event -> {
                    buttons[finalX].setStyle("-fx-background-color:#EC7063");
                    temp.add(randomQueue.get(finalX));
                });
                buttons[n].setPrefSize(40, 40);
                n++;
            }

            int num = 0;     //oder of seats
            for (int i = 0; i < num1; i++) {
                for (int j = 0; j < 1; j++) {
                    gridPane.add(buttons[num++], j, i);
                }
            }
            btnbord.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    event -> {
                        for (int i = 0; i < temp.size(); i++) {
                            PassengerQueue.add(i,temp.get(i));
                            randomQueue.remove(temp.get(i));
                        }
                        System.out.print("Waiting Room - ");
                        for (Passenger passenger : waitingRoom){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+" | ");
                            }
                        }
                        System.out.println();
                        System.out.print("Passenger Queue - ");
                        for (Passenger passenger : PassengerQueue.getQueueArray()){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+"-"+passenger.getSeat()+" | ");
                            }
                        }
                        stage.close();

                    });
            btnbook.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    event -> {
                        stage.close();
                        System.out.print("Waiting Room - ");
                        for (Passenger passenger : waitingRoom){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+" | ");
                            }
                        }
                        System.out.println();
                        System.out.print("Passenger Queue - ");
                        for (Passenger passenger : PassengerQueue.getQueueArray()){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+"-"+passenger.getSeat()+" | ");
                            }
                        }
                    });
            Scene sceneAdd = new Scene(gridPane, 500, 500);
            stage.setScene(sceneAdd);
            stage.showAndWait();
        }
        else {
            Button[] buttons = new Button[randomQueue.size()];    //button array
            GridPane gridPane = new GridPane();

            Button btnbook = new Button("OK");
            gridPane.add(btnbook, 3, 7);
            btnbook.setPrefHeight(40);
            btnbook.setPrefWidth(80);
            btnbook.setStyle("-fx-background-color:#2980B9");
            gridPane.setStyle("-fx-background-color:#17202A");

            Button btnbord = new Button("Confirm");
            gridPane.add(btnbord, 4, 7);
            btnbord.setPrefHeight(40);
            btnbord.setPrefWidth(80);
            btnbord.setStyle("-fx-background-color:#2980B9;");
            gridPane.setStyle("-fx-background-image:url('img1.jpg');-fx-background-size: stretch;");

            Stage stage = new Stage();
            stage.setTitle("Train Queue");

            int n = 0;
            while (n < randomQueue.size()) {        //creating GUI on gridpane
                buttons[n] = new Button("" + (n + 1));
                int finalX = n;
                buttons[finalX].setStyle("-fx-background-color:#EC7063");
                temp.add(randomQueue.get(finalX));
                buttons[n].setPrefSize(40, 40);
                n++;
            }
            for (int j = 0; j < randomQueue.size(); j++) {  // disable the selected button
                buttons[j].setStyle("-fx-background-color:#EC7063");
            }

            int num = 0;     //oder of seats
            for (int i = 0; i < randomQueue.size(); i++) {
                for (int j = 0; j < 1; j++) {
                    gridPane.add(buttons[num++], j, i);
                }
            }
            btnbord.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    event -> {
                        for (int i = 0; i < temp.size(); i++) {
                            PassengerQueue.add(i,temp.get(i));
                            randomQueue.remove(temp.get(i));
                        }
                        System.out.print("Waiting Room - ");
                        for (Passenger passenger : waitingRoom){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+" | ");
                            }
                        }
                        System.out.println();
                        System.out.print("Passenger Queue - ");
                        for (Passenger passenger : PassengerQueue.getQueueArray()){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+"-"+passenger.getSeat()+" | ");
                            }
                        }
                        stage.close();

                    });
            btnbook.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    event -> {
                        stage.close();
                        System.out.print("Waiting Room - ");
                        for (Passenger passenger : waitingRoom){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+" | ");
                            }
                        }
                        System.out.println();
                        System.out.print("Passenger Queue - ");
                        for (Passenger passenger : PassengerQueue.getQueueArray()){
                            if (passenger!=null){
                                System.out.print(passenger.getName()+"-"+passenger.getSeat()+" | ");
                            }
                        }
                    });
            Scene sceneAdd = new Scene(gridPane, 500, 500);
            stage.setScene(sceneAdd);
            stage.showAndWait();
        }Collections.sort(PassengerQueue.getQueueArray());
    }
    private static void ViewQueue(){
        if (PassengerQueue.isEmpty()){
            System.out.println("The passenger queue is empty");
            return;
        }
        Passenger[] View = new Passenger[42];

        Stage stage = new Stage();
        stage.setTitle("Passenger Queue");
        Scene  sceneView;
        Button[] buttons = new Button[42];
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-image:url('img2.jpg');-fx-background-size: stretch;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Button btnbook = new Button("OK");
        gridPane.add(btnbook, 2, 9);
        btnbook.setPrefHeight(50);
        btnbook.setPrefWidth(110);
        btnbook.setStyle("-fx-background-color:#E67E22");

        for (int i=0; i<PassengerQueue.getQueueArray().size();i++){
            View[i] = PassengerQueue.getQueueArray().get(i);
        }

        int n = 0;
        while (n<42) {
            if (View[n] != null) {
                buttons[n] = new Button("" + View[n].getSeat()+" | " + View[n].getFirstName());
                buttons[n].setStyle("-fx-background-color:#EC7063");  //red
            }else{
                buttons[n] = new Button("Empty" );
                buttons[n].setStyle("-fx-background-color:#2ECC71");  //green
            }
            buttons[n].setPrefSize(90, 50);
            n++;
        }

        int numb = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                gridPane.add(buttons[numb++], j, i);
            }
        }
        btnbook.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    stage.close();
                });
        sceneView = new Scene(gridPane, 700, 500);
        stage.setScene(sceneView);
        stage.showAndWait();
    }
    private static void DeletePassenger(){
        if (PassengerQueue.isEmpty()){
            System.out.println("The passenger queue is Empty");
            return;
        }
        Scanner input = new Scanner(System.in);
        System.out.print("What is the seat number you want delete(seats 1-42): ");
        int number = input.nextInt();
        if (number <= 42 && number>0) {
            int count =0;
            int x=0;
            for (int i=0; i<PassengerQueue.getQueueArray().size();i++){
                if (PassengerQueue.getQueueArray().get(i).getSeat()==number){
                    Passenger passenger = PassengerQueue.remove(i);
                    System.out.println("Successfuly Deleted: "+passenger.getName()+" from the seat number: "+passenger.getSeat() );
                    PassengerQueue.getQueueArray().remove(count);
                    x++;
                } count++;
            }
        if(x==0) {
            System.out.println("There is no passenger in the seat number "+number+"!!!!! ");
        }
        }
    }
    public static void Report(){
        if (PassengerQueue.isEmpty()){
            System.out.println("The passenger queue is Empty");
            return;
        }
        int MaxLength =0;
        int Time;

        for (int i=0; i<PassengerQueue.getQueueArray().size();i++){
            Train.add(PassengerQueue.getQueueArray( ).get(i));
            MaxLength++;
            TrainStation.MaxLength.add(MaxLength);
        }
        int tot = 0;
        for (int i=0; i<PassengerQueue.getQueueArray().size();i++){
                Random rand = new Random();
                int time1 = rand.nextInt(6) + 1;
                int time2 = rand.nextInt(6) + 1;
                int time3 = rand.nextInt(6) + 1;

                Time = time1 + time2 + time3;
                tot += Time;
                TotalTime += Time;
                MaxStay.add(Time);

            PassengerQueue.getQueueArray().get(i).setSecondsInQueue(tot);

        }
        int Average=TotalTime/Train.size();

        PassengerQueue MaxObj = new PassengerQueue();
        MaxObj.setLength(Collections.max(TrainStation.MaxLength));
        MaxObj.setMaxStay(Collections.max(TrainStation.MaxStay));

        PassengerQueue.getQueueArray().clear();

        Stage stage = new Stage();
        stage.setTitle("Report");
        Scene  sceneView;
        Button[] buttons = new Button[Train.size()];
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-image:url('img3.jpg');-fx-background-size: stretch;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Button btnbook = new Button("OK");
        gridPane.add(btnbook, 2, 9);
        btnbook.setPrefHeight(70);
        btnbook.setPrefWidth(140);
        btnbook.setStyle("-fx-background-color:#2980B9");

        Label label_1 = new Label("--SUMMARY--");
        gridPane.add(label_1,12,1);
        label_1.setStyle("-fx-text-fill:white");

        Label label_2 = new Label("Max Length- "+MaxObj.getLength());
        gridPane.add(label_2,12,2);
        label_2.setStyle("-fx-text-fill:white");

        Label label_3 = new Label("Max Time- "+MaxObj.getMaxStay());
        gridPane.add(label_3,12,3);
        label_3.setStyle("-fx-text-fill:white");

        Label label_4 = new Label("Min Time- "+Collections.min(TrainStation.MaxStay));
        gridPane.add(label_4,12,4);
        label_4.setStyle("-fx-text-fill:white");

        Label label_5 = new Label("Average Time- "+Average);
        gridPane.add(label_5,12,5);
        label_5.setStyle("-fx-text-fill:white");

        for (int r = 0; r < Train.size(); r++) {
            buttons[r] = new Button("Seat | " + Train.get(r).getSeat() + "\nName | " + Train.get(r).getFirstName()+"\nTime | "+Train.get(r).getSeconds()+"s");
            buttons[r].setStyle("-fx-background-color:#EC7063");  //red
            buttons[r].setPrefSize(130, 70);
        }
        File file = new File("File.txt");
        PrintWriter pw = null;
        FileWriter fw ;
        for (int r = 0; r < Train.size(); r++) {
            try {
                fw = new FileWriter(file, false);  //can append the data for file
                pw = new PrintWriter(fw, true);  //delete old data when run the program again
                pw.println("Name- "+Train.get(r).getName());
                pw.println("Seat- "+Train.get(r).getSeat());
                pw.println("Time- "+Train.get(r).getSeconds()+"s");
                pw.println("");

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("No prmission to the file");
            }
        }
        pw.println("Max Length- "+MaxObj.getLength());
        pw.println("Max Time- "+MaxObj.getMaxStay());
        pw.println("Min Time- "+Collections.min(TrainStation.MaxStay));
        pw.println("Average Time- "+Average);
        pw.println("");
        pw.close();

        int numb = 0;
        int c=0;
        for (int i = 0; i <7; i++) {
            for (int j = 0; j < 6; j++) {
                gridPane.add(buttons[numb++], j, i);
                c++;
                if (c==Train.size()){break;}
            } if (c==Train.size()){break;}
        }
        btnbook.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    stage.close();
                });
        sceneView = new Scene(gridPane, 1200, 800);
        stage.setScene(sceneView);
        stage.showAndWait();
    }
    public static void storeData()  {
        if (PassengerQueue.isEmpty()){
            System.out.println("No data to store... Please add data!!!");
            return;
        }
        MongoClient mongoClient = MongoClients.create("mongodb://Localhost:27017"); //create a client
        MongoDatabase database = mongoClient.getDatabase("TrainStation-Colombo");  //get database
        MongoCollection<Document> collection = database.getCollection("DenuwaraManike");  //in database create a collection
        BasicDBObject document = new BasicDBObject();
        collection.deleteMany(document);    //delete old data from db

        for (Passenger passenger : PassengerQueue.getQueueArray()) {
            Document record = new Document("title", "records")  //add data to the db
                    .append("SeatNumber", passenger.getSeat() )
                    .append("Name", passenger.getName())
                    .append("Date",1 )
                    .append("Trip","Colombo-Badulla" );
            collection.insertOne(record);
        }
    }
    public static void loadData() {
        MongoClient mongoClient = MongoClients.create("mongodb://Localhost:27017");  //create a client
        MongoDatabase database = mongoClient.getDatabase("TrainStation-Colombo");   //get database
        MongoCollection<Document> collection = database.getCollection("DenuwaraManike");  //in database create a collection
        FindIterable<Document>  data = collection.find();//find data in records
        for(Document record:data){  //get collection of data from records
            String name = record.get("Name").toString();
            int seat = Integer.parseInt(record.get("SeatNumber").toString());

            Passenger loadData = new Passenger();
            loadData.setName(name,"");
            loadData.setSeat(seat);
            PassengerQueue.getQueueArray().add(loadData);

            if(seat-1<42){   //to delete passenger from waitingroom
                waitingRoom[seat-1] = null;
            }
        }
        System.out.println("Successfully data load to the program");
        for (Passenger passenger : PassengerQueue.getQueueArray()){
            if (passenger!=null){
                System.out.print(passenger.getName()+"-"+passenger.getSeat()+" | ");
            }
        }
    }
}
