package cpp.skywalker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import java.sql.Time;
import  java.util.Calendar;
import java.io.IOException;
import java.util.Date;


public class frgCreateEvent extends Fragment {

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

    SimpleDateFormat mddformat = new SimpleDateFormat("dd");
    String currentDay= mddformat.format(calendar.getTime());

    private final static int GALLARY_INTENT = 2;
    Uri uri;
    public class PreviewEventHolder {
        ImageView ivEventPhoto;

        EditText  etAgenda, etLocation,etShortDescription,etEventTitle;//etToTime, etToDate, etFromTime, etFromDate,
        Button btnCreateEvent;
        DatePicker dpFromDate,dpToDate;
        TimePicker tpFromTIme,tpToTime;
    }
    SavedEventDetails savedEventDetails=new SavedEventDetails();
    PreviewEventHolder previewEventHolder = new PreviewEventHolder();

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
       final StorageReference storageReferenceForEvent = FirebaseStorage.getInstance().getReference();
        final View rootView=inflater.inflate(R.layout.fragment_frg_create_event, container, false);
        final Context applicationContext =EventListActivity.getContextOfApplication();
        context=applicationContext;
        //Getting the intent value here
       final Bundle intent = getArguments();


        userInfo = (GetUserInfo) intent.getSerializable("userInfo");

        //Initializing all the variables here

        //-------------------------------------------------------------------------------------------------------------------------------------------//
       progressDialog = new ProgressDialog(applicationContext);

        previewEventHolder.ivEventPhoto = (ImageView)  rootView.findViewById(R.id.ivEventPhoto);
        previewEventHolder.etAgenda = (EditText) rootView.findViewById(R.id.etAgenda);
        previewEventHolder.etEventTitle = (EditText) rootView.findViewById(R.id.etEventTitle);

        previewEventHolder.etLocation = (EditText) rootView.findViewById(R.id.etLocation);
        previewEventHolder.etShortDescription = (EditText) rootView.findViewById(R.id.etShortDescription);
        previewEventHolder.dpFromDate=(DatePicker)    rootView.findViewById(R.id.dpFromDate);
        previewEventHolder.dpToDate=(DatePicker)    rootView.findViewById(R.id.dpFromDate);
        previewEventHolder.tpFromTIme=(TimePicker)    rootView.findViewById(R.id.tpFromTime);
        previewEventHolder.tpToTime=(TimePicker)    rootView.findViewById(R.id.tpToTime);
        previewEventHolder.btnCreateEvent=(Button)    rootView.findViewById(R.id.btnCreateEvent);


        //If user clicks to change the profile photo
        previewEventHolder.ivEventPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,GALLARY_INTENT);


            }
        });


        previewEventHolder.btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Fromday =previewEventHolder.dpFromDate.getDayOfMonth();
                int Frommonth = previewEventHolder.dpFromDate.getMonth() + 1;
                int Fromyear = previewEventHolder.dpFromDate.getYear();

                int Today =previewEventHolder.dpToDate.getDayOfMonth();
                int Tomonth = previewEventHolder.dpToDate.getMonth() + 1;
                int Toyear = previewEventHolder.dpToDate.getYear();

                int fromHH=previewEventHolder.tpFromTIme.getCurrentHour();
                int fromMM=previewEventHolder.tpFromTIme.getCurrentMinute();

                int toHH=previewEventHolder.tpToTime.getCurrentHour();
                int toMM=previewEventHolder.tpToTime.getCurrentMinute();

                 savedEventDetails.FromDate=Fromyear+"/"+Frommonth+"/"+Fromday;
               savedEventDetails.ToDate=Toyear+"/"+Tomonth+"/"+Today;
                savedEventDetails.FromTime=fromHH+":"+fromMM;
                savedEventDetails.ToTime=toHH+":"+toMM;

                savedEventDetails.Title=previewEventHolder.etEventTitle.getText().toString();
                savedEventDetails.Location=previewEventHolder.etLocation.getText().toString();
                savedEventDetails.ShortDescription=previewEventHolder.etShortDescription.getText().toString();
                savedEventDetails.Agenda=previewEventHolder.etAgenda.getText().toString();
                savedEventDetails.HostedBy=userInfo.user_name;

                savedEventDetails.CreatedDate=savedEventDetails.CreatedDate==null || savedEventDetails.CreatedDate=="" ?(currentYear+"/"+currentMonth+"/"+currentDay):savedEventDetails.CreatedDate;
                savedEventDetails.Agenda=previewEventHolder.etAgenda.getText().toString();

                //final StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(savedEventDetails.UniqueID);

//                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        Glide.with(frgCreateEvent.this).load(uri.toString()).apply(RequestOptions.circleCropTransform()).into(previewEventHolder.ivEventPhoto);
//                    }
//                });

                DatabaseReference databaseReferenceForEvent = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.firebaseDBPath)+"EventDetails/");

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                savedEventDetails.UniqueID=userInfo.user_name+"_"+savedEventDetails.Title.trim().replace(" ","") + "_" + currentYear+"_"+currentMonth+"_"+currentDay;
                //Saving the data for providers only
                StorageReference filepath = storageReferenceForEvent.child(userInfo.user_name).child(savedEventDetails.UniqueID);
                databaseReferenceForEvent.child(savedEventDetails.UniqueID).setValue(savedEventDetails);
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.v("upload done", "upload done");
                        progressDialog.hide();
                        Intent intent=new Intent(context,EventListActivity.class);
                        intent.putExtra("userInfo",userInfo);
                        startActivity(intent);

                        //getActivity().onBackPressed();
                        //getFragmentManager().popBackStack();
                    }
                });

                Toast.makeText(applicationContext, "Event Details Saved", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        try {
            uri = data.getData();
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//            Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
           previewEventHolder.ivEventPhoto.setImageBitmap(bitmap);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
