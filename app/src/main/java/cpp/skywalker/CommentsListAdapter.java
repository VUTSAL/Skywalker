package cpp.skywalker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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


import java.util.ArrayList;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {

    private ArrayList<Comments> getComments = new ArrayList<>();

    private Context context;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference filepath;

    ArrayList<String> urls = new ArrayList<>();
    //private Singleton var = Singleton.getInstance();
    public CommentsListAdapter(Context frgEventInfoContext, ArrayList<Comments> commentList) {
        getComments = commentList;
        context = frgEventInfoContext;


    }
    @NonNull
    @Override
    public CommentsListAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card_comment_list_view, parent, false);
        return new CommentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsListAdapter.CommentViewHolder holder, int position) {


        holder.tvUserName.setText(getComments.get(position).Name); //getComments.get(position).Name
        holder.tvComments.setText(getComments.get(position).Comments);//getComments.get(position).Comments
        Bitmap bitmap1= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user1_stark);
        Bitmap bitmap2= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user2_stark);
        Bitmap bitmap3= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.user3_stark);

        Bitmap resized1 = Bitmap.createScaledBitmap(bitmap1, 100, 100, true);
        Bitmap conv_bm1 = getRoundedRectBitmap(resized1, 100);



        Bitmap resized2 = Bitmap.createScaledBitmap(bitmap2, 100, 100, true);
        Bitmap conv_bm2 = getRoundedRectBitmap(resized2, 100);



        Bitmap resized3 = Bitmap.createScaledBitmap(bitmap3, 100, 100, true);
        Bitmap conv_bm3 = getRoundedRectBitmap(resized3, 100);
if(position/3==0)
{
    holder.ivUserPhoto.setImageBitmap(conv_bm3);
}
else  if(position/2==0)
{
    holder.ivUserPhoto.setImageBitmap(conv_bm2);
}
else {
    holder.ivUserPhoto.setImageBitmap(conv_bm1);


}

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
        return getComments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {


        ImageView ivUserPhoto;

        TextView tvUserName;
        TextView tvComments;


        public CommentViewHolder(View itemView) {
            super(itemView);
            ivUserPhoto = (ImageView) itemView.findViewById(R.id.ivUserPhoto);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvComments = (TextView) itemView.findViewById(R.id.tvComments);



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
