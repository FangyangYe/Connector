import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPChat {
    private Socket sock;
    private ChatInput input;
    private ChatOutput output;
    public TCPChat(Socket sock){
        this.sock = sock;
        this.input = new ChatInput(sock);
        this.output = new ChatOutput(sock);
    }

    public void start(){
        input.start();
        output.start();
    }

    public void end(){
        input.stop();
        output.stop();
        try{
            sock.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}


class ChatInput extends Thread{
    private Socket sock;
    public ChatInput(Socket sock){
        this.sock = sock;
    }

    @Override
    public void run(){
        try(InputStream input = this.sock.getInputStream()){
            handle(input);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void handle(InputStream stream) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        for(;;){
            String s = reader.readLine();
            if(s!=null) {
                System.out.println("<<<" + s);
            }
        }
    }
}

class ChatOutput extends Thread{
    private Socket sock;
    public ChatOutput(Socket sock){
        this.sock = sock;
    }

    @Override
    public void run(){
        try(OutputStream output = this.sock.getOutputStream()){
            handle(output);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void handle(OutputStream stream) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(stream, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        for(;;){
            System.out.print(">>> "+"\r"); // 打印提示
            String s = scanner.nextLine(); // 读取一行输入
            writer.write(s);
            writer.newLine();
            writer.flush();
        }
    }
}