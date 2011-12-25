package sk.mato.kuchy;

import java.util.ArrayList;

/*nie nutne riesi mix, 4hru aj 3hru napriklad*/

public class ViacHra extends Zapas {
	private ArrayList<Hrac> teamA = new ArrayList<Hrac>();
	private ArrayList<Hrac> teamB = new ArrayList<Hrac>();

	public ArrayList<Hrac> getTeamA() {
		return teamA;
	}

	public void setTeamA(ArrayList<Hrac> teamA) {
		this.teamA = teamA;
	}

	public ArrayList<Hrac> getTeamB() {
		return teamB;
	}

	public void setTeamB(ArrayList<Hrac> teamB) {
		this.teamB = teamB;
	}

	public ViacHra(ArrayList<Hrac> teamA, ArrayList<Hrac> teamB) {
		this.teamA = teamA;
		this.teamB = teamB;
		this.setVytaz(0);
		this.setVysledok(0);
	}

	public ViacHra(ArrayList<Hrac> teamA, ArrayList<Hrac> teamB, int vytaz,
			int vysledok) {
		this.teamA = teamA;
		this.teamB = teamB;
		this.setVytaz(vytaz);
		this.setVysledok(vysledok);
	}

	public void setVytaz(ArrayList<Hrac> vytaz) {
		if (this.teamA == vytaz)
			this.vytaz = 1;
		else if (this.teamB == vytaz)
			this.vytaz = 2;
		else
			this.vytaz = 0;
	}

	@Override
	public String tostring() {
		// TODO Auto-generated method stub
		StringBuffer r = new StringBuffer();
		for (Hrac  i : teamA) {
			r.append(i.getMeno()+" "+i.getPriezvisko()+", ");
		}
		r.append("vs.");
		for (Hrac  i : teamB) {
			r.append(i.getMeno()+" "+i.getPriezvisko()+", ");
		}
		if ((this.vysledok==0) && (this.vytaz==0))
			if (this.vytaz==1) r.append("prvy team, na:"+ vysledok);
			else r.append("Druhy team, na:"+ vysledok);
		else r.append("Este nedohrate");
		r.append("\n");
		
		return r.toString();
	}

	public boolean isDvojHra() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStvorHra() {
		// TODO Auto-generated method stub
		return (this.teamA.size()==2) && (this.teamB.size()==2);
	}

	public boolean isViacHra() {
		// TODO Auto-generated method stub
		return !isStvorHra();
	}
}
