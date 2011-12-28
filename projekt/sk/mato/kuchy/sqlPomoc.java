package sk.mato.kuchy;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class sqlPomoc extends SQLiteOpenHelper {

	public sqlPomoc(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// hraci
	public void inicializujHracskuDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		db
				.execSQL("CREATE TABLE `hraci` (`id` INTEGER PRIMARY KEY ,`meno` VARCHAR( 50 ) NOT NULL ,`priezvisko` VARCHAR( 50 ) NOT NULL ,`vek` INTEGER NOT NULL ,`respekt` REAL NOT NULL) ;");
	}

	public void inicializujTreningoovuDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		db
				.execSQL("CREATE TABLE `treningy` (`id` Integer Primary Key Autoincrement, `datum` Text NOT NULL , `popis` TEXT NOT NULL , `pocetkurtov` INT NOT NULL , `hraci` INT NOT NULL , `zapasy` INT NOT NULL  ) ;");
	}

	public void inicializujZapasovuDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		db
				.execSQL("CREATE TABLE \"zapasy\"(     \"id\" Integer Primary Key Autoincrement  ,      \"typ\" Integer  NOT NULL  ,      \"teamA\" Text  NOT NULL  ,      \"teamB\" Text  NOT NULL  ,      \"datum\" Text  NOT NULL  ,      \"vysledok\" Integer  NOT NULL  ,      \"vytaz\" Integer  NOT NULL  );");
	}

	public void pridajHraca(Hrac novy) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		// cv.put("id", 1);
		cv.put("meno", novy.getMeno());
		cv.put("priezvisko", novy.getPriezvisko());
		cv.put("vek", novy.getVek());
		cv.put("respekt", novy.getRespekt());
		db.insert("hraci", "id", cv);
		cv.clear();
		db.close();
	}

	public String vypisHracskuDb() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM `hraci` ", new String[] {});
		ArrayList<String> strings = new ArrayList<String>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String mTitleRaw = cursor.getString(0) + " , "
					+ cursor.getString(1) + " , " + cursor.getString(2) + " , "
					+ cursor.getString(3) + " , " + cursor.getString(4);
			strings.add(mTitleRaw + "\n");
		}
		String ret = new String("");
		for (String string : strings) {
			ret += string;
		}
		return ret;
	}

	public Hrac getHraca(String meno, String priezvisko) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM `hraci` WHERE `meno` LIKE '"
				+ meno + "' AND `priezvisko` LIKE '" + priezvisko + "' ",
				new String[] {});
		Hrac novy;
		cursor.moveToFirst();
		novy = new Hrac(Integer.parseInt(cursor.getString(0)), cursor
				.getString(1), cursor.getString(2), Integer.parseInt(cursor
				.getString(3)), Double.parseDouble(cursor.getString(4)));
		cursor.close();
		db.close();
		return novy;
	}

	public Hrac getHraca(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM `hraci` WHERE `id` =  '"
				+ id + "' ", new String[] {});
		Hrac novy;
		cursor.moveToFirst();
		novy = new Hrac(Integer.parseInt(cursor.getString(0)), cursor
				.getString(1), cursor.getString(2), Integer.parseInt(cursor
				.getString(3)), Double.parseDouble(cursor.getString(4)));
		cursor.close();
		db.close();
		return novy;
	}

	public int getIdHraca(String meno, String priezvisko) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT id FROM `hraci` WHERE `meno` LIKE '" + meno
						+ "' AND `priezvisko` LIKE '" + priezvisko + "' ",
				new String[] {});
		cursor.moveToFirst();
		return Integer.parseInt(cursor.getString(0));
	}

	public int getIdZapasu(Zapas z) {
		SQLiteDatabase db = this.getReadableDatabase();

		int typ = 0;
		String teamA = new String();
		String teamB = new String();

		if (z.isDvojHra()) {
			typ = 1;
			teamA = Integer.toString(((Dvojhra) z).getTeamA().getId());
			teamB = Integer.toString(((Dvojhra) z).getTeamB().getId());
		} else
			typ = 2;
		// chyba doiplementovat iny typ...

		Cursor cursor = db.rawQuery("SELECT id FROM `zapasy` WHERE `typ` = '"
				+ typ + "' AND `teamA` = '" + teamA + "' AND `teamB` = '"
				+ teamB + "' AND `datum` = '" + z.getDatum().toString() + "' ",
				new String[] {});
		cursor.moveToFirst();
		int result = Integer.parseInt(cursor.getString(0));
		cursor.close();
		db.close();
		return result;
	}

	private static Hrac najdiHracavDB(ArrayList<Hrac> db, int id) {
		for (int i = 0; i < db.size(); i++)
			if (db.get(i).getId() == id) {
				return db.get(i);
			}
		return null;
	}

	public Zapas getZapas(int id, ArrayList<Hrac> hraci) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM `zapasy` WHERE `id` = '"
				+ id + "' ", new String[] {});

		cursor.moveToFirst();

		int typ = Integer.parseInt(cursor.getString(1));

		Dvojhra novy = null;

		if (typ == 1) {
			// Dvojhra novy= new Dvojhra( hraci.get, Hrac2, vytaz, nvysledok)
			int idA = Integer.parseInt(cursor.getString(2));
			int idB = Integer.parseInt(cursor.getString(3));
			Date datum = new Date(cursor.getString(4));// ?E?
			int vysledok = Integer.parseInt(cursor.getString(5));
			int vytaz = Integer.parseInt(cursor.getString(6));
			novy = new Dvojhra(najdiHracavDB(hraci, idA), najdiHracavDB(hraci,
					idB), vytaz, vysledok);
			novy.setDatum(datum);
		} else
			; // cakajuca implementacia na viachru

		cursor.close();
		db.close();

		return novy;
	}

	public ArrayList<Hrac> dajCeluDb() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM `hraci` ", new String[] {});
		ArrayList<Hrac> novy = new ArrayList<Hrac>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			novy.add(new Hrac(Integer.parseInt(cursor.getString(0)), cursor
					.getString(1), cursor.getString(2), Integer.parseInt(cursor
					.getString(3)), Double.parseDouble(cursor.getString(4))));
		}
		cursor.close();
		db.close();
		return novy;
	}

	public void vycistiHracskuDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from hraci; ");
		db.close();
	}

	public void dropHraci() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DROP TABLE `hraci`");
		db.close();
	}

	public static boolean checkDataBase(String cesta) {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(cesta, null,
					SQLiteDatabase.OPEN_READONLY);
			checkDB.close();
		} catch (SQLiteException e) {
			// database doesn't exist yet.
		}
		return checkDB != null ? true : false;
	}

	public void pridajZapas(Zapas z) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		int typ = 0;
		String teamA = new String();
		String teamB = new String();

		if (z.isDvojHra()) {
			typ = 1;
			teamA = Integer.toString(((Dvojhra) z).getTeamA().getId());
			teamB = Integer.toString(((Dvojhra) z).getTeamB().getId());
		} else
			typ = 2;

		cv.put("typ", typ);
		cv.put("teamA", teamA);
		cv.put("teamB", teamB);
		cv.put("datum", z.getDatum().toString());
		cv.put("vysledok", z.getVysledok());
		cv.put("vytaz", z.getVytaz());
		db.insert("zapasy", "id", cv);

		cv.clear();
		db.close();
	}

	public void pridajTrening(Trening trening, StringBuffer zapasyString) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		StringBuffer hraci = new StringBuffer();
		for (Hrac h : trening.getHracov()) {
			hraci.append(h.getId() + "/");
		}

		cv.put("datum", trening.getDatumTreningu().toString());
		cv.put("popis", trening.getPopisTreningu());
		cv.put("pocetkurtov", trening.getPocetKurtov());
		cv.put("hraci", hraci.toString());
		cv.put("zapasy", zapasyString.toString());
		db.insert("treningy", "id", cv);
	}
}
