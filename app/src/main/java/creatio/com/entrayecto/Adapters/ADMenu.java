package creatio.com.entrayecto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import creatio.com.entrayecto.ItemsMenu;
import creatio.com.entrayecto.Objects.OMenu;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADMenu extends BaseAdapter {
    Context context;
    ArrayList<OMenu> data = new ArrayList<>();


    public ADMenu(Context context, ArrayList<OMenu> data) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item = inflater.inflate(R.layout.ad_menu, viewGroup, false);
        final TextView txt = item.findViewById(R.id.txt);
        final TextView txtDesc = item.findViewById(R.id.txtDesc);
        txt.setText(data.get(i).getName());
        txtDesc.setText(data.get(i).getDescription());
        final ImageView img = item.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemsMenu.class);
                intent.putExtra("name", data.get(i).getName());
                intent.putExtra("description", data.get(i).getDescription());
                intent.putExtra("image", data.get(i).getImage());
                intent.putExtra("ID_categorie", data.get(i).getID_categorie());
                context.startActivity(intent);
            }
        });
        final LinearLayout ly = item.findViewById(R.id.ly);
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
                                        ly.setBackground(shape);
                                        txt.setTextColor(textSwatch.getTitleTextColor());
                                        txtDesc.setTextColor(textSwatch.getTitleTextColor());
                                    }
                                });
                    }
                });
        return item;
    }
}
