

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class Client extends Thread {

    private String ip;
    private int port1;
            private int port2;
private int currentNum;
private volatile boolean stop=false;

     public Client(String ip, int first, int second) {
         this.ip = ip;
         this.port1 = first;
         this.port2 = second;
     }


    public void run() {

        try {
            Socket socket1 = new Socket(ip, port1);
            Socket socket2 = new Socket(ip,port2);
            BufferedReader fileReader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            BufferedReader fileReader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

            BufferedWriter   writer1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
            BufferedWriter  writer2 = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));

            writer1.write("Hello:231514\n");
            writer1.flush();

            writer2.write("Hello:231514\n");
            writer2.flush();

            String line1, line2;

            line1 = fileReader1.readLine();
            line2 = fileReader2.readLine();
            if(line1.equals("loggedIn:231514") && line2.equals("loggedIn:231514"))
             {
                 writer1.write("sendNumber:231514\n");
                 writer1.flush();

                 line1=fileReader1.readLine();
                 if(line1.startsWith("Number:")){
                     int num=Integer.parseInt(line1.substring(7));
                     writer1.write(num+"\n");
                     writer1.flush();

                     Thread t1=new Thread(()-> {
                        try{
                            String line;

                            while(!stop && (line = fileReader1.readLine()) != null){
                                handleMessage(line, writer1);
                            }

                        }
                        catch (IOException e) {
                            // TODO Auto-generated catch block
                          throw new RuntimeException(e);
                        }
                     });

                     Thread t2=new Thread(()-> {
                         try{
                             String line;

                             while(!stop &&  (line = fileReader1.readLine()) != null){
                                 handleMessage(line, writer2);
                             }

                         }
                         catch (IOException e) {
                             // TODO Auto-generated catch block
                             throw new RuntimeException(e);
                         }
                     });
t1.start();
        t2.start();
                     try {
                         t1.join();
                     } catch (InterruptedException e) {
                         throw new RuntimeException(e);
                     }
                     t2.join();
                 }
                 else{
                     System.out.println("Error in messages");
                 }


            }
            else {
                System.out.println("Error in messages");
            }



        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
private synchronized void handleMessage(String line, BufferedWriter writer) throws IOException{
         if(line.startsWith("increment:")){
             int val=Integer.parseInt(line.split(":")[1]);
             currentNum+=val;

         }
         else if(line.startsWith("decrement:")){
             int val=Integer.parseInt(line.split(":")[1]);
             currentNum-=val;
         }
         else if(line.startsWith("STOP")){
             stop=true;
             writer.write("current num is:" +currentNum+"\n");
             writer.flush();
         }
}
    public static void main(String[] args) {
        Client client = new Client( "192.168.88.10", 7771, 7772);
        client.start();
    }

}