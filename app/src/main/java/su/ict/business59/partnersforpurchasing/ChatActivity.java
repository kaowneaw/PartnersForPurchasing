//package su.ict.business59.partnersforpurchasing;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class ChatActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        init();
//    }
//
//    private void init() {
//        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference mUsersRef = mRootRef.child("users");
//        DatabaseReference mMessagesRef = mRootRef.child("messages");
//        mUsersRef.child("id-12345").setValue("Jirawatee");
//    }
//}
