package cpp.skywalker;

import android.content.Context;
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
}
