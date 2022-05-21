package br.com.sp.senai.auditorio.auditorio.model;



public enum Periodo {
	MANHA("manha"), TARDE("tarde"), NOITE("noite"), TODOS("todos");
	
	String periodo; 
	Periodo(String string) {
		this.periodo = string; 
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
