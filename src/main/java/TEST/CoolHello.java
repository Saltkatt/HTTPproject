package TEST;

import HTTPcommunication.HTTPRequest;
import HTTPcommunication.HTTPResponse;
import HTTPcommunication.HTTPResponseGenerator;

@AnnotationsClass.Page("/Hello")
public class CoolHello implements SayHello {

    @Override
    public HTTPResponse sayHi() {
        String html = "<!DOCTYPE html><html><h3 style='color:red;'>hello</h3></html>";
        byte[] body = html.getBytes();
        HTTPResponse response = new HTTPResponse();
        response.setStatus(200).setMessage("OK").setContentType("text/html").setContentLength(body.length).setBody(body);
        return response;
    }
}
