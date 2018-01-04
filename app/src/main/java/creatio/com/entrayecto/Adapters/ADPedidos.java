package creatio.com.entrayecto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import creatio.com.entrayecto.FRCar;
import creatio.com.entrayecto.FRPedidos;
import creatio.com.entrayecto.HelperClass;
import creatio.com.entrayecto.Map;
import creatio.com.entrayecto.Objects.OCar;
import creatio.com.entrayecto.Objects.OPEdidos;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADPedidos extends BaseAdapter {
    Context context;
    ArrayList<OPEdidos> data = new ArrayList<>();
    FRPedidos fragment;

    public ADPedidos(Context context, ArrayList<OPEdidos> data, FRPedidos fragment) {
        this.context = context;
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item = inflater.inflate(R.layout.ad_pedidos, viewGroup, false);
        TextView txtTotal,txtOrder,txtFecha,txtName,txtStatus;
        Button btnTrayecto;
        txtOrder = item.findViewById(R.id.txtOrder);
        txtTotal = item.findViewById(R.id.txtTotal);
        txtName = item.findViewById(R.id.txtName);
        txtStatus = item.findViewById(R.id.txtStatus);
        txtFecha = item.findViewById(R.id.txtDate);
        btnTrayecto = item.findViewById(R.id.btnTrayecto);

        btnTrayecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrir mapa
                Intent intent = new Intent(context, Map.class);
                intent.putExtra("location",data.get(i).getLocation());
                intent.putExtra("order",data.get(i).getID());
                intent.putExtra("location_branch", data.get(i).getLocation_branch());
                context.startActivity(intent);
            }
        });
        txtTotal.setText(HelperClass.formatDecimal(Double.parseDouble(data.get(i).getTotal())));
        txtOrder.setText("Orden número: " + data.get(i).getID());
        txtFecha.setText(HelperClass.FormatDate(data.get(i).getCreate_date()));
        txtName.setText(data.get(i).getName_branch());
        if (data.get(i).getIs_dom().equalsIgnoreCase("1")){
            //Es servicio a domicilio
            btnTrayecto.setVisibility(View.VISIBLE);
        }else{
            //Es pasar por el
            btnTrayecto.setVisibility(View.INVISIBLE);
        }

        if (data.get(i).getStatus().equalsIgnoreCase("1")){
            //Creada
            txtStatus.setTextColor( context.getResources().getColor(R.color.alerter_default_success_background));
            txtStatus.setText("Creada");
        }
        if (data.get(i).getStatus().equalsIgnoreCase("2")){
            //En camino
            txtStatus.setTextColor(context.getResources().getColor(R.color.light_blue_500));
            txtStatus.setText("En camino");
        }
        if (data.get(i).getStatus().equalsIgnoreCase("3")){
            //Pagada
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
            txtStatus.setText("Pagada");
        }
        if (data.get(i).getStatus().equalsIgnoreCase("4")){
            //Cancelada
            txtStatus.setTextColor(context.getResources().getColor(R.color.alert_default_error_background));
            txtStatus.setText("Cancelada");
        }
        if (data.get(i).getStatus().equalsIgnoreCase("5")){
            //terminada
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            txtStatus.setText("Terminada");
        }
        return item;
    }
    public void GetPedidosLines(){

    }
}
