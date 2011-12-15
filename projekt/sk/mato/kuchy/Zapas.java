package sk.mato.kuchy;

public class Zapas {
	private int vysledok;
	private Hrac a,b ;
	private Hrac vytaz;
	//nahradit charom s hodnotami A or B or N

/*	@Override
	public String toString() {
		// TODO Auto-generated method stub
		char v;
		if (this.a==this.vytaz) v='1';
		else v='2';
		return a.getMeno()+" "+a.getPriezvisko()+
		" "+b.getMeno()+" "+b.getPriezvisko()+
		" "+ v +" "+ vysledok +  "\n";
	}
*/
	
	public String prevodDoSuboru(){
		char v;
		if (this.a==this.vytaz) v='1';
		else v='2';
		return a.getMeno()+" "+a.getPriezvisko()+
		" "+b.getMeno()+" "+b.getPriezvisko()+
		" "+ v +" "+ vysledok +  "\n";
	}
	
	public Zapas(Hrac Hrac1, Hrac Hrac2) {
		this.setA(Hrac1);
		this.setB(Hrac2);
		vysledok=-1;
		vytaz= new Hrac("nikto", "nikto", -1);
	}
	
	public Zapas(Hrac Hrac1, Hrac Hrac2, Hrac vytaz, int nvysledok) {
		this.setA(Hrac1);
		this.setB(Hrac2);
		this.setVytaz(vytaz);
		this.setVysledok(nvysledok);
	}

	public void setVysledok(int vysledok) {
		this.vysledok = vysledok;
	}

	public int getVysledok() {
		return vysledok;
	}

	public void setVytaz(Hrac vytaz) {
		this.vytaz = vytaz;
	}

	public Hrac getVytaz() {
		return vytaz;
	}

	public void setB(Hrac b) {
		this.b = b;
	}

	public Hrac getB() {
		return b;
	}

	public void setA(Hrac a) {
		this.a = a;
	}

	public Hrac getA() {
		return a;
	}
}
