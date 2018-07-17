package cpp.skywalker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static Context context;
    public static Context contextOfApplication;
    private Bundle arguments = new Bundle();
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    GetUserInfo userInfo=null;
    RecyclerView recyclerView;
   GetEventListInfo [] getEventListInfoArray = null;
    // this is to save the information as an object of the food within 30 miles
    ArrayList<GetEventListInfo> getEventListInfoList = new ArrayList<>();
    FloatingActionButton fabEvent ;//= (FloatingActionButton) findViewById(R.id.fab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        contextOfApplication=getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        final Bundle intent = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userInfo = (GetUserInfo) intent.getSerializable("userInfo");
        DatabaseReference databaseReference;
        StorageReference storageReference;
        StorageReference filepath;
        recyclerView = (RecyclerView) findViewById(R.id.rvEventList);
        fabEvent = (FloatingActionButton) findViewById(R.id.fab);
//--------------------------Load List-----------------------------------------------------
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(getString(R.string.firebaseDBPath)+"EventDetails/");
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //applying jackson object mapper to deserialize
                ObjectMapper mapper = new ObjectMapper();
                String json = "";
                try {
                    json  = "["+mapper.writeValueAsString(dataSnapshot.getValue())+"]";
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                try {
                    Log.v("json:",json);
                    getEventListInfoArray = mapper.readValue(json,GetEventListInfo[].class);
                    getEventListInfoList.add(getEventListInfoArray[0]);
                    createListView();

                }catch (JsonParseException exception){
                    Log.v("error","Json Parser"+exception);
                } catch (JsonMappingException e){
                    Log.v("error","Mapping: "+e);
                }
                catch (IOException e) {
                    Log.v("error","IoException"+e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//--------------------------Load List-----------------------------------------------------

 //--------------------------Fab Event-----------------------------------------------------
        //final FloatingActionButton fabEvent = (FloatingActionButton) findViewById(R.id.fab);
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment event = new frgCreateEvent();
                arguments.putSerializable( "UserInfo" , userInfo);
                //This will set the bundle as an argument to the object
                event.setArguments(arguments);
                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.mainContent, event).commit();
                view.setVisibility(view.GONE);
//                Intent intLogin=new Intent(EventListActivity.this, LoginActivity.class);
//                startActivity(intLogin);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showFloatingActionButton() {
        fabEvent.show();
    }

    public void hideFloatingActionButton() {
        fabEvent.hide();
    }
    void createListView(){


        EventListAdapter eventListAdapter = new EventListAdapter(EventListActivity.this,getEventListInfoList,this.userInfo);


        Log.v("getEventlistCOunt",Integer.toString(getEventListInfoList.size()));
        recyclerView.setLayoutManager(new LinearLayoutManager(EventListActivity.this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(eventListAdapter);
    }
}
