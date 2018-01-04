package creatio.com.entrayecto;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Layge on 21/12/2017.
 */

public class HelperClass {
    public static String type = "";
    public static String is_dom = "1";
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }
    public static String formatDecimal(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("$ #,###.00", symbols);
        String prezzo = decimalFormat.format(number);
        return prezzo;
    }
    public static void ShowAlertSwitch(final Context context, final String total, final String id_car){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_switch);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // set the custom dialog components - text, image and button
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        TextView txtMsj = (TextView) dialog.findViewById(R.id.txtMsj);
        txtTitle.setText("Metodo de pago");
        txtMsj.setText("Como quieres tu platillo");
        final Switch sw = dialog.findViewById(R.id.sw);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("Switch State=", ""+b);
                if (b){
                    type = "otro";
                }else{
                    type = "conekta";
                }
            }
        });

        Button btnAceptar = (Button) dialog.findViewById(R.id.btnAceptar);
        final Button btnDom = (Button) dialog.findViewById(R.id.btnDom);
        final Button btnPasar = (Button) dialog.findViewById(R.id.btnPasar);
        Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        btnDom.setBackgroundResource(R.drawable.button_accent);

        btnDom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDom.setBackgroundResource(R.drawable.button_accent);
                btnPasar.setBackgroundResource(R.drawable.button_primary);
                is_dom = "1";
            }
        });
        btnPasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDom.setBackgroundResource(R.drawable.button_primary);
                btnPasar.setBackgroundResource(R.drawable.button_accent);
                is_dom = "0";
            }
        });
        // if button is clicked, close the custom dialog
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw.isChecked()){
                    type = "otro";
                }else{
                    type = "conekta";
                }
                dialog.dismiss();
                Intent intent =  new Intent(context,CardForm.class);
                intent.putExtra("oxxo",false);
                intent.putExtra("updateCard",false);
                intent.putExtra("total", total);
                intent.putExtra("id_car", id_car);
                intent.putExtra("type", type);
                intent.putExtra("is_dom", is_dom);
                context.startActivity(intent);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidName(String name) {

        return true;
    }

    public static boolean isValidPhone(String phone) {
        if (phone != null && phone.length() >= 10) {
            return true;
        }
        return false;
    }

    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }
    public static String FormatDate(String strCurrentDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date newDate = format.parse(strCurrentDate);

            format = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            String date = format.format(newDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void SendNotification(String ID_branch, String title, String msj, String type) {
        AndroidNetworking.post("http://api.entrayecto.com/SendAndroidBranch")
                .addBodyParameter("ID_branch", ID_branch)
                .addBodyParameter("msj", msj)
                .addBodyParameter("title", title)
                .addBodyParameter("type", type)
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {


                Log.e("SendNotification succes", response);


            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("SendNotification error", error.toString());
            }
        });
    }
}
