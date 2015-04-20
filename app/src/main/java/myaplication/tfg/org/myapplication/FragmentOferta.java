package myaplication.tfg.org.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jin on 2015/4/9.
 */
public class FragmentOferta extends Fragment {
    private List<Product> products;
    private MyProductAdapter adapter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.oferta_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView oferta = (ListView)getActivity().findViewById(R.id.oferta_list);
        products = new ArrayList<Product>();
        String[] name = new String[]{"VOLCOM SUPERNATURAL INS JACKET ELECTRIC GREEN - 2015", "BURTON WOMAN SOCIETY PANT BLUE-RAY NOVEAU NEON - 2014","BURTON WB PELE MITT KAMANA WANNA LEI YA - 2015","SALOMON ASSASSIN 155 - 2015"};
        String[] price = new String[]{"256.5€", "120.95€","34.97€","84.00€"};
        String[] description = new String[]{"Great Jacket", "Comfortable snowPants","LowPrice, good quality gloves","New model of SnowBoard with incredible price"};
        int[] images = new int[]{R.drawable.jacket,R.drawable.pant2,R.drawable.gloves,R.drawable.board};

        for(int i =0;i<name.length;i++) {
            Product p = new Product();
            p.setTitle(name[i]);
            p.setImage(images[i]);
            p.setPrice(price[i]);
            p.setDescription(description[i]);
            products.add(p);
        }
        adapter1 = new MyProductAdapter(this.getActivity(),products,R.layout.oferta_list);
        oferta.setAdapter(adapter1);
        oferta.setOnItemClickListener(new productDetailClickListener());
    }

    private class productDetailClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            loadProductDetail(position);
        }
    }

    private void loadProductDetail(int position){
        Bundle bundle = new Bundle();
        bundle.putSerializable("key",products.get(position));
        Intent intent = new Intent(getActivity(),IndividualItemInfo.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }
}