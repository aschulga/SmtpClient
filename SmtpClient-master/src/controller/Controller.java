package controller;

import view.Document;

import java.io.*;
import java.net.Socket;
import java.util.Stack;

public class Controller {

    private Socket smtp = null;
    private PrintStream ps = null;
    private DataInputStream dis = null;
    private Document document;

    public Controller(Document document){
        this.document = document;
    }

    public void connect(String str){
        try {
            smtp = new Socket(str, 25);
            OutputStream os = smtp.getOutputStream();
            ps = new PrintStream(os);
            InputStream is = smtp.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.out.println(e);
            end();
        }
    }

    public void send(String str)
    {
        ps.println(str);
        ps.flush();
        document.add("Client: "+str);
    }

    public void receive()
    {
        try {
            String readstr = dis.readLine();
            document.add("Server: "+readstr);
        } catch (IOException e) {
            System.out.println(e);
            end();
        }
    }

    public StringBuilder getChat(){
        return document.getChat();
    }

    public void end(){

        try {
            if (smtp != null) {
                smtp.close();
            }

            if (dis != null) {
                dis.close();
            }

            if (ps != null) {
                ps.close();
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public void sendText(String from, String subject, String text){

        Stack<String> stack = new Stack<>();
        stack.push("\n");
        stack.push(".");
        stack.push(text);
        stack.push("\n");
        stack.push("subject: "+subject);
        stack.push("from: "+from);

        while(!stack.isEmpty()){
            send(stack.pop());
        }
    }
}
