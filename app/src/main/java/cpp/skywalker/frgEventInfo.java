package cpp.skywalker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class frgEventInfo extends Fragment {
    Bitmap bitmap;
    Context context;
    private ProgressDialog progressDialog;
    GetUserInfo userInfo = new GetUserInfo();
    Date currentTime = Calendar.getInstance().getTime();

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat mdyformat = new SimpleDateFormat("yyyy");
    String currentYear= mdyformat.format(calendar.getTime());
    
    SimpleDateFormat mdmformat = new SimpleDateFormat("mm");
    String currentMonth= mdmformat.format(calendar.getTime());
    RecyclerView recyclerView;
    SimpleDateFormat mddformat = new SimpleDateFormat("yyyy");
    String currentDay= mddformat.format(calendar.getTime());
    String UniqueID;
    ArrayList<Comments> getComments = new ArrayList<>();
    private final static int GALLARY_INTENT = 2;
    Uri uri;
    public class PreviewEventHolder {
        ImageView ivEventPhoto;
        TextView tvEventTitle, tvShortDescription,tvLocation,tvAgenda,tvDateTime;
        Button btnSubmit,btnComment;
        EditText etComments;

    }
    EventDetails eventDetails;
    PreviewEventHolder previewEventHolder = new PreviewEventHolder();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        final StorageReference storageReferenceForEvent = FirebaseStorage.getInstance().getReference();
        final View rootView=inflater.inflate(R.layout.fragment_frg_event_info, container, false);
        final Context applicationContext =EventListActivity.getContextOfApplication();
        context=applicationContext;
        //Getting the intent value here
        final Bundle intent = getArguments();
        boolean userJoined=false;

        userInfo = (GetUserInfo) intent.getSerializable("UserInfo");
        eventDetails = (EventDetails) intent.getSerializable("EventDetails");
        for (String str:eventDetails.UserName) {
            if (str==userInfo.user_name)
            {
                userJoined=true;
                break;
            }
            
        }
        final boolean userJoinStatus=userJoined;
        UniqueID=intent.getString("UniqueID");
        //Initializing all the variables here
        final StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(eventDetails.Title + "_" + currentYear+"_"+currentMonth+"_"+currentDay);
        //-------------------------------------------------------------------------------------------------------------------------------------------//
        progressDialog = new ProgressDialog(applicationContext);

        previewEventHolder.ivEventPhoto = (ImageView)  rootView.findViewById(R.id.ivEventPhoto);
        previewEventHolder.tvAgenda = (TextView) rootView.findViewById(R.id.etAgenda);
        previewEventHolder.tvEventTitle = (TextView) rootView.findViewById(R.id.etEventTitle);

        previewEventHolder.tvLocation = (TextView) rootView.findViewById(R.id.tvLocation);
        previewEventHolder.tvShortDescription = (TextView) rootView.findViewById(R.id.tvShortDescription);
        previewEventHolder.tvDateTime=(TextView)    rootView.findViewById(R.id.tvDateTime);
        previewEventHolder.btnSubmit=(Button)    rootView.findViewById(R.id.btnSubmit);
        previewEventHolder.etComments=(EditText)    rootView.findViewById(R.id.etComments);
        previewEventHolder.btnSubmit.setText(userJoined?"J O I N":"L E A V E");
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(frgEventInfo.this).load(uri.toString()).apply(RequestOptions.circleCropTransform()).into(previewEventHolder.ivEventPhoto);
            }
        });
        if (eventDetails.commntList!=null && eventDetails.commntList.size()>0)
        {
            getComments=eventDetails.commntList;
            createListView();
        }
      


        previewEventHolder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userJoinStatus) {
                    eventDetails.UserName.remove(userInfo.user_name);
                }
                else{eventDetails.UserName.add(userInfo.user_name);}
                DatabaseReference databaseReferenceForEvent = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.firebaseDBPath)+"EventDetails/"+UniqueID);
               // StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(eventDetails.Title + "_" + currentYear+"_"+currentMonth+"_"+currentDay);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                //Saving the data for providers only

                databaseReferenceForEvent.setValue(eventDetails);
                progressDialog.hide();
                getActivity().onBackPressed();
                //getFragmentManager().popBackStack();
//                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Log.v("upload done", "upload done");
//                       
//                        
//                       
//                    }
//                });

                Toast.makeText(applicationContext, "Joined Event", Toast.LENGTH_SHORT).show();
            }
        });


        previewEventHolder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eventDetails.commntList==null || eventDetails.commntList.size()==0) {
                    eventDetails.commntList=new ArrayList<Comments>();
                }
                eventDetails.commntList.add(new Comments(userInfo.user_name,previewEventHolder.etComments.getText().toString(),""));
                DatabaseReference databaseReferenceForEvent = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.firebaseDBPath)+"EventDetails/"+UniqueID);
                // StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(eventDetails.Title + "_" + currentYear+"_"+currentMonth+"_"+currentDay);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                //Saving the data for providers only

                databaseReferenceForEvent.setValue(eventDetails);
                progressDialog.hide();
                getActivity().onBackPressed();
                //getFragmentManager().popBackStack();
//                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Log.v("upload done", "upload done");
//
//
//
//                    }
//                });

                Toast.makeText(applicationContext, "Joined Event", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//
//        try {
//            uri = data.getData();
//            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
////            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
////            Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
//            previewEventHolder.ivEventPhoto.setImageBitmap(bitmap);
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    void createListView(){


        CommentsListAdapter commentsListAdapter = new CommentsListAdapter(context,getComments);


        Log.v("getComments",Integer.toString(getComments.size()));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(commentsListAdapter);
    }

}
