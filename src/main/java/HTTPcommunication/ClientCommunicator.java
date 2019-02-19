
package HTTPcommunication;

import commentpage.JsonParser;
import commentpage.Sqlite;
import parsing.QueryStringToJSON;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientCommunicator extends Thread{

    private Socket clientSocket;
    private PrintWriter out;
    private OutputStream outByte;
    private BufferedReader in;

    private HTTPResponse response;
    private HTTPRequest request;


    public ClientCommunicator(Socket socket, PrintWriter outChar, OutputStream out, BufferedReader in) throws IOException {
        this.clientSocket = socket;
        this.out = outChar;
        this.outByte = out;
        this.in = in;
    }

    public void run() {

                try {
                       request = HTTPRequestFactory.getHTTPRequest(getRequestAsList());
                       request.setBody(getBody(request.getContentLength()));

                       //FIXA BÄTTRE LÖSNING
                       if (request.getURL().equals("/URL.html") && request.getQuery().length() > 0){
                           QueryStringToJSON.writeJsonObjToFile(QueryStringToJSON.convert(request.getQuery()), new File("web/jsonFromQuery.json"));
                       }



                        //Till kommentar sidan
                        if(request.getMethod().equals("POST")){

                            String httpBody = new String(request.getBody()); //byte array to string
                            String s = httpBody.replaceAll("\\+", " "); //all blank spaces became + symbols... this fixes it back to normal
                            String keyValue = s.substring(s.indexOf("=") + 1); //only take the key from key/value

                            Sqlite.insertOne(keyValue);
                            new JsonParser().writeJsonToFile(Sqlite.selectAll());



                        }






                       response = HTTPResponseGenerator.getHTTPResponse(request);
                       sendResponse(response);

                       if (response.getBody().length > 0 && !(request.getMethod().equals("HEAD"))) {
                           sendFile(response.getBody());
                       }else
                           out.println();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            try {

                out.close();
                in.close();
//                outByte.close();
                clientSocket.close();
                System.out.println("socket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


    public List<String> getRequestAsList() throws IOException {
        List<String> requestList = new ArrayList<>();
        String line = "";

        while((line = in.readLine()) != null && !(line.isEmpty())){
            requestList.add(line);
        }

        return requestList;
    }


    public byte[] getBody(int length) throws IOException {
        //StringBuilder body = new StringBuilder();
        byte[] body = new byte[length];
        int i = 0;
        while(in.ready()){
            //body.append((char) in.read());
            body[i] = (byte)in.read();
            i++;
        }
        return body;
    }

     public void sendResponse(HTTPResponse response){
        out.print(response);
        out.println();
        out.flush();
    }

    public String byteArrayToString(byte[] content){
        String body = "";
        for(int i = 0; i < content.length; i++){
            body += (char)content[i];
        }
        return body;
    }
    public void sendFile(byte[] content){
        try {
            outByte.write(content, 0, content.length);
            outByte.close();
            System.out.println("in send file");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}