package dell.example.com.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREF="ChatPref";
    public static final String DISPLAY_NAME="UserName";

    //  Get all the UI elements

    private AutoCompleteTextView myUsername;
    private EditText myEmail;
    private EditText myPassword;
    private EditText myConfirmPassword;

    // Get reference to firebase

    private FirebaseAuth myAuth;

    // Progess Dialog Box

    private ProgressDialog mProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Connect all the UI elements

        myUsername=findViewById(R.id.register_username);
        myEmail=findViewById(R.id.register_email);
        myPassword=findViewById(R.id.register_password);
        myConfirmPassword=findViewById(R.id.register_confirm_password);

        // Get instance of the Firebase

        myAuth=FirebaseAuth.getInstance();

        //  Progess dialog box while registering
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);




    }

    // On tapping signUp this method works

    public void signUp(View view)
    {
        registerUser();
    }

    // Actual Registration happens here

    private void registerUser()
    {
        myEmail.setError(null);
        myPassword.setError(null);
        myConfirmPassword.setError(null);
        myUsername.setError(null);

        // Grab the values

        String email=myEmail.getText().toString();
        String password=myPassword.getText().toString();

        boolean cancel=false;
        View focusView=null;

        // Email validation

        if(TextUtils.isEmpty(email) )
        {
            myEmail.setError(getString(R.string.invalid_email));
            cancel=true;
            focusView=myEmail;
        }else if(!checkEmail(email))
        {
            myEmail.setError(getString(R.string.invalid_email));
            cancel=true;
            focusView=myEmail;
        }

        //  Password validation

        if(TextUtils.isEmpty(password))
        {
            myPassword.setError(getString(R.string.invalid_password));
            focusView=myPassword;
            cancel=true;
        }else if(!checkPassword(password))
        {
            myPassword.setError(getString(R.string.invalid_password));
            focusView=myPassword;
            cancel=true;
        }

        if(cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            createUser();
        }
    }



    // TODO: FOR PRODUCTION APP CREATE MORE STRONG EMAIL AND PASSWORD VALIDATION USING REGEX

    // Email Validation

    private boolean checkEmail(String email)
    {
       return email.contains("@");
    }

    // Validation for Password

    private boolean checkPassword(String password)
    {
        String confPassword=myConfirmPassword.getText().toString();
        return confPassword.equals(password) && password.length()>=6 ;
    }

    //  Create User for the firebase

    private void createUser()
    {

        mProgress.show();
        // Get the values of email and password


        String email=myEmail.getText().toString();
        String password=myPassword.getText().toString();

        // Call method for firebase

        //Toast.makeText(RegisterActivity.this,"Registering...",Toast.LENGTH_SHORT).show();

        myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //  TODO: DELETE BEFORE PRODUCTION
                Log.i("LOOKCCODE","User creation was"+task.isSuccessful());

                if(!task.isSuccessful())
                {
                    showErrorBox("Oops registration failed");
                    mProgress.dismiss();
                }
                else
                {
                    saveUserName();
                    Toast.makeText(RegisterActivity.this,"Registration Successfull",Toast.LENGTH_SHORT).show();

                    // Move user to login screen

                    Intent intent=new Intent(RegisterActivity.this,Login_Activity.class);
                    finish();
                    startActivity(intent);

                }

            }
        });


    }


    //  Use Share Preference to save username

    private void saveUserName()
    {
        String userName=myUsername.getText().toString();
        SharedPreferences pref=getSharedPreferences(CHAT_PREF,0);
        pref.edit().putString(DISPLAY_NAME,userName).apply();
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

