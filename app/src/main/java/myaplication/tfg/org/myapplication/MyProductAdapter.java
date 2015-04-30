package myaplication.tfg.org.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        if(convertView==null){
            convertView=View.inflate(context, resource, null);
        }
        ProductConfigurable p = productConfigurables.get(position);
        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView price = (TextView)convertView.findViewById(R.id.Price);
        ImageView image = (ImageView)convertView.findViewById(R.id.thumbnail);
        ImageView icon =(ImageView)convertView.findViewById(R.id.next3);
        title.setText(p.getTitle());
        image.setImageResource(p.getImage());
        icon.setImageResource(R.drawable.ic_action_next_item_dark);
        price.setText(p.getPrice());
        return convertView;
    }
}
