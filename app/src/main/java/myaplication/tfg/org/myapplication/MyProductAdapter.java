package myaplication.tfg.org.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

/**
 * Created by jin on 2015/4/15.
 */
public class MyProductAdapter extends BaseAdapter {
    Context context;
    private List<ProductConfigurable> productConfigurables;
    private int resource;

    @SuppressWarnings("static-access")
    public MyProductAdapter(Context context,List<ProductConfigurable> productConfigurables, int resource) {
        this.productConfigurables = productConfigurables;
        this.resource = resource;
        this.context=context;

    }
    static class ViewHolder{
        TextView title;
        TextView price;
        ImageView image;
        ImageView icon;
    }
    @Override
    public int getCount() {
        return productConfigurables.size();
    }

    @Override
    public Object getItem(int position) {
        return productConfigurables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView==null){
            convertView=View.inflate(context, resource, null);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.price = (TextView)convertView.findViewById(R.id.Price);
            viewHolder.icon =(ImageView)convertView.findViewById(R.id.next3);
            viewHolder.image =(ImageView)convertView.findViewById(R.id.thumbnail);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProductConfigurable p = productConfigurables.get(position);
        if(p != null){
        viewHolder.title.setText(p.getTitle());
        UrlImageViewHelper.setUrlDrawable(viewHolder.image, "http://mininegocio.es/media/catalog/product/android/gf_1.png");
       // image.setImageResource(R.drawable.jacket);
        viewHolder.icon.setImageResource(R.drawable.ic_action_next_item_dark);
        viewHolder.price.setText(p.getPrice());
        }

        return convertView;
    }
}
