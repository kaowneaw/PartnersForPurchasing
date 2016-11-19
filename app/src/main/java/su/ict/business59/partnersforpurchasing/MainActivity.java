package su.ict.business59.partnersforpurchasing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.user)
EditText et;
    @Bind(R.id.pass)
    EditText et2;
    @Bind(R.id.btsignin)
    Button bt1;
    @Bind(R.id.btsignup)
    Button bt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==bt1){
            Toast.makeText(getApplicationContext(),et.getText().toString(),Toast.LENGTH_SHORT).show();
        }else if (view==bt2){
            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),et2.getText().toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
