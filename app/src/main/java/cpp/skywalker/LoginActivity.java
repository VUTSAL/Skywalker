package cpp.skywalker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.oob.SignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private class LoginHolder {
        EditText etUserName;
        EditText etPassword;
        Button btnLogin;

        TextView forgotPasswordSeeker;
    }

    String userName = null, password = null;
    LoginActivity context = this;
    GetUserInfo userInfo[] = null;

    private boolean flag = false;
    final LoginHolder loginHolder = new LoginHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.firebaseDBPath)+ "userInfo/");
        loginHolder.etUserName = (EditText) findViewById(R.id.etUserName);
        loginHolder.etPassword = (EditText) findViewById(R.id.etPassword);
        loginHolder.btnLogin = (Button) findViewById(R.id.btnLogin);

        loginHolder.forgotPasswordSeeker = (TextView) findViewById(R.id.forgotPasswordSeeker);

        loginHolder.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = String.valueOf(loginHolder.etUserName.getText());
                password = String.valueOf(loginHolder.etPassword.getText());
                if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                } else {

                    signIn(userName, password,databaseReference);
                }
            }

        });

        loginHolder.forgotPasswordSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(this, ForgotPassword.class);
//                //intent.putExtra("type","Seeker");
//                startActivity(intent);

            }
        });

//        loginHolder.signUpSeeker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, SignUp.class);
//                intent.putExtra("type", "seeker");
//                startActivity(intent);
//
//
//            }
//        });

//        Button btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(MainActivity.this, "Login clicked", Toast.LENGTH_SHORT).show();
//                Intent intLogin = new Intent(LoginActivity.this, EventListActivity.class);
//                startActivity(intLogin);
//            }
//        });


    }

    private void signIn(final String uname, final String pwd,DatabaseReference databaseReference ) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uname).exists()) {
                    if (!uname.isEmpty()) {
                        Log.v("Exists:", "" + dataSnapshot.child(uname).getValue());
                        GetUserInfo userInfo = dataSnapshot.child(uname).getValue(GetUserInfo.class);
                        if (userInfo.getPassWord().equals(pwd)) {
                            Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Username is not registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

}
