package sk.mato.kuchy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.util.Xml;

public class OXml extends Activity{
	
	public static ArrayList<Hrac> nacitajHracov(InputStream file) {
		ArrayList<Hrac> nacitane= new ArrayList<Hrac>();
		  try {		   
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		  NodeList nodeLst = doc.getElementsByTagName("hrac");

		  for (int s = 0; s < nodeLst.getLength(); s++) {

		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		  
		           Element fstElmnt = (Element) fstNode;
		      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("meno");
		      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      NodeList fstNm = fstNmElmnt.getChildNodes();
		      String nmeno=((Node) fstNm.item(0)).getNodeValue();
		      //System.out.println("meno : "  + ((Node) fstNm.item(0)).getNodeValue());
		      
		      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("priezvisko");
		      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		      NodeList lstNm = lstNmElmnt.getChildNodes();
		      String npriezvisko=((Node) lstNm.item(0)).getNodeValue();
		      //System.out.println("priezvisko : " + ((Node) lstNm.item(0)).getNodeValue());
		      
		      NodeList rokNmElmntLst = fstElmnt.getElementsByTagName("vek");
		      Element rokNmElmnt = (Element) rokNmElmntLst.item(0);
		      NodeList rokNm = rokNmElmnt.getChildNodes();
		      int nvek=Integer.parseInt(((Node) rokNm.item(0)).getNodeValue());
		      //System.out.println("vek : "  + ((Node) rokNm.item(0)).getNodeValue());
		      
		      NodeList resNmElmntLst = fstElmnt.getElementsByTagName("respekt");
		      Element resNmElmnt = (Element) resNmElmntLst.item(0);
		      NodeList resNm = resNmElmnt.getChildNodes();
		      double nrespekt= Double.parseDouble(((Node) resNm.item(0)).getNodeValue());
 		      //System.out.println("respekt : "  + ((Node) resNm.item(0)).getNodeValue());
		      nacitane.add(new Hrac(nmeno, npriezvisko, nvek, nrespekt));
		    }

		  }
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		
		  return nacitane;
	}
	
	
 public static void pridajhraca(InputStream file, String nmeno, String npriezvisko, int nvek, double nrespekt) {
	 try {
		 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	     Document document = documentBuilder.parse(file);
	     Element root = document.getDocumentElement();
	     
	     Element main = document.getDocumentElement();
	     
	     Collection<Hrac> hra = new ArrayList<Hrac>();
	     hra.add(new Hrac(nmeno, npriezvisko, nvek));

	     
	     for (@SuppressWarnings("unused") Hrac i : hra) {
	            
	            Element hrac = document.createElement("hrac");
	            main.appendChild(hrac);

	            Element meno = document.createElement("meno");
	            meno.appendChild(document.createTextNode(nmeno));
	            hrac.appendChild(meno);

	            Element priezvisko = document.createElement("priezvisko");
	            priezvisko.appendChild(document.createTextNode(npriezvisko));
	            hrac.appendChild(priezvisko);

	            Element vek = document.createElement("vek");
	            vek.appendChild(document.createTextNode(Integer.toString(nvek)));
	            hrac.appendChild(vek);
	            
	            Element respekt = document.createElement("respekt");
	            respekt.appendChild(document.createTextNode(Double.toString(nrespekt)));
	            hrac.appendChild(respekt);
	              
	            root.appendChild(hrac);
	        }
	     DOMSource source = new DOMSource(document);

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        //StreamResult result = new StreamResult("assets/hraci.xml");
	        transformer.transform(source, (Result) file);
	        
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
}
 
private static Hrac najdiHracavDB(ArrayList<Hrac> db ,String meno, String priezvisko) {
		for (int i = 0; i < db.size(); i++)
			if (meno.equals(db.get(i).getMeno()) && priezvisko.equals(db.get(i).getPriezvisko())) {
				return db.get(i);
			}
		return null;
	}
	
 public static Trening nacitajTrening(InputStream file, ArrayList<Hrac> hracskaDB) {
		Trening nacitaj;
		ArrayList<Hrac> hraci= new ArrayList<Hrac>();
		ArrayList<Zapas> zapasy= new ArrayList<Zapas>();
		Date datumTreningu = null;//yyyy-mm-dd format
		int pocKurtov = 0;
		String poznamka = null;
		  try {		   
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document doc = db.parse(file);
		  doc.getDocumentElement().normalize();
		  
		  NodeList nodeLst = doc.getElementsByTagName("poznamky");
		  //System.out.println("dlzka:"+nodeLst.getLength());
		  Node datumnode = nodeLst.item(0);
		  if (datumnode.getNodeType() == Node.ELEMENT_NODE) {
			//datum
			  Element fstElmnt = (Element) datumnode;
		      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("date");
		      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      NodeList fstNm = fstNmElmnt.getChildNodes();
		      String datum=((Node) fstNm.item(0)).getNodeValue();
		      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		      
		      datumTreningu= formatter.parse(datum);
		    //pocetkurtov
			  fstElmnt = (Element) datumnode;
		      fstNmElmntLst = fstElmnt.getElementsByTagName("pock");
		      fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      fstNm = fstNmElmnt.getChildNodes();
		      pocKurtov= Integer.parseInt(((Node) fstNm.item(0)).getNodeValue());
		    //poznamka
		      fstElmnt = (Element) datumnode;
		      fstNmElmntLst = fstElmnt.getElementsByTagName("pozn");
		      fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      fstNm = fstNmElmnt.getChildNodes();
		      poznamka=((Node) fstNm.item(0)).getNodeValue();
		  }
		  //hraci
		  nodeLst = doc.getElementsByTagName("hrac");

		  for (int s = 0; s < nodeLst.getLength(); s++) {

		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		  
		      Element fstElmnt = (Element) fstNode;
		      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("meno");
		      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      NodeList fstNm = fstNmElmnt.getChildNodes();
		      String nmeno=((Node) fstNm.item(0)).getNodeValue();
		      //System.out.println("meno : "  + ((Node) fstNm.item(0)).getNodeValue());
		      
		      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("priezvisko");
		      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		      NodeList lstNm = lstNmElmnt.getChildNodes();
		      String npriezvisko=((Node) lstNm.item(0)).getNodeValue();
		      //System.out.println("priezvisko : " + ((Node) lstNm.item(0)).getNodeValue());
		      hraci.add(najdiHracavDB(hracskaDB, nmeno, npriezvisko));
		    }
		  }
		//zapasy
		  nodeLst = doc.getElementsByTagName("zapas");

		  for (int s = 0; s < nodeLst.getLength(); s++) {

		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		  
		           Element fstElmnt = (Element) fstNode;
		      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("ameno");
		      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		      NodeList fstNm = fstNmElmnt.getChildNodes();
		      String amenos=((Node) fstNm.item(0)).getNodeValue();
		      String[] ameno= amenos.split("/");
		      Hrac a= najdiHracavDB(hraci, ameno[1], ameno[0]);
		      
		      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("bmeno");
		      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		      NodeList lstNm = lstNmElmnt.getChildNodes();
		      String bmenos=((Node) lstNm.item(0)).getNodeValue();
		      String[] bmeno= bmenos.split("/");
		      Hrac b =najdiHracavDB(hraci, bmeno[1], bmeno[0]);
		      
		      NodeList rokNmElmntLst = fstElmnt.getElementsByTagName("vmeno");
		      Element rokNmElmnt = (Element) rokNmElmntLst.item(0);
		      NodeList rokNm = rokNmElmnt.getChildNodes();
		      String vmenos=((Node) rokNm.item(0)).getNodeValue();
		      String[] vmeno= vmenos.split("/");
		      Hrac v =najdiHracavDB(hraci, vmeno[1], vmeno[0]);
		      
		      NodeList resNmElmntLst = fstElmnt.getElementsByTagName("vysledok");
		      Element resNmElmnt = (Element) resNmElmntLst.item(0);
		      NodeList resNm = resNmElmnt.getChildNodes();
		      int vysledok= Integer.parseInt(((Node) resNm.item(0)).getNodeValue());
		      zapasy.add(new Zapas(a, b, v, vysledok));
		      
		    }
		  }
		  nacitaj= new Trening(hraci, zapasy, pocKurtov, poznamka, datumTreningu);
		  return nacitaj;
		  
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		
		  nacitaj= new Trening(null, null, -1, "zleje!", null);  
		return nacitaj;
	}
 
public static void zapisHracomNovyrespekt( ArrayList<Hrac> zapisuj ) throws IllegalArgumentException, IllegalStateException, IOException  {
	 File novytrening= new File("/data/data/sk.mato.kuchy/files/hraci.xml");
		
		if (novytrening.exists()) novytrening.delete();
		novytrening.createNewFile();
		
		FileOutputStream novyTreningFOS = new FileOutputStream(novytrening);
		XmlSerializer serializer = Xml.newSerializer();	
		
		serializer.setOutput(novyTreningFOS, "UTF-8"); //hlavicka
		serializer.startDocument(null, Boolean.valueOf(true));
		
		serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);// wtf?
		
		serializer.startTag(null, "main");
		
		for (int i=0; i<zapisuj.size(); i++) {
			
			serializer.startTag(null, "hrac");
			
			serializer.startTag(null, "meno");
			serializer.text( zapisuj.get(i).getMeno() );
			serializer.endTag(null, "meno");
			
			serializer.startTag(null, "priezvisko");
			serializer.text( zapisuj.get(i).getPriezvisko());
			serializer.endTag(null, "priezvisko");
			
			serializer.startTag(null, "vek");
			serializer.text( Integer.toString( zapisuj.get(i).getVek() ));
			serializer.endTag(null, "vek");
			
			serializer.startTag(null, "respekt");
			serializer.text( Double.toString( zapisuj.get(i).getRespekt() ));
			serializer.endTag(null, "respekt");
			
			serializer.endTag(null, "hrac");
		}
		
		/*
		 * <hrac>
		<meno>Šimon</meno>
		<priezvisko>Jurina</priezvisko>
		<vek>0</vek>
		<respekt>1.0</respekt>
	</hrac>
	*/
		
		serializer.endTag(null, "main");    
	    serializer.endDocument();
	    serializer.flush();
	    novyTreningFOS.close();    
 }

public static void vytvorNovyTrening( Trening trening ) throws IOException{
	File novytrening= new File("/data/data/sk.mato.kuchy/files/trening.xml");
	
	if (novytrening.exists()) novytrening.delete();
	novytrening.createNewFile();
	
	FileOutputStream novyTreningFOS = new FileOutputStream(novytrening);
	XmlSerializer serializer = Xml.newSerializer();
	
	//samotne xml
	serializer.setOutput(novyTreningFOS, "UTF-8"); //hlavicka
	serializer.startDocument(null, Boolean.valueOf(true));
	
	serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);// wtf?
	
	serializer.startTag(null, "main");
	
	//zaciatok poznamok
	serializer.startTag(null, "poznamky");
	
	//datumtreningu
	serializer.startTag(null, "date");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	serializer.text(formatter.format(trening.getDatumTreningu()));
	serializer.endTag(null, "date");
	
	//pocetkurtov
	serializer.startTag(null, "pock");
	serializer.text(Integer.toString(trening.getPocetKurtov()));
	serializer.endTag(null, "pock");
	
	//textova poznamka
	serializer.startTag(null, "pozn");
	serializer.text( trening.getPopisTreningu() );
	serializer.endTag(null, "pozn");
	
    serializer.endTag(null, "poznamky");
    
    serializer.startTag(null, "dochdzka");
	for (int i=0; i<trening.getPocetHracov(); i++) {
		
		serializer.startTag(null, "hrac");
		
		serializer.startTag(null, "meno");
		serializer.text( trening.getHracov().get(i).getMeno() );
		serializer.endTag(null, "meno");
		
		serializer.startTag(null, "priezvisko");
		serializer.text( trening.getHracov().get(i).getPriezvisko());
		serializer.endTag(null, "priezvisko");
		
		serializer.endTag(null, "hrac");
	}
	serializer.endTag(null, "dochdzka");
    
	serializer.startTag(null, "zapasy");
	for (int i=0; i<trening.getZapasy().size(); i++) {
		
		serializer.startTag(null, "zapas");
		
		serializer.startTag(null, "ameno");
		serializer.text( trening.getZapasy().get(i).getA().getPriezvisko()+"/"+trening.getZapasy().get(i).getA().getMeno());
		serializer.endTag(null, "ameno");
		
		serializer.startTag(null, "bmeno");
		serializer.text( trening.getZapasy().get(i).getB().getPriezvisko()+"/"+trening.getZapasy().get(i).getB().getMeno());
		serializer.endTag(null, "bmeno");
		
		serializer.startTag(null, "vmeno");
		if (trening.getZapasy().get(i).getVysledok()<0) serializer.text("NiktoNevyhralEste/NiktoNevyhralEste");
		else serializer.text( trening.getZapasy().get(i).getVytaz().getPriezvisko()+"/"+trening.getZapasy().get(i).getVytaz().getMeno());
		serializer.endTag(null, "vmeno");
		
		serializer.startTag(null, "vysledok");
		serializer.text( Integer.toString(trening.getZapasy().get(i).getVysledok()));
		serializer.endTag(null, "vysledok");
		
		serializer.endTag(null, "zapas");
	}
	serializer.endTag(null, "zapasy");
	
    serializer.endTag(null, "main");    
    serializer.endDocument();
    serializer.flush();
    novyTreningFOS.close();
	
}

public static void vytvorNovyTrening( Trening trening,File wallpaperDirectory, String name ) throws IOException{
	File novytrening= new File(wallpaperDirectory, name);
    
	if (novytrening.exists()) novytrening.delete();
	novytrening.createNewFile();
	
	FileOutputStream novyTreningFOS = new FileOutputStream(novytrening);
	novyTreningFOS.flush();
	
	XmlSerializer serializer = Xml.newSerializer();
	
	//samotne xml
	serializer.setOutput(novyTreningFOS, "UTF-8"); //hlavicka
	serializer.startDocument(null, Boolean.valueOf(true));
	
	serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);// wtf?
	
	serializer.startTag(null, "main");
	
	//zaciatok poznamok
	serializer.startTag(null, "poznamky");
	
	//datumtreningu
	serializer.startTag(null, "date");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	serializer.text(formatter.format(trening.getDatumTreningu()));
	serializer.endTag(null, "date");
	
	//pocetkurtov
	serializer.startTag(null, "pock");
	serializer.text(Integer.toString(trening.getPocetKurtov()));
	serializer.endTag(null, "pock");
	
	//textova poznamka
	serializer.startTag(null, "pozn");
	serializer.text( trening.getPopisTreningu() );
	serializer.endTag(null, "pozn");
	
    serializer.endTag(null, "poznamky");
    
    serializer.startTag(null, "dochdzka");
	for (int i=0; i<trening.getPocetHracov(); i++) {
		
		serializer.startTag(null, "hrac");
		
		serializer.startTag(null, "meno");
		serializer.text( trening.getHracov().get(i).getMeno() );
		serializer.endTag(null, "meno");
		
		serializer.startTag(null, "priezvisko");
		serializer.text( trening.getHracov().get(i).getPriezvisko());
		serializer.endTag(null, "priezvisko");
		
		serializer.endTag(null, "hrac");
	}
	serializer.endTag(null, "dochdzka");
    
	serializer.startTag(null, "zapasy");
	for (int i=0; i<trening.getZapasy().size(); i++) {
		
		serializer.startTag(null, "zapas");
		
		serializer.startTag(null, "ameno");
		serializer.text( trening.getZapasy().get(i).getA().getPriezvisko()+"/"+trening.getZapasy().get(i).getA().getMeno());
		serializer.endTag(null, "ameno");
		
		serializer.startTag(null, "bmeno");
		serializer.text( trening.getZapasy().get(i).getB().getPriezvisko()+"/"+trening.getZapasy().get(i).getB().getMeno());
		serializer.endTag(null, "bmeno");
		
		serializer.startTag(null, "vmeno");
		if (trening.getZapasy().get(i).getVysledok()<0) serializer.text("NiktoNevyhralEste/NiktoNevyhralEste");
		else serializer.text( trening.getZapasy().get(i).getVytaz().getPriezvisko()+"/"+trening.getZapasy().get(i).getVytaz().getMeno());
		serializer.endTag(null, "vmeno");
		
		serializer.startTag(null, "vysledok");
		serializer.text( Integer.toString(trening.getZapasy().get(i).getVysledok()));
		serializer.endTag(null, "vysledok");
		
		serializer.endTag(null, "zapas");
	}
	serializer.endTag(null, "zapasy");
	
    serializer.endTag(null, "main");    
    serializer.endDocument();
    serializer.flush();
    novyTreningFOS.close();
	
}

}
