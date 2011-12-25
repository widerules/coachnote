package sk.mato.kuchy;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;

public class Hrac {
	private int id;
	private ArrayList<Date> dochadzka;
	private int vek;
	private String meno, priezvisko;
	private double respekt;
	private ArrayList<Zapas> Zapasy;
//	private boolean pohlavie;

	public Hrac(int id,String nove_meno, String nove_priezvisko, int novy_vek) {
		dochadzka = new ArrayList<Date>();
		Zapasy = new ArrayList<Zapas>();
		this.setId(id);
		this.setMeno(nove_meno);
		this.setPriezvisko(nove_priezvisko);
		this.setVek(novy_vek);
		this.setRespekt(1);// nazaciatku je respekt 1
	}

	public Hrac(int id, String nove_meno, String nove_priezvisko, int novy_vek,
			double novy_respekt) {
		dochadzka = new ArrayList<Date>();
		Zapasy = new ArrayList<Zapas>();
		this.setId(id);
		this.setMeno(nove_meno);
		this.setPriezvisko(nove_priezvisko);
		this.setVek(novy_vek);
		this.setRespekt(novy_respekt);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String vypis = meno + " " + priezvisko + " " + String.valueOf(respekt)
				+ " " + vek + "\n";
		return vypis;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setVek(int vek) {
		this.vek = vek;
	}

	public int getVek() {
		return vek;
	}

	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}

	public String getPriezvisko() {
		return priezvisko;
	}

	public void setDochdzka(ArrayList<Date> dochadzka) {
		this.dochadzka = dochadzka;
	}

	public ArrayList<Date> getDochadzka() {
		return dochadzka;
	}

	public String vypisDochadzka() {
		String vypis = "";
		for (int i = 0; i < this.dochadzka.size(); i++)
			vypis += DateFormat.getDateInstance().format(this.dochadzka.get(i));
		return vypis;
	}

	public void setMeno(String meno) {
		this.meno = meno;
	}

	public String getMeno() {
		return meno;
	}

	public void setRespekt(double respekt) {
		this.respekt = respekt;
	}

	public double getRespekt() {
		return respekt;
	}

	public void setZapasy(ArrayList<Zapas> Zapasy) {
		this.Zapasy = Zapasy;
	}

	public ArrayList<Zapas> getZapasy() {
		return Zapasy;
	}

	public void pridajTRening(Date datum) {
		this.dochadzka.add(datum);

	}

	public void pridajZapasHracovi(Zapas novy) {
		Zapasy.add(novy);
	}

}
