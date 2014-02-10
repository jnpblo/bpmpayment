package com.bpm.adapters;

import java.util.ArrayList;
import com.bpm.bpmpayment.Factura;
import com.bpm.bpmpayment.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapterFacturas extends BaseAdapter{
	private ArrayList<ItemFactura> recivo = new ArrayList<ItemFactura>();
    private LayoutInflater inflaterFacturas;
    
    public ListAdapterFacturas(Context contexto, ArrayList<Factura> facturas) {
    	inflaterFacturas = LayoutInflater.from(contexto);
		
		for(Factura nota : facturas) {
			recivo.add(new ItemFactura(nota.nombre, R.drawable.factura));
		}
	}

	public ArrayList<ItemFactura> getRecivo() {
		return recivo;
	}

	@Override
	public int getCount() {
		return recivo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return recivo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return recivo.get(arg0).drawableId;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
        ImageView picture;
        TextView name;

        if(v == null) {
            v = inflaterFacturas.inflate(R.layout.item_layout, arg2, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView)v.getTag(R.id.picture);
        name = (TextView)v.getTag(R.id.text);

        ItemFactura item = (ItemFactura)getItem(arg0);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
	}

	public class ItemFactura {
        final String name;
        final int drawableId;

        ItemFactura(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
