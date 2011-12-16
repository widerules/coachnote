package sk.mato.kuchy;

public class Zapas {
	private int vysledok;
	private Hrac a, b;
	// private Hrac vytaz;
	private int vytaz; // 0 || 1|| 2

	public String prevodDoSuboru() {

		return a.getMeno() + " " + a.getPriezvisko() + " " + b.getMeno() + " "
				+ b.getPriezvisko() + " " + vytaz + " " + vysledok + "\n";
	}

	public Zapas(Hrac Hrac1, Hrac Hrac2) {
		this.setA(Hrac1);
		this.setB(Hrac2);
		vysledok = -1;
		vytaz = 0;
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
		if (this.a == vytaz)
			this.vytaz = 1;
		else if (this.b == vytaz)
			this.vytaz = 2;
		else
			this.vytaz = 0;

	}

	public Hrac getVytaz() {
		if (this.vytaz==1) return this.a;
		if (this.vytaz==2) return this.b;
		return null;
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
