package TEST;

import HTTPcommunication.HTTPRequest;
import HTTPcommunication.HTTPResponse;

public interface SayHello {

    public HTTPResponse sayHi(String name);

    public HTTPResponse toJSON(String key, String value);

}
