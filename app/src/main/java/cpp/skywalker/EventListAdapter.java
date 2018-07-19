package cpp.skywalker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
    private Bundle arguments = new Bundle();
    GetUserInfo userInfo=null;
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
    public EventListAdapter(EventListActivity eventListActivity, ArrayList<GetEventListInfo> eventDetails,GetUserInfo UInfo) {
        getEventListInfo = eventDetails;
        context = eventListActivity;
        userInfo=UInfo;

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

        int Count=getEventListInfo.get(position).Members!=null && getEventListInfo.get(position).Members.size()>0? getEventListInfo.get(position).Members.size():0;
        holder.txtMemberCount.setText(Count==0?"No members yet":Count+" members");

        holder.txtTime.setText( "On:"+getEventListInfo.get(position).FromDate+"-"+getEventListInfo.get(position).ToDate);//+"At:"+getEventListInfo.get(position).FromTime+"-"+getEventListInfo.get(position).ToTime


//        Uri uri = Uri.parse("android.resource://cpp.skywalker/drawable/user1_stark");
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), uri);
        Bitmap bitmap1= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user1_stark);
        Bitmap bitmap2= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user2_stark);
        Bitmap bitmap3= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user3_stark);

        Bitmap resized1 = Bitmap.createScaledBitmap(bitmap1, 100, 100, true);
        Bitmap conv_bm1 = getRoundedRectBitmap(resized1, 100);
        holder.imgUser1.setImageBitmap(conv_bm1);


        Bitmap resized2 = Bitmap.createScaledBitmap(bitmap2, 100, 100, true);
        Bitmap conv_bm2 = getRoundedRectBitmap(resized2, 100);
        holder.imgUser2.setImageBitmap(conv_bm2);


        Bitmap resized3 = Bitmap.createScaledBitmap(bitmap3, 100, 100, true);
        Bitmap conv_bm3 = getRoundedRectBitmap(resized3, 100);
        holder.imgUser3.setImageBitmap(conv_bm3);
        //Getthig the image of the food
//        filepath = storageReference.child(userInfo.user_name).child()
//        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(context).load(uri.toString()).into(holder.todaysFoodImage);
//                urls.add(uri.toString());
//                var.urlOfTodaysFoodImage.put(todayFoodInfo.get(position).user_name + "_" + todayFoodInfo.get(position).dish_name, uri.toString());
//            }
//        });
        final int index=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment eventInfo = new frgEventInfo();
                arguments.putSerializable( "userInfo" , userInfo);
                arguments.putString( "UniqueID" , getEventListInfo.get(index).UniqueID);
                arguments.putString( "Position" ,""+index);
                arguments.putSerializable("EventDetails",getEventListInfo.get(index));
                //This will set the bundle as an argument to the object
                eventInfo.setArguments(arguments);
                FragmentManager fragmentManager = (context).getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.mainContent, eventInfo).commit();
                v.setVisibility(v.GONE);

            }
        });

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
    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            int roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (NullPointerException e) {
// return bitmap;
        } catch (OutOfMemoryError o){}
        return result;
    }

}
