package myaplication.tfg.org.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jin on 2015/4/9.
 */
public class FragmentOfertaItemDetail extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.oferta_item_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get the data from the master list
        Bundle bundle = getArguments();
        Product p =(Product)bundle.getSerializable("key");
        TextView title = (TextView)getActivity().findViewById(R.id.detail_title);
        TextView price = (TextView)getActivity().findViewById(R.id.detail_price);
        TextView description = (TextView)getActivity().findViewById(R.id.detail_description);
        ImageView image = (ImageView)getActivity().findViewById(R.id.detail_image);
        String fullprice = "Price: "+p.getPrice();
        title.setText(p.getTitle());
        image.setImageResource(p.getImage());
        description.setText(p.getDescription());
        price.setText(fullprice);

    }
}
