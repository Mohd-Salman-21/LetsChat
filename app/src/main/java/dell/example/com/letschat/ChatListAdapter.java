package dell.example.com.letschat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity myActivity;
    private String myUserName;
    private DatabaseReference myDatabaseRef;
    private ArrayList<DataSnapshot> mySnapShot;

    // Child event listener

    private ChildEventListener myListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mySnapShot.add(dataSnapshot);
            notifyDataSetChanged();
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
    };


    // Constructor goes here

    public ChatListAdapter(Activity activity,DatabaseReference reference,String username)
    {
        myActivity=activity;
        myDatabaseRef=reference.child("Chats");
        myUserName=username;
        mySnapShot=new ArrayList<>();

        // Add child event listener

        myDatabaseRef.addChildEventListener(myListener);

    }

    // static class

    public class ViewHolder
    {
        TextView senderName;
        TextView chatBody;
        LinearLayout.LayoutParams layoutParams;

    }



    @Override
    public int getCount() {
        return mySnapShot.size();
    }

    @Override
    public InstantMessage getItem(int i) {

        DataSnapshot snapshot=mySnapShot.get(i);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Can be two situation , 1: there can be no view or chat previously , 2: There might be previous chat
        //                                                                         and u want to continue
        if(view==null)
        {
            // Getting rid of NUll POINTER Exception if occurs : Production way to deal
            LayoutInflater inflater=(LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.chat_msg_row,viewGroup,false);

            final ViewHolder holder=new ViewHolder();
            holder.senderName=view.findViewById(R.id.author);
            holder.chatBody=view.findViewById(R.id.message);
            holder.layoutParams=(LinearLayout.LayoutParams) holder.senderName.getLayoutParams();

            view.setTag(holder);

        }

        final InstantMessage message=getItem(i);
        final ViewHolder holder=(ViewHolder) view.getTag();

        // Styling for making users left and right on chat screen

        boolean isME=message.getAuthor().equals(myUserName);

        // Call a function for syling
        chatRowStyling(isME,holder);


        String author=message.getAuthor();
        holder.senderName.setText(author);

        String msg=message.getMessage();
        holder.chatBody.setText(msg);


        return view;
    }


    private void chatRowStyling(boolean isItMe,ViewHolder holder)
    {
        if(isItMe){
            holder.layoutParams.gravity=Gravity.END;
            holder.senderName.setTextColor(Color.BLUE);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_green);
        }else
        {
            holder.layoutParams.gravity=Gravity.START;
            holder.senderName.setTextColor(Color.GREEN);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_orange);
        }

        holder.senderName.setLayoutParams(holder.layoutParams);
        holder.chatBody.setLayoutParams(holder.layoutParams);

    }

    // This is memory related thing for the performance of your application so that your application dont be laggy

    public void freeUpResources()
    {
        myDatabaseRef.removeEventListener(myListener);
    }


}
