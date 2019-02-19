package TEST;

import HTTPcommunication.HTTPRequest;
import HTTPcommunication.HTTPResponse;
import HTTPcommunication.HTTPResponseGenerator;
import org.json.JSONObject;

@AnnotationsClass.Page("/Hello")
public class CoolHello implements SayHello {

    @Override
    public HTTPResponse sayHi(@AnnotationsClass.Param("name")String name) {
        String html = "<!DOCTYPE html><html><h3 style='color:red;'>HELLO "+name+"</h3></html>";
        byte[] body = html.getBytes();
        HTTPResponse response = new HTTPResponse();
        response.setStatus(200).setMessage("OK").setContentType("text/html").setContentLength(body.length).setBody(body);
        return response;
    }

    public HTTPResponse toJSON(String key, String value){
        HTTPResponse response = new HTTPResponse();
        byte[] body = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        body = jsonObject.toString().getBytes();
        response.setStatus(200).setMessage("OK").setContentLength(body.length).setContentType("application/json").setBody(body);
        return response;
    }
}
