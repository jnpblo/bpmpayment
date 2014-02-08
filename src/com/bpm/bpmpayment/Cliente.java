package com.bpm.bpmpayment;

public class Cliente {
	private int id_cliente;
	private String apellidop;
	private String apellidom;
	private String nombres;
	private String razon_social;
	private String email;
	private String calle_y_num;
	private String ciudad;
	private String colonia;
	private String delegacion;
	private String estado;
	private String cp;
	private String pais;
	private String rfc;
	private String foto;
	private int foto_pic;
	private int id_usuario;
	private String telefonos;
	
	public Cliente() {
		
	}
	
	public Cliente(int pic, int id_cliente, String apellidop,
			String apellidom, String nombres, String razon_social,
			String email, String calle_y_num, String ciudad, String colonia,
			String delegacion, String estado, String cp, String pais,
			String rfc, String foto, int id_usuario, String telefonos) {
		this.foto_pic = pic;
		this.id_cliente = id_cliente;
		this.apellidop = apellidop;
		this.apellidom = apellidom;
		this.nombres = nombres;
		this.razon_social = razon_social;
		this.email = email;
		this.calle_y_num = calle_y_num;
		this.ciudad = ciudad;
		this.colonia = colonia;
		this.delegacion = delegacion;
		this.estado = estado;
		this.cp = cp;
		this.pais = pais;
		this.rfc = rfc;
		this.foto = foto;
		this.id_usuario = id_usuario;
		this.telefonos = telefonos;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getApellidop() {
		return apellidop;
	}

	public void setApellidop(String apellidop) {
		this.apellidop = apellidop;
	}

	public String getApellidom() {
		return apellidom;
	}

	public void setApellidom(String apellidom) {
		this.apellidom = apellidom;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getRazon_social() {
		return razon_social;
	}

	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCalle_y_num() {
		return calle_y_num;
	}

	public void setCalle_y_num(String calle_y_num) {
		this.calle_y_num = calle_y_num;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getDelegacion() {
		return delegacion;
	}

	public void setDelegacion(String delegacion) {
		this.delegacion = delegacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getFoto_pic() {
		return foto_pic;
	}

	public void setFoto_pic(int foto_pic) {
		this.foto_pic = foto_pic;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(String telefonos) {
		this.telefonos = telefonos;
	}
}