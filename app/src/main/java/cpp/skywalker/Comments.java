package cpp.skywalker;

import java.io.Serializable;


public class Comments implements Serializable{
    public  String Name;
    public  String ImgUrl;
    public  String Comments;

    public Comments()
    {

    }
    public  Comments(String name,String comments,String url)
    {
        Name=name;
        Comments=comments;
        ImgUrl=url;
    }

}
