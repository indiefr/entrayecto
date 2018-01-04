package creatio.com.entrayecto.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import creatio.com.entrayecto.Objects.OLists;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADLists extends BaseAdapter {
    Context context;
    ArrayList<OLists> data = new ArrayList<>();

    public ADLists(Context context, ArrayList<OLists> data) {
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
        final View item = inflater.inflate(R.layout.ad_lists, viewGroup, false);
        TextView txtName = item.findViewById(R.id.txtName);
        final TextView txtSucursal = item.findViewById(R.id.txtSucursal);
        txtName.setText(data.get(i).getName());
        txtSucursal.setText(data.get(i).getName_branch());
        final ImageView img = item.findViewById(R.id.img);
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
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                        GradientDrawable shape = new GradientDrawable();
                                        shape.setCornerRadius(150);

                                        if (textSwatch == null) {
                                            Toast.makeText(context, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        int def = 0x303030;
                                        shape.setColor(palette.getVibrantColor(def));
                                        txtSucursal.setBackground(shape);
                                        txtSucursal.setTextColor(textSwatch.getTitleTextColor());
                                    }
                                });
                    }
                });
        return item;
    }
}
