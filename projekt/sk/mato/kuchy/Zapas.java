package sk.mato.kuchy;

import java.util.Date;

public abstract class Zapas{
	protected int vytaz=0;
	protected int vysledok=0;
	protected Date datum = new Date();
	
	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setVysledok(int vysledok){
		this.vysledok=vysledok;
	}
	
	public int getVysledok(){
		return this.vysledok;
	}
	
	public int getVytaz(){
		return this.vytaz;
	}
	
	public void setVytaz( int vytaz){
		this.vytaz=vytaz;
	}
	
	public abstract String tostring();
	public abstract boolean isDvojHra();
	public abstract boolean isStvorHra();
	//public abstract boolean isMix();
	public abstract boolean isViacHra();
}