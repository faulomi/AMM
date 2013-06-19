package models;

import play.data.validation.Constraints;

/**
 * Created with IntelliJ IDEA.
 * User: Jérôme
 * Date: 30/05/13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class ShareRecord {

    public String source;
    public String destination;
    @Constraints.Required
    public String id;
    public String type;
}
