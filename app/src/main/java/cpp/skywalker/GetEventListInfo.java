package cpp.skywalker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class GetEventListInfo implements Serializable {
    public String Title;
    public String ImgUrl;
    public String Location;
    public String HostedBy;
    public String ShortDescription;
    public String Agenda;
    public int MemberCount;
    public String FromDate;
    public String ToDate;
    public String FromTime;
    public String ToTime;
    public String CreatedDate;
    public  String UniqueID;
    public ArrayList<Comments> CommentList;
    public ArrayList<String>Members;
}
