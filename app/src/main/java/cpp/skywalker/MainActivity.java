package cpp.skywalker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.oob.SignUp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //login toast
        Button btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Login clicked", Toast.LENGTH_SHORT).show();
                Intent intLogin=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intLogin);
            }
        });
        Button btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Signup clicked", Toast.LENGTH_SHORT).show();
                Intent intSignup=new Intent(MainActivity.this, Signup.class);
                startActivity(intSignup);
            }
        });
    }

}
