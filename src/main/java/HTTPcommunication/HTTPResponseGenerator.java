package HTTPcommunication;

import TEST.AnnotationsClass;
import TEST.SayHello;
import com.sun.javafx.image.BytePixelSetter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.ServiceLoader;

public class HTTPResponseGenerator {

    private final static String WEB_ROOT = ".//web";
    private static boolean isFile;
    //private  boolean isFile = true;

    public static HTTPResponse getHTTPResponse(HTTPRequest request){

        String message = "OK";
        String contentType = "";
        int status = 200;


        String url = "";
        String name = "";

        String q = request.getQuery();
        System.out.println(q);

//        if(request.getQuery().contains("name"))
//            name = q.substring(q.indexOf("name")+5);
//        else
//            name = q.substring(q.indexOf("=")+1);
        String key = "";
        String value = "";
        if(!q.isEmpty() && q != null && !q.equals("") && q.contains("=")) {
            key = q.substring(q.indexOf("?")+1, q.indexOf("="));
            value = q.substring(q.indexOf("=")+1);
        }

        if(request.getURL().equals("/Hello")){
            ServiceLoader<SayHello> loader = ServiceLoader.load(SayHello.class);
            Iterator<SayHello> it = loader.iterator();
            for(SayHello h : loader){
                if(h.getClass().getAnnotation(AnnotationsClass.Page.class).value().equals("/Hello"))
                    return h.toJSON(key, value);
            }



        }

        if(!(request.getURL().equals("/")) && !(request.getURL().equals("")))
            url = request.getURL().substring(1);


        if(!request.getURL().isEmpty() && request.getURL().contains(".")) {
            String fileEnding = request.getURL().substring(request.getURL().indexOf("."));
            contentType = contentTypeRequested(fileEnding);
        }

        byte[] content = new byte[0];

        if(!(request.getURL().isEmpty())) {

            File file = null;

            if ((!(request.getURL().equals("/"))) && new File(WEB_ROOT, url).exists()) {
                System.out.println("Inside file found");
                file = new File(WEB_ROOT, url);
            }
            else if (request.getURL().equals("/")) {
                System.out.println("Inside default");
                file = new File(WEB_ROOT, "index.html");
                contentType = "text/html";
            }
            else {
                System.out.println("Inside 404");
                status = 404;
                message = "Not Found";
                file = new File(WEB_ROOT, "404.html");
            }

            try {
                content = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        int contentLength = content.length;
        String connection = request.getConnection();


        return new HTTPResponse().setStatus(status).setMessage(message).setContentType(contentType).setContentLength(contentLength).setConnection(connection).setBody(content);
    }

    private static String contentTypeRequested(String fileExtension){
        switch (fileExtension){
            case ".html" : return "text/html";
            case ".css" : return "text/css";
            case ".js" : return "text/javascript";
            case ".png" : return "image/png";
            case ".pdf" : return "application.pdf";
            case ".jpg" : return "image/jpeg";
            case ".json" : return "application.json";
            default: return "text/plain";
        }
    }
}