package models;

import java.util.HashMap;
import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: Jérôme
* Date: 01/06/13
* Time: 01:05
* To change this template use File | Settings | File Templates.
*/
public class Query {

    public String url;
    public final Map<String, String> queryParameters = new HashMap<String, String>();
}
