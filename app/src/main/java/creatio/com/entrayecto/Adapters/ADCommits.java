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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import creatio.com.entrayecto.DetalleItem;
import creatio.com.entrayecto.HelperClass;
import creatio.com.entrayecto.ItemsMenu;
import creatio.com.entrayecto.Objects.OCommit;
import creatio.com.entrayecto.Objects.OItemMenu;
import creatio.com.entrayecto.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADCommits extends BaseAdapter {
    Context context;
    ArrayList<OCommit> data = new ArrayList<>();
    DetalleItem activity;


    public ADCommits(Context context, ArrayList<OCommit> data, DetalleItem activity) {
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
        final View item = inflater.inflate(R.layout.ad_commit, viewGroup, false);
        TextView txtName,txtCommit;
        final CircleImageView img;
        RatingBar rtBarSpe;

        txtName = item.findViewById(R.id.txtName);
        txtCommit = item.findViewById(R.id.txtCommit);
        img = item.findViewById(R.id.img);
        rtBarSpe = item.findViewById(R.id.rtBarSpe);

        rtBarSpe.setRating(Float.parseFloat(data.get(i).getRate()));
        Glide.with(context)
                .load(data.get(i).getImage_user())
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
        txtName.setText(data.get(i).getName_user());
        txtCommit.setText(data.get(i).getCommit());
        return item;
    }
}
