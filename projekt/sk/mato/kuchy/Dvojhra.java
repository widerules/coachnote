package sk.mato.kuchy;

public class Dvojhra extends Zapas {
	private Hrac teamA;
	private Hrac teamB;
	
	public Hrac getTeamA() {
		return teamA;
	}

	public void setTeamA(Hrac teamA) {
		this.teamA = teamA;
	}

	public Hrac getTeamB() {
		return teamB;
	}

	public void setTeamB(Hrac teamB) {
		this.teamB = teamB;
	}

	@Override
	public String tostring() {
		// TODO Auto-generated method stub
		return "Team A"+teamA.getMeno() + " " + teamA.getPriezvisko() + " " + teamB.getMeno() + " "
		+"Team B:"+ teamB.getPriezvisko() + " " + vytaz + " " + vysledok + "\n";
	}
	
	public Dvojhra(Hrac Hrac1, Hrac Hrac2) {
		this.setTeamA(Hrac1);
		this.setTeamB(Hrac2);
	}
	
	public Dvojhra(Hrac Hrac1, Hrac Hrac2, int vytaz, int nvysledok) {
		this.setTeamA(Hrac1);
		this.setTeamB(Hrac2);
		this.setVytaz(vytaz);
		this.setVysledok(nvysledok);
	}
	
	public void setVytaz(Hrac vytaz) {
		if (this.teamA == vytaz)
			this.vytaz = 1;
		else if (this.teamB == vytaz)
			this.vytaz = 2;
		else
			this.vytaz = 0;
	}

	public boolean isDvojHra() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isStvorHra() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isViacHra() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
