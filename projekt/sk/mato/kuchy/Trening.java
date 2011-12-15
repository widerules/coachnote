package sk.mato.kuchy;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.io.*;

public class Trening {
	private ArrayList<Hrac> Hraci;
	private ArrayList<Zapas> Zapasy;
	private int pocetKurtov;
	private String popisTreningu;
	private Date datumTreningu;

	public Trening(ArrayList<Hrac> novyHraci, ArrayList<Zapas> noveZapasy,
			int novy_PocetKurtov, String novyPopisTreningu,
			Date novyDatumTreningu) {
		Hraci = new ArrayList<Hrac>();
		Zapasy = new ArrayList<Zapas>();
		
		if (novyHraci != null) this.setHraci(novyHraci);
		if (novyDatumTreningu != null) this.setDatumTreningu(novyDatumTreningu);
		if (novyPopisTreningu != null) this.setPopisTreningu(novyPopisTreningu);
		
		if (noveZapasy != null) this.setZapasy(noveZapasy);
		this.pocetKurtov = novy_PocetKurtov;
	}

	public void setHraci(ArrayList<Hrac> Hraci) {
		this.Hraci = Hraci;
		for (int i = 0; i < this.Hraci.size(); i++) {
			zapisTreningHracovi(Hraci.get(i));
		}
	}
	
	public ArrayList<Hrac> getHracov() {
		return this.Hraci;
	}
	
	
	public int getPocetHracov() {
		return this.Hraci.size();
	}

	public String vypisHracov() {
		String vypis = "";
		for (int i = 0; i < this.Hraci.size(); i++) {
			vypis += i + " : " + this.Hraci.get(i).getMeno() + "  "
					+ this.Hraci.get(i).getPriezvisko() + "\n";
		}
		return vypis;
	}

	public void setPocetKurtov(int PocetKurtov) {
		this.pocetKurtov = PocetKurtov;
	}

	public int getPocetKurtov() {
		return pocetKurtov;
	}

	public void setZapasy(ArrayList<Zapas> Zapasy) {
		this.Zapasy = Zapasy;
	}

	public ArrayList<Zapas> getZapasy() {
		return Zapasy;
	}

	public void setPopisTreningu(String popisTreningu) {
		this.popisTreningu = popisTreningu;
	}

	public String getPopisTreningu() {
		return popisTreningu;
	}

	public void setDatumTreningu(Date datumTreningu) {
		this.datumTreningu = datumTreningu;
	}

	public Date getDatumTreningu() {
		return datumTreningu;
	}

	public String vypisDatumTreningu() {
		return DateFormat.getDateInstance().format(datumTreningu);
	}

	public void zapisTreningHracovi(Hrac komu) {
		komu.pridajTRening(this.datumTreningu);
	}

	public void vytvorRebricek() {
		Comparator<Hrac> porovnajPodlaRespektu= new Comparator<Hrac>() {
			public int compare( Hrac a, Hrac b )
            {
            if ( a.getRespekt()>b.getRespekt() ) return 1;
            else return 0;
            }
		};
		Collections.sort(Hraci, porovnajPodlaRespektu);
	}

	public String vypisRebricek() {
		this.vytvorRebricek();
		String vypis = "";
		for (int i = 0; i < Hraci.size(); i++) {
			vypis += i + " " + Hraci.get(i).getMeno() + " "
					+ Hraci.get(i).getPriezvisko() + " " + Hraci.get(i).getRespekt()
					+ " " + Hraci.get(i).getVek() + " \n";
		}
		return vypis;
	}


	public String vypisZapasy(int i) { // ak je int -1 tak secko inak iba dajaky
										// index
		String vypis = "";
		if (i == -1) {
			for (i = 0; i < Zapasy.size(); i++) {
				Hrac a = Zapasy.get(i).getA();
				Hrac b = Zapasy.get(i).getB();
				vypis += a.getMeno() + " " + a.getPriezvisko() + "  vs.  "
						+ b.getMeno() + " " + b.getPriezvisko()
						+ " vysledok: \n\n\n";
			}
			return vypis;
		} else {
			Hrac a = Zapasy.get(i).getA();
			Hrac b = Zapasy.get(i).getB();
			vypis += a.getMeno() + " " + a.getPriezvisko() + "  vs.  "
					+ b.getMeno() + " " + b.getPriezvisko() + " vysledok: "
					+ Zapasy.get(i).getVysledok() + "\n";
			return vypis;
		}

	}

	public void nacitajVysledkyZapasov() {
		for (int i = 0; i < Zapasy.size(); i++) {
			System.out.println("Zapas: " + vypisZapasy(i));
			String line = null;
			int kto = 0;
			int vysledok = 0;
			System.out.println("kto vyhral?: 1/2 ");
			try {
				BufferedReader vstup = new BufferedReader(
						new InputStreamReader(System.in));
				line = vstup.readLine();
				kto = Integer.parseInt(line);
				Hrac vytaz = null, porazeny = null;
				if (kto == 1) {
					Zapasy.get(i).setVytaz(Zapasy.get(i).getA());
					vytaz = Zapasy.get(i).getA();
				} else
					porazeny = Zapasy.get(i).getA();

				if (kto == 2) {
					Zapasy.get(i).setVytaz(Zapasy.get(i).getB());
					vytaz = Zapasy.get(i).getB();
				} else
					porazeny = Zapasy.get(i).getB();
				// uprava respektu po odohrani Zapasu
				double konstanta = 0.3;
				double zmena = vytaz.getRespekt() / 0.3;
				vytaz.setRespekt(vytaz.getRespekt() + zmena);
				porazeny.setRespekt(vytaz.getRespekt() / konstanta);
				// koniec upravy
				/* este kontrolu ci napisal iba 1 alebo 2 */
				System.out.println("Na kolko? ");
				line = null;
				line = vstup.readLine();
				vysledok = Integer.parseInt(line);
			} catch (NumberFormatException ex) {
				System.err.println("Not a valid number: ");
			} catch (IOException e) {
				System.err.println("Unexpected IO ERROR: " + e);
			}
			// System.out.println("Vyhral: " + kto+" na: "+ vysledok);
			Zapasy.get(i).setVysledok(vysledok);
			// Zapasy[i].getVytaz();
		}
	}

	public Hrac najdiHracaNaTreningu(String meno, String priezvisko) {
		for (int i = 0; i < Hraci.size(); i++)
			if (meno.equals(Hraci.get(i).getMeno())
					&& priezvisko.equals(Hraci.get(i).getPriezvisko())) {
				return Hraci.get(i);
			}
		return null;
	}
	
	public Hrac najdiHracavDB(ArrayList<Hrac> db ,String meno, String priezvisko) {
		for (int i = 0; i < db.size(); i++)
			if (meno.equals(db.get(i).getMeno()) && priezvisko.equals(db.get(i).getPriezvisko())) {
				return db.get(i);
			}
		return null;
	}

	public int generujZapasy() { 
		/*
		* toto je uplne random...pridat nieco so
		* zoradenim a respektom...
		*/
		// dam to tak ze bude rozhranie -+ zopar(5) Hracov nad podla rebricka a
		// pod podla...
		//
		Hrac NiktoNevyhralEste = new Hrac("NiktoNevyhralEste", "NiktoNevyhralEste", 47);
		NiktoNevyhralEste.setRespekt(-1);
		
		Random generator = new Random();
		/*
		 * 
		 * ak su iba dvaja na treningu budu hrat spolu
		 * ak je jeden exit
		 * 
		 * */
		if (Hraci.size()==2) {
			Zapasy.add( new Zapas(Hraci.get(0), Hraci.get(1)));
			return 1;
		} 
		
		if (Hraci.size()<2) return -1;
		
		
		if (Hraci.size()>2) {
		int[] hralUz = new int[Hraci.size()];
		for (int i = 0; i < Hraci.size(); i++) {
			hralUz[i] = 3;
		}
		for (int i = 0; i < (Hraci.size() / 2 + 1); i++) {
			this.vytvorRebricek();
			int index = -1;
			boolean ok = true;
			while (ok) {
				int nahodnyIndex = generator.nextInt(4);
				index = i + nahodnyIndex;
				if ((index < Hraci.size()) && (index != i))
					ok = false;
				else
					ok = true;
				// System.out.println("index:"+index+"  i:"+i);
			}
			ok = true;
			// System.out.println("i:"+i);
			hralUz[i]--;
			hralUz[index]--;
			// System.out.println("hodnoty"+hralUz[i]+ " + "+hralUz[index]);
			if (hralUz[i] > 0 && hralUz[index] > 0) {
				Zapas novy = new Zapas(Hraci.get(i), Hraci.get(index));
				// System.out.println("index:"+(index+1)+"  i:"+(i+1));
				novy.setVytaz(NiktoNevyhralEste); ///este neodohraty Zapas
				Zapasy.add(novy);
			}
		}

		for (int i = Hraci.size() - 1; i > (Hraci.size() / 2 - 1); i--) {
			this.vytvorRebricek();
			int index = -1;
			boolean ok = true;
			while (ok) {
				int nahodnyIndex = generator.nextInt(4);
				index = i - nahodnyIndex;
				if ((index > -1) && (index != i))
					ok = false;
				else
					ok = true;

			}
			ok = true;
			// System.out.println("i:"+i);
			hralUz[i]--;
			hralUz[index]--;
			// System.out.println("hodnoty"+hralUz[i]+ " + "+hralUz[index]);
			if (hralUz[i] > 0 && hralUz[index] > 0) {
				// System.out.println("index:"+(index+1)+"  i:"+(i+1));
				Zapas novy = new Zapas(Hraci.get(i), Hraci.get(index));
				novy.setVytaz(NiktoNevyhralEste); ///este neodohraty Zapas
				Zapasy.add(novy);
			}
		}
		return 0;
		}
		return 0;
	}

	public static void ulozDoXml(Trening[] trening) {
			}

	public static ArrayList<Trening> nacitajZoXML() {
		return null;
			}
	
	
	public String vypis() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		String NEW_LINE = System.getProperty("line.separator");
		result.append(this.vypisDatumTreningu()+ NEW_LINE + this.vypisHracov() +NEW_LINE+ this.vypisRebricek()+NEW_LINE + this.vypisZapasy(-1)+NEW_LINE+ this.popisTreningu + NEW_LINE+ this.pocetKurtov );
		return result.toString();
	}

	public void pridajHracovText(StringBuffer text,  ArrayList<Hrac> DB) {
		// TODO Auto-generated method stub
		String mena=text.toString();
		ArrayList<Hrac> novyHraci = new ArrayList<Hrac>();
		novyHraci= this.Hraci;
		
		String[] menoPriez= mena.split("\n"); 
		for (String i: menoPriez) {
			String[] jedenHrac= i.split(" ");
			Hrac novy = najdiHracavDB(DB, jedenHrac[0] , jedenHrac[1]);
			if ( (novy !=null) && (kontrolaDuplicity(novyHraci, novy)) ) novyHraci.add(novy);			 						
		}
		setHraci(novyHraci);
}

	public void vymazHracovText(StringBuffer text, ArrayList<Hrac> DB) {
		// TODO Auto-generated method stub
		ArrayList<Hrac> novyHraci = new ArrayList<Hrac>();
		novyHraci= this.Hraci; //vsetci Hraci co aktualne su na treningu
		String[] menoPriez= text.toString().split("\n");
		for (String i: menoPriez) {
			String[] jedenHrac= i.split(" ");
			Hrac novy = najdiHracavDB(DB, jedenHrac[0] , jedenHrac[1]);
			//v podstate zbytocna kontrola ale radcej ak ho nacitam viac krat je to v pohode
			//lebo aj ak nema koho vyhodit vrati ten remove -1
			if (novy !=null) novyHraci.remove(novy);
			
			
		}
		setHraci(novyHraci);
	}
	
	public void pridajHracaNa(Hrac novyHrac) {
		this.Hraci.add(novyHrac);
	}

	private boolean kontrolaDuplicity(ArrayList<Hrac> novyHraci, Hrac novy) {
		// TODO Auto-generated method stub
		for (Hrac i : novyHraci) {
			if (i.getMeno().equals(novy.getMeno()) && i.getPriezvisko().equals(novy.getPriezvisko()) ) return false;
		}
		return true;
	}	  
}
