package su.ict.business59.partnersforpurchasing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import su.ict.business59.partnersforpurchasing.adapter.ChatMsgAdapter;
import su.ict.business59.partnersforpurchasing.models.Message;
import su.ict.business59.partnersforpurchasing.models.Shop;
import su.ict.business59.partnersforpurchasing.utills.UserPreference;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btn_send)
    ImageButton btn_send;
    @Bind(R.id.rc_chat)
    RecyclerView rc_chat;
    @Bind(R.id.edt_msg)
    EditText edt_msg;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference roomRef;
    private DatabaseReference roomMsgRef;
    private UserPreference pref;
    private Shop currentUser;
    private List<Message> msgList = new ArrayList<>();
    private ChatMsgAdapter adapter;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setTitle("ห้องสนทนา");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = new UserPreference(this);
        currentUser = pref.getUserObject();
        btn_send.setOnClickListener(this);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        String postId = bundle.getString("postId");
        Toast.makeText(getApplicationContext(), postId, Toast.LENGTH_SHORT).show();
        roomRef = mRootRef.child("room");
        roomRef.child("room-" + postId);
        roomMsgRef = roomRef.child("room-" + postId);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new comment has been added, add it to the displayed list
                Message msg = dataSnapshot.getValue(Message.class);
                msgList.add(msg);
                adapter.notifyDataSetChanged();
                rc_chat.scrollToPosition(msgList.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // A comment has changed, use the key
                // to determine if we are displaying this
                // comment and if so displayed the changed comment.
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // A comment has changed, use the key
                // to determine if we are displaying this
                // comment and if so remove it.
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // A comment has changed position,
                // use the key to determine if we are
                // displaying this comment and if so move it.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        roomRef.child("room-" + postId).addChildEventListener(childEventListener);
        adapter = new ChatMsgAdapter(msgList, getApplicationContext());
        rc_chat.setAdapter(adapter);
        rc_chat.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {
        if (view == btn_send) {
            Date date = new Date();
            Message msg = new Message(currentUser.getUser_id(), currentUser.getUsername(), currentUser.getImage_url(), edt_msg.getText().toString(), dateFormat.format(date));
            roomMsgRef.push().setValue(msg);
            edt_msg.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
