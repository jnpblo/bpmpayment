package com.bpm.bpmpayment.parcelable;

import com.bpm.bpmpayment.Cliente;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableCliente implements Parcelable {
	
	private Cliente cliente;

	public ParcelableCliente(Cliente cliente) {
		super();
		this.cliente = cliente;
	}
	
	public ParcelableCliente(Parcel in) {
		cliente = new Cliente();
		cliente.setId_cliente(in.readInt());
		cliente.setApellidop(in.readString());
		cliente.setApellidom(in.readString());
		cliente.setNombres(in.readString());
		cliente.setRazon_social(in.readString());
		cliente.setEmail(in.readString());
		cliente.setCalle_y_num(in.readString());
		cliente.setCiudad(in.readString());
		cliente.setColonia(in.readString());
		cliente.setDelegacion(in.readString());
		cliente.setEstado(in.readString());
		cliente.setCp(in.readString());
		cliente.setPais(in.readString());
		cliente.setRfc(in.readString());
		cliente.setFoto(in.readString());
		cliente.setFoto_pic(in.readInt());
		cliente.setId_usuario(in.readInt());
		cliente.setTelefonos(in.readString());
	}

	public Cliente getCliente() {
		return cliente;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(cliente.getId_cliente());
		dest.writeString(cliente.getApellidop());
		dest.writeString(cliente.getApellidom());
		dest.writeString(cliente.getNombres());
		dest.writeString(cliente.getRazon_social());
		dest.writeString(cliente.getEmail());
		dest.writeString(cliente.getCalle_y_num());
		dest.writeString(cliente.getCiudad());
		dest.writeString(cliente.getColonia());
		dest.writeString(cliente.getDelegacion());
		dest.writeString(cliente.getEstado());
		dest.writeString(cliente.getCp());
		dest.writeString(cliente.getPais());
		dest.writeString(cliente.getRfc());
		dest.writeString(cliente.getFoto());
		dest.writeInt(cliente.getFoto_pic());
		dest.writeInt(cliente.getId_usuario());
		dest.writeString(cliente.getTelefonos());
	}

	public static final Parcelable.Creator<ParcelableCliente> CREATOR =
            new Parcelable.Creator<ParcelableCliente>() {
        public ParcelableCliente createFromParcel(Parcel in) {
            return new ParcelableCliente(in);
        }
 
        public ParcelableCliente[] newArray(int size) {
            return new ParcelableCliente[size];
        }
    };
}
