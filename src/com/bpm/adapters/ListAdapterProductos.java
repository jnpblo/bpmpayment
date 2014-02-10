package com.bpm.adapters;

import java.util.ArrayList;
import com.bpm.bpmpayment.Producto;
import com.bpm.bpmpayment.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterProductos extends BaseAdapter {
	private ArrayList<ItemProducto> articulo = new ArrayList<ItemProducto>();
    private LayoutInflater inflaterProducto;
    
    public ListAdapterProductos(Context contexto, ArrayList<Producto> productos) {
    	inflaterProducto = LayoutInflater.from(contexto);
		
		for(Producto prod : productos) {
			articulo.add(new ItemProducto(prod.nombre, R.drawable.noimageproduct));
		}
	}
	
	public ArrayList<ItemProducto> getArticulo() {
		return articulo;
	}

	@Override
	public int getCount() {
		return articulo.size();
	}

	@Override
	public Object getItem(int position) {
		return articulo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return articulo.get(position).drawableId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        ImageView picture;
        TextView name;

        if(v == null) {
            v = inflaterProducto.inflate(R.layout.item_layout, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView)v.getTag(R.id.picture);
        name = (TextView)v.getTag(R.id.text);

        ItemProducto item = (ItemProducto)getItem(position);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
	}

	public class ItemProducto {
        final String name;
        final int drawableId;

        ItemProducto(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
