package su.ict.business59.partnersforpurchasing.utills;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import su.ict.business59.partnersforpurchasing.R;

/**
 * Created by kaowneaw on 3/6/2017.
 */

public class AlertInputDialog {

    public AlertInputDialog() {
    }

    private EditText edittext;

    public AlertDialog.Builder getDialog(Fragment fm) {

        AlertDialog.Builder alert = new AlertDialog.Builder(fm.getContext());
        LayoutInflater inflater = fm.getLayoutInflater(fm.getArguments());
        //this is what I did to added the layout to the alert dialog
        View layout = inflater.inflate(R.layout.dialog_input, null);
        alert.setMessage("ใส่จำนวนสินค้าที่คุณต้องการร่วมซื้อ");
        alert.setView(layout);
        this.edittext = (EditText) layout.findViewById(R.id.edtDialog);

        return alert;
    }

    public int getValueAmount() {
        String text = edittext.getText().toString();
        int amount = 0;
        if (!text.equals("")) {

            return Integer.parseInt(text);
        }
        return amount;
    }
}
