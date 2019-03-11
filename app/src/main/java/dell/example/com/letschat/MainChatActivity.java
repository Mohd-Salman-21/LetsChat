package dell.example.com.letschat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {

    private String myUserName;
    private ListView myChatListView;
    private EditText myChatText;
    private ImageButton mySendChatButton;
    private  ChatListAdapter myAdapter;

    private DatabaseReference myDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setUpDisplayName();

        myDatabaseRef=FirebaseDatabase.getInstance().getReference();


       // Get UI elements Refs

        myChatListView=findViewById(R.id.chat_list_view);
        myChatText=findViewById(R.id.messageInput);
        mySendChatButton=findViewById(R.id.sendButton);
        // Push chat to firebase on button tapped

        mySendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatToFirebase();
            }
        });

        myChatText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendChatToFirebase();
                return true;
            }
        });



  }
    // Send Chat to firebase

    private void sendChatToFirebase()
    {
        String chatInput=myChatText.getText().toString();
        if(!chatInput.equals(""))
        {
            InstantMessage chat=new InstantMessage(myUserName,chatInput);
            myDatabaseRef.child("Chats").push().setValue(chat);
            myChatText.setText("");
        }
    }

   // Setting the userName for user

    private void setUpDisplayName()
    {
        SharedPreferences prefs=getSharedPreferences(RegisterActivity.CHAT_PREF,MODE_PRIVATE);
        myUserName=prefs.getString(RegisterActivity.DISPLAY_NAME,null);

        // TODO: In register activity deal with username so that user is not allowed to put field empty
        if(myUserName==null)
        {
            myUserName="User";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter=new ChatListAdapter(this,myDatabaseRef,myUserName);
        myChatListView.setAdapter(myAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.freeUpResources();
    }
}
