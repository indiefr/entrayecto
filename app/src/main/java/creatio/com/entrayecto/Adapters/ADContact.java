package creatio.com.entrayecto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import creatio.com.entrayecto.Map;
import creatio.com.entrayecto.Objects.OContact;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADContact extends BaseAdapter {
    Context context;
    ArrayList<OContact> data = new ArrayList<>();

    public ADContact(Context context, ArrayList<OContact> data) {
        this.context = context;
        this.data = data;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item = inflater.inflate(R.layout.ad_contact, viewGroup, false);
        TextView txtNameBranch,txtName,txtEmail,txtTel,txtAddress,txtHorario;
        txtNameBranch = item.findViewById(R.id.txtNameBranch);
        txtName = item.findViewById(R.id.txtName);
        txtEmail = item.findViewById(R.id.txtEmail);
        txtTel = item.findViewById(R.id.txtTel);
        txtAddress = item.findViewById(R.id.txtAddress);
        txtHorario = item.findViewById(R.id.txtHorario);

        txtNameBranch.setText(data.get(i).getName_branch());
        txtName.setText(data.get(i).getName());
        txtEmail.setText(data.get(i).getEmail());
        txtTel.setText(data.get(i).getTel());
        txtAddress.setText(data.get(i).getAddress());
        txtHorario.setText(data.get(i).getHorario());
        ImageButton btnServ = item.findViewById(R.id.btnServ);
        if (data.get(i).getService_out().equalsIgnoreCase("1")){
            btnServ.setVisibility(View.VISIBLE);
        }else{
            btnServ.setVisibility(View.GONE);
        }
        return item;
    }
}
