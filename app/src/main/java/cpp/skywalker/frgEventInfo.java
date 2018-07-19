package cpp.skywalker;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
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
import java.util.HashMap;


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
    int Position;
    ArrayList<Comments> getComments = new ArrayList<>();
    private final static int GALLARY_INTENT = 2;
    Uri uri;
    public class PreviewEventHolder {
        ImageView ivEventPhoto;
        TextView tvEventTitle, tvShortDescription,tvLocation,tvAgenda,tvDateTime;
        Button btnSubmit,btnComment;
        EditText etComments;
        RecyclerView rvComments;


    }

    //EventDetails eventDetails;
    SavedEventDetails savedEventDetails=new SavedEventDetails();
    GetEventListInfo getEventListInfo;
    PreviewEventHolder previewEventHolder = new PreviewEventHolder();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Initializing all the variables here
//        final StorageReference storageReferenceForEvent = FirebaseStorage.getInstance().getReference();
//        final StorageReference filepath = storageReferenceForEvent.child(savedEventDetails.HostedBy).child(savedEventDetails.UniqueID);

//        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                Glide.with(frgEventInfo.this).load(uri.toString()).into(previewEventHolder.ivEventPhoto);
//            }
//        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Intent int1=new Intent(context,EventListActivity.class);
        int1.putExtra("userInfo",userInfo);
        startActivity(int1);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        ((EventListActivity) getActivity()).hideFloatingActionButton();
        final StorageReference storageReferenceForEvent = FirebaseStorage.getInstance().getReference();
        final View rootView=inflater.inflate(R.layout.fragment_frg_event_info, container, false);
        final Context applicationContext =EventListActivity.getContextOfApplication();
        context=applicationContext;
        //Getting the intent value here
        final Bundle intent = getArguments();
        boolean userJoined=false;
        UniqueID =intent.getString("UniqueID");
        Position=Integer.parseInt(intent.getString("Position"));
        userInfo = (GetUserInfo) intent.getSerializable("userInfo");
        getEventListInfo = (GetEventListInfo) intent.getSerializable("EventDetails");


        savedEventDetails.Members=getEventListInfo.Members;
        savedEventDetails.Agenda=getEventListInfo.Agenda;
        savedEventDetails.UniqueID=getEventListInfo.UniqueID;
        savedEventDetails.Title=getEventListInfo.Title;
        savedEventDetails.HostedBy=getEventListInfo.HostedBy;
        savedEventDetails.ToTime=getEventListInfo.ToTime;
        savedEventDetails.FromTime=getEventListInfo.FromTime;
        savedEventDetails.FromDate=getEventListInfo.FromDate;
        savedEventDetails.ToDate=getEventListInfo.ToDate;
        savedEventDetails.ShortDescription=getEventListInfo.ShortDescription;
        savedEventDetails.Location=getEventListInfo.Location;
        savedEventDetails.CreatedDate=getEventListInfo.CreatedDate;
        savedEventDetails.CommentList=getEventListInfo.CommentList;
        savedEventDetails.ImgUrl=getEventListInfo.ImgUrl;



        //-------------------------------------------------------------------------------------------------------------------------------------------//
        progressDialog = new ProgressDialog(applicationContext);

        previewEventHolder.ivEventPhoto = (ImageView)  rootView.findViewById(R.id.ivEventPhoto);
        previewEventHolder.tvAgenda = (TextView) rootView.findViewById(R.id.tvAgenda);
        previewEventHolder.tvEventTitle = (TextView) rootView.findViewById(R.id.tvEventTitle);

        previewEventHolder.tvLocation = (TextView) rootView.findViewById(R.id.tvLocation);
        previewEventHolder.tvShortDescription = (TextView) rootView.findViewById(R.id.tvShortDescription);
        previewEventHolder.tvDateTime=(TextView)rootView.findViewById(R.id.tvDateTime);
        previewEventHolder.btnSubmit=(Button)rootView.findViewById(R.id.btnSubmit);
        previewEventHolder.etComments=(EditText)rootView.findViewById(R.id.etComments);
        previewEventHolder.btnComment=(Button)rootView.findViewById(R.id.btnComment);
        previewEventHolder.rvComments=(RecyclerView) rootView.findViewById(R.id.rvComments);
        recyclerView=previewEventHolder.rvComments;
        previewEventHolder.btnSubmit.setText(!userJoined?"J O I N":"L E A V E");
        previewEventHolder.tvEventTitle.setText(savedEventDetails.Title);
        previewEventHolder.tvDateTime.setText(savedEventDetails.FromDate+"-"+savedEventDetails.ToDate);
        previewEventHolder.tvShortDescription.setText(savedEventDetails.ShortDescription);
        previewEventHolder.tvLocation.setText(savedEventDetails.Location);
        previewEventHolder.tvAgenda.setText(savedEventDetails.Agenda);
        previewEventHolder.etComments.setVisibility(View.GONE);
        previewEventHolder.btnComment.setVisibility(View.GONE);

//        Log.v("Check if user",""+(savedEventDetails.HostedBy==userInfo.user_name));
//        Log.v("Members not null",""+(savedEventDetails.Members!=null));
//        Log.v("member size count",""+(savedEventDetails.Members.size()>0));
if(userInfo!=null && savedEventDetails.HostedBy.equals(userInfo.user_name))
{
    previewEventHolder.btnSubmit.setVisibility(View.GONE);
}
        if((!(savedEventDetails.HostedBy==userInfo.user_name)) && savedEventDetails.Members!=null && savedEventDetails.Members.size()>0) {
            for (String str : savedEventDetails.Members) {
                if (str.equals(userInfo.user_name)) {

                    userJoined = true;
                    previewEventHolder.etComments.setVisibility(View.VISIBLE);
                    previewEventHolder.btnComment.setVisibility(View.VISIBLE);
                    previewEventHolder.btnSubmit.setText("L E A V E");
                    break;
                }

            }
        }
        final boolean userJoinStatus=userJoined;

        if (savedEventDetails.CommentList!=null && savedEventDetails.CommentList.size()>0)
        {
            getComments=savedEventDetails.CommentList;
            createListView(applicationContext);
        }


        final StorageReference filepath = storageReferenceForEvent.child(savedEventDetails.HostedBy).child(savedEventDetails.UniqueID);
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                if(frgEventInfo.this!=null && frgEventInfo.this.isAdded()) {
                    Glide.with(frgEventInfo.this).load(uri.toString()).into(previewEventHolder.ivEventPhoto);
                }
            }
        });


        previewEventHolder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedEventDetails.Members= savedEventDetails.Members==null?new ArrayList<String>(): savedEventDetails.Members;
                if (userJoinStatus) {
                    savedEventDetails.Members.remove(userInfo.user_name);
                    previewEventHolder.etComments.setVisibility(View.GONE);
                    previewEventHolder.btnComment.setVisibility(View.GONE);
                    previewEventHolder.btnSubmit.setText("J O I N");

                }
                else{
                    savedEventDetails.Members.add(userInfo.user_name);
                    previewEventHolder.etComments.setVisibility(View.VISIBLE);
                    previewEventHolder.btnComment.setVisibility(View.VISIBLE);
                    previewEventHolder.btnSubmit.setText("L E A V E");
                }
                DatabaseReference databaseReferenceForEvent = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.firebaseDBPath)+"EventDetails/"+UniqueID);
               // StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(eventDetails.Title + "_" + currentYear+"_"+currentMonth+"_"+currentDay);
               progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                //Saving the data for providers only
                HashMap<String,Object> updateMembers=new HashMap<>();
                updateMembers.put("Members",savedEventDetails.Members);
                databaseReferenceForEvent.updateChildren(updateMembers);

                // databaseReferenceForEvent.child(UniqueID).setValue(savedEventDetails);
                progressDialog.hide();
               // getActivity().onBackPressed();
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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frgEventInfo.this).attach(frgEventInfo.this).commit();
            String msg=userJoinStatus?"Left the event":"Joined the event.Congrats!";
                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show();
            }
        });


        previewEventHolder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (savedEventDetails.CommentList==null || savedEventDetails.CommentList.size()==0) {
                    savedEventDetails.CommentList=new ArrayList<Comments>();
                }
                savedEventDetails.CommentList.add(new Comments(userInfo.user_name,previewEventHolder.etComments.getText().toString(),""));
                DatabaseReference databaseReferenceForEvent = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.firebaseDBPath)+"EventDetails/"+UniqueID);
                // StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(eventDetails.Title + "_" + currentYear+"_"+currentMonth+"_"+currentDay);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                //Saving the data for providers only
                HashMap<String,Object> updateMembers=new HashMap<>();
                updateMembers.put("CommentList",savedEventDetails.CommentList);
                databaseReferenceForEvent.updateChildren(updateMembers);
                previewEventHolder.etComments.setText("");
                progressDialog.hide();
                // Reload current fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frgEventInfo.this).attach(frgEventInfo.this).commit();
//                frgEventInfo.this.notify();
               // databaseReferenceForEvent.child(UniqueID).setValue(savedEventDetails);

               // getActivity().onBackPressed();
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

               // Toast.makeText(applicationContext, "Commented", Toast.LENGTH_SHORT).show();
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

    void createListView(Context cont){


        CommentsListAdapter commentsListAdapter = new CommentsListAdapter(cont,getComments);


        Log.v("getComments",Integer.toString(getComments.size()));
        recyclerView.setLayoutManager(new LinearLayoutManager(cont));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(commentsListAdapter);
    }

}
