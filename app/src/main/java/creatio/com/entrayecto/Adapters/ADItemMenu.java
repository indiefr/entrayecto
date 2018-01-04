package creatio.com.entrayecto.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

import creatio.com.entrayecto.HelperClass;
import creatio.com.entrayecto.ItemsMenu;
import creatio.com.entrayecto.Objects.OItemMenu;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADItemMenu extends BaseAdapter {
    Context context;
    ArrayList<OItemMenu> data = new ArrayList<>();
    ItemsMenu activity;


    public ADItemMenu(Context context, ArrayList<OItemMenu> data, ItemsMenu activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
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
        final View item = inflater.inflate(R.layout.ad_item_menu, viewGroup, false);
        final TextView txt = item.findViewById(R.id.txt);
        final TextView txtDesc = item.findViewById(R.id.txtDesc);
        final TextView txtPrice = item.findViewById(R.id.txtPrice);
        final ImageView img = item.findViewById(R.id.img);

        txt.setText(data.get(i).getName());
        txtDesc.setText(data.get(i).getDescription());
        txtPrice.setText(HelperClass.formatDecimal(Double.parseDouble(data.get(i).getPrice())));

        Glide.with(context)
                .load(data.get(i).getImage())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(new SimpleTarget<Bitmap>(800, 400) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bitmap = resource;
                        img.setImageBitmap(resource);

                    }
                });

        return item;
    }
}
