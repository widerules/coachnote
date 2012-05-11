package sk.mato.kuchy;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

public class Trening {
	private ArrayList<Hrac> hraci = new ArrayList<Hrac>();
	private ArrayList<Zapas> zapasy = new ArrayList<Zapas>();
	private int pocetKurtov = 0;
	private String popisTreningu = new String();
	private Date datumTreningu = new Date();

	public Trening(ArrayList<Hrac> novyHraci, ArrayList<Zapas> noveZapasy,
			int novyPocetKurtov, String novyPopisTreningu,
			Date novyDatumTreningu) {
		
		if (novyDatumTreningu != null)
			this.datumTreningu=novyDatumTreningu;
		
		if (novyPopisTreningu != null)
			this.popisTreningu=novyPopisTreningu;
		
		if (novyHraci != null)
			this.hraci=novyHraci;
		
		if (noveZapasy != null)
			this.zapasy=noveZapasy;
		
		pocetKurtov = novyPocetKurtov;
	}

	public void setHraci(ArrayList<Hrac> Hraci) {
		this.hraci = Hraci;
		for (int i = 0; i < this.hraci.size(); i++) {
			zapisTreningHracovi(Hraci.get(i));
		}
	}

	public ArrayList<Hrac> getHracov() {
		return this.hraci;
	}

	public int getPocetHracov() {
		return this.hraci.size();
	}

	public String vypisHracov() {
		String vypis = "";
		for (int i = 0; i < this.hraci.size(); i++) {
			vypis += i + " : " + this.hraci.get(i).getMeno() + "  "
					+ this.hraci.get(i).getPriezvisko() + "\n";
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
		this.zapasy = Zapasy;
	}


	public ArrayList<Zapas> getZapasy() {
		return zapasy;
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
		Comparator<Hrac> porovnajPodlaRespektu = new Comparator<Hrac>() {
			public int compare(Hrac a, Hrac b) {
				if (a.getRespekt() > b.getRespekt())
					return 1;
				else
					return 0;
			}
		};
		Collections.sort(hraci, porovnajPodlaRespektu);
	}

	public String vypisRebricek() {
		this.vytvorRebricek();
		String vypis = "";
		for (int i = 0; i < hraci.size(); i++) {
			vypis += i + " " + hraci.get(i).getMeno() + " "
					+ hraci.get(i).getPriezvisko() + " "
					+ hraci.get(i).getRespekt() + " " + hraci.get(i).getVek()
					+ " \n";
		}
		return vypis;
	}

	public String vypisZapasy(int i) { 
		// ak je int -1 tak secko inak iba dajaky index
		if ((i>zapasy.size()) || (i<-1)) return null;
		StringBuffer result = new StringBuffer();
		if (i == -1) {
			for (Zapas z : zapasy) {
						if (z.isDvojHra()) result.append("Typ: Dvojhra ");
						if (z.isStvorHra()) result.append("Typ: 4hra ");
						if (z.isViacHra()) result.append("Typ: Viachra ");
						result.append(z.tostring()+'\n');
			}
			return result.toString();
		}
		else {
			if (zapasy.get(i).isDvojHra()) result.append("Typ: Dvojhra ");
			if (zapasy.get(i).isStvorHra()) result.append("Typ: 4hra ");
			if (zapasy.get(i).isViacHra()) result.append("Typ: Viachra ");
			result.append(zapasy.get(i).tostring()+'\n');
		}
		
		return result.toString();
	}
/*
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
*/
	
	public Hrac najdiHracaNaTreningu(String meno, String priezvisko) {
		for (int i = 0; i < hraci.size(); i++)
			if (meno.equals(hraci.get(i).getMeno())
					&& priezvisko.equals(hraci.get(i).getPriezvisko())) {
				return hraci.get(i);
			}
		return null;
	}

	public Hrac najdiHracavDB(ArrayList<Hrac> db, String meno, String priezvisko) {
		for (int i = 0; i < db.size(); i++)
			if (meno.equals(db.get(i).getMeno())
					&& priezvisko.equals(db.get(i).getPriezvisko())) {
				return db.get(i);
			}
		return null;
	}

	public int generujZapasy() {
		/*
		 * toto je uplne random...pridat nieco so zoradenim a respektom...
		 */
		// dam to tak ze bude rozhranie -+ zopar(5) Hracov nad podla rebricka a
		// pod podla...
		//
		Hrac NiktoNevyhralEste = new Hrac(-1, "NiktoNevyhralEste",
				"NiktoNevyhralEste", 47);
		NiktoNevyhralEste.setRespekt(-1);

		Random generator = new Random();
		/*
		 * 
		 * ak su iba dvaja na treningu budu hrat spolu ak je jeden exit
		 */
		if (hraci.size() == 2) {
			zapasy.add(new Dvojhra(hraci.get(0), hraci.get(1)));
			return 1;
		}

		if (hraci.size() < 2)
			return -1;

		if (hraci.size() > 2) {
			int[] hralUz = new int[hraci.size()];
			for (int i = 0; i < hraci.size(); i++) {
				hralUz[i] = 3;
			}
			for (int i = 0; i < (hraci.size() / 2 + 1); i++) {
				this.vytvorRebricek();
				int index = -1;
				boolean ok = true;
				while (ok) {
					int nahodnyIndex = generator.nextInt(4);
					index = i + nahodnyIndex;
					if ((index < hraci.size()) && (index != i))
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
					Zapas novy = new Dvojhra(hraci.get(i), hraci.get(index));
					// System.out.println("index:"+(index+1)+"  i:"+(i+1));
					zapasy.add(novy);
				}
			}

			for (int i = hraci.size() - 1; i > (hraci.size() / 2 - 1); i--) {
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
					Zapas novy = new Dvojhra(hraci.get(i), hraci.get(index));
					zapasy.add(novy);
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
		result.append(this.vypisDatumTreningu() + NEW_LINE + this.vypisHracov()
				+ NEW_LINE + this.vypisRebricek() + NEW_LINE
				+ this.vypisZapasy(-1) + NEW_LINE + this.popisTreningu
				+ NEW_LINE + this.pocetKurtov);
		return result.toString();
	}

	public void zmenHracovText(StringBuffer text, ArrayList<Hrac> DB) {
		// TODO Auto-generated method stub
		String mena = text.toString();
		ArrayList<Hrac> novyHraci = new ArrayList<Hrac>();

		String[] menoPriez = mena.split("\n");
		for (String i : menoPriez) {
			String[] jedenHrac = i.split(" ");
			Hrac novy = najdiHracavDB(DB, jedenHrac[0], jedenHrac[1]);
			if ((novy != null) && (kontrolaDuplicity(novyHraci, novy)))
				novyHraci.add(novy);
		}
		setHraci(novyHraci);
	}

	public void vymazHracovText(StringBuffer text, ArrayList<Hrac> DB) {
		// TODO Auto-generated method stub
		ArrayList<Hrac> novyHraci = new ArrayList<Hrac>();
		novyHraci = this.hraci; // vsetci Hraci co aktualne su na treningu
		String[] menoPriez = text.toString().split("\n");
		for (String i : menoPriez) {
			String[] jedenHrac = i.split(" ");
			Hrac novy = najdiHracavDB(DB, jedenHrac[0], jedenHrac[1]);
			// v podstate zbytocna kontrola ale radcej ak ho nacitam viac krat
			// je to v pohode
			// lebo aj ak nema koho vyhodit vrati ten remove -1
			if (novy != null)
				novyHraci.remove(novy);

		}
		setHraci(novyHraci);
	}

	public void pridajHracaNa(Hrac novyHrac) {
		this.hraci.add(novyHrac);
	}

	private static boolean kontrolaDuplicity(ArrayList<Hrac> novyHraci, Hrac novy) {
		// TODO Auto-generated method stub
		for (Hrac i : novyHraci) {
			if (i.getMeno().equals(novy.getMeno())
					&& i.getPriezvisko().equals(novy.getPriezvisko()))
				return false;
		}
		return true;
	}
}
