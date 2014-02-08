package com.bpm.adapters;

import java.util.ArrayList;
import java.util.List;

import com.bpm.bpmpayment.Cliente;
import com.bpm.bpmpayment.Factura;
import com.bpm.bpmpayment.Producto;
import com.bpm.bpmpayment.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter<T> extends BaseAdapter {
    private List<Item> items = new ArrayList<Item>();
    private LayoutInflater inflater;

	public MyListAdapter(Context context, ArrayList<T> elementos) {
        inflater = LayoutInflater.from(context);
        
        for (T elemento : (ArrayList<T>)elementos) {
        	if(elemento instanceof Cliente) {
        		items.add(new Item(((Cliente) elemento).getNombres() + " " + ((Cliente) elemento).getApellidop(), R.drawable.noimageuser));
        	}
        	
        	if(elemento instanceof Factura) {
        		items.add(new Item(((Factura) elemento).nombre, R.drawable.factura));
        	}
        	
        	if(elemento instanceof Producto) {
        		items.add(new Item(((Producto) elemento).nombre, R.drawable.noimageproduct));
        	}
		}
    }
    
	@Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if(v == null) {
            v = inflater.inflate(R.layout.item_layout, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView)v.getTag(R.id.picture);
        name = (TextView)v.getTag(R.id.text);

        Item item = (Item)getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private class Item {
        final String name;
        final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}