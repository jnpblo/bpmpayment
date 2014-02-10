package com.bpm.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bpm.bpmpayment.Cliente;
import com.bpm.bpmpayment.R;

public class ListAdapterClientes extends BaseAdapter{
	private ArrayList<ItemCliente> consumidor = new ArrayList<ItemCliente>();
    private LayoutInflater inflaterClientes;
    
	public ListAdapterClientes(Context contexto, ArrayList<Cliente> clientes) {
		inflaterClientes = LayoutInflater.from(contexto);
		
		for(Cliente persona : clientes) {
			consumidor.add(new ItemCliente(persona.getNombres() + " " + persona.getApellidop(), R.drawable.noimageuser));
		}
	}
	
	public ArrayList<ItemCliente> getConsumidor() {
		return consumidor;
	}
	
	@Override
	public int getCount() {
		return consumidor.size();
	}


	@Override
	public Object getItem(int position) {
		return consumidor.get(position);
	}

	@Override
	public long getItemId(int position) {
		return consumidor.get(position).drawableId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        ImageView picture;
        TextView name;

        if(v == null) {
            v = inflaterClientes.inflate(R.layout.item_layout, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView)v.getTag(R.id.picture);
        name = (TextView)v.getTag(R.id.text);

        ItemCliente item = (ItemCliente)getItem(position);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
	}
	
	public class ItemCliente {
        final String name;
        final int drawableId;

        ItemCliente(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
