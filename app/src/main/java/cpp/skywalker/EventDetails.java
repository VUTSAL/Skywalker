package cpp.skywalker;

import java.io.Serializable;
import java.util.ArrayList;

public class EventDetails implements Serializable {

    public String Title;
    public String ImgUrl;
    public String Location;
    public String HostedBy;
    public String ShortDescription;
    public String Description;
    public int MemberCount;

    public ArrayList<Comments> commntList;
    public ArrayList<String>UserName;


}
