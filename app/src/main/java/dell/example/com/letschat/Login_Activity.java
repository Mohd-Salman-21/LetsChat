package dell.example.com.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    // Reference to Firebase

    FirebaseAuth myAuth;

    // Regernce to UI elements

    EditText myEmail;
    EditText myPassword;

    // Progess dialog box

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  Grab the UI elements

        myEmail=findViewById(R.id.login_email);
        myPassword=findViewById(R.id.login_password);

        // Get the instance of Firebase

        myAuth=FirebaseAuth.getInstance();

        //  Progess dialog box while registering
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


    }

    //  After Sign in button is tapped

    public void signInUser(View view)
    {
        SignInUserToFirebase();
    }

    // SignIn user to the Firebase

    private void SignInUserToFirebase()
    {
        //Dialog box

        mProgress.show();

        String email=myEmail.getText().toString();
        String password=myPassword.getText().toString();

        // TODO: IMPLEMENT A CHECK LIKE IN REGISTER ACTIVITY
        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(this,"Please Fill all the fields",Toast.LENGTH_SHORT).show();
            return;
        }

        //Toast.makeText(Login_Activity.this,"Logging In...",Toast.LENGTH_SHORT).show();

        myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.i("CODEL","Was user logged in "+task.getException());

                if(!task.isSuccessful())
                {
                    showErrorBox("Sign in failed");
                    mProgress.dismiss();
                }else{

                    Intent intent=new Intent(Login_Activity.this,MainChatActivity.class);
                    finish();
                    startActivity(intent);


                }


            }
        });


    }

    //  Move User to register Activity

    public void SignUp(View view)
    {
        Intent intent=new Intent(this,RegisterActivity.class);
        finish();
        startActivity(intent);
    }


    //  Create Dialogbox for errors

    private void showErrorBox(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("HEYYYY")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}

