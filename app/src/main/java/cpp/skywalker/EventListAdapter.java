package cpp.skywalker;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    //arraylist of all the food within the some distance only
    private ArrayList<GetEventListInfo> getEventListInfo = new ArrayList<>();
    //List view activity Context to inflate the card view
    private EventListActivity context;
    //Storage reference
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference filepath;
    //for firebase authentication
    //Current user properties
    //GetUserInfo currentUserInfo;
    //Arraylist of all the uri
    ArrayList<String> urls = new ArrayList<>();
    //private Singleton var = Singleton.getInstance();
    public EventListAdapter(EventListActivity eventListActivity, ArrayList<GetEventListInfo> eventDetails) {
        getEventListInfo = eventDetails;
        context = eventListActivity;


    }
    @NonNull
    @Override
    public EventListAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_event_list_view, parent, false);
        return new EventViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.EventViewHolder holder, int position) {

//        //Setting todays date
//        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
//        final Date now = new Date();
//        final String strDate = sdfDate.format(now);
        holder.txtEventTitle.setText( getEventListInfo.get(position).Title);
        holder.txtEventShortDescription.setText(getEventListInfo.get(position).ShortDescription);
        holder.txtLocation.setText(getEventListInfo.get(position).Location);
        holder.txtTime.setText( "On:"+getEventListInfo.get(position).FromDate+"-"+getEventListInfo.get(position).ToDate+"At:"+getEventListInfo.get(position).FromTime+"-"+getEventListInfo.get(position).ToTime);
        //Getthig the image of the food
//        filepath = storageReference.child("Todays Food").child(strDate).child(todayFoodInfo.get(position).user_name + "_" + todayFoodInfo.get(position).dish_name);
//        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(context).load(uri.toString()).into(holder.todaysFoodImage);
//                urls.add(uri.toString());
//                var.urlOfTodaysFoodImage.put(todayFoodInfo.get(position).user_name + "_" + todayFoodInfo.get(position).dish_name, uri.toString());
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SeekerClickedTodaysFoodInfo.class);
//                intent.putExtra("current_user_info", currentUserInfo);
//
//                intent.putExtra("current_value", position);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return getEventListInfo.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {


        ImageView imgUser1;
        ImageView imgUser2;
        ImageView imgUser3;
        TextView txtEventTitle;
        TextView txtEventShortDescription;
        TextView txtTime;
        TextView txtLocation;
        TextView txtMemberCount;

        public EventViewHolder(View itemView) {
            super(itemView);
            imgUser1 = (ImageView) itemView.findViewById(R.id.imgUser1);
            imgUser2 = (ImageView) itemView.findViewById(R.id.imgUser2);
            imgUser3 = (ImageView) itemView.findViewById(R.id.imgUser3);
            txtEventTitle = (TextView) itemView.findViewById(R.id.txtEventTitle);
            txtEventShortDescription = (TextView) itemView.findViewById(R.id.txtEventShortDescription);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtMemberCount = (TextView) itemView.findViewById(R.id.txtMemberCount);


        }
    }
}
