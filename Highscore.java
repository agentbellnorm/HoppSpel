import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.lang.*;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.Integer.*;

//Klass en Highscore skapar, sorterar och skriver ut topplistan.
public class Highscore{
	private int poeng;
	private String nam, temp;
	boolean namncheck;
	String dir = "C:/HoppHs.txt";


		//Metoden som lagger till alla nya poang och namn i textfilen
		public void Add(int peng){
			poeng=peng;
			namncheck=false;

			//Inmatning av namn som ser till att strangen inte lamnas tom
			while(namncheck==false){
				nam = JOptionPane.showInputDialog("Ditt Namn: ");
				if(nam==null || nam.equals("")){
					namncheck=false;
				}
				else{
					namncheck=true;
				}
			}
			//Skriver namn och poang till .txt-filen
			try{
				PrintWriter utFil = new PrintWriter(new BufferedWriter(
				new FileWriter(dir, true)));
				utFil.println(nam);
				utFil.println(poeng);
				utFil.close();
				}
			catch(Exception e){
				JOptionPane.showMessageDialog(null,"Filen kunde inte lasas!");
			}
		}
		//Metod som laser in fran filen, sorterar och skriver ut topplistan
		public void Read(){
			int antalrader=0, antal;
			int t=0;
			int p;
			String n;
			try{
				//Raknar rader i filen for langdbestamning av arrayen
				Scanner sc = new Scanner (new File(dir));
				while(sc.hasNext()){
					String line = sc.nextLine();
					antalrader++;
				}
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null,"Filen kunde inte lasas!");
			}
			antal=antalrader/2;
			int rest = 10-antal;
			//Skriver tomma poster till filen om antalet poster ar under 10
			try{
				PrintWriter utFil1 = new PrintWriter(new BufferedWriter(
				new FileWriter(dir, true)));
					for(int y=0; y<rest; y++){
						utFil1.println("[tom]");
						utFil1.println(0);
					}
					utFil1.close();

			}

			catch(Exception e){
				JOptionPane.showMessageDialog(null,"Filen kunde inte lasas!");
			}
			System.out.println(antalrader + " " + antal);

			//Skapar sa manga spelare som existerar i filen
			Person spelare[] = spelare=new Person[antal];
			try{
				//Laser in data fran filen till en array av Person-objet.
				t=0;
				Scanner sc2 = new Scanner (new File(dir));
				while(sc2.hasNext()){
					n = sc2.next();
					p = sc2.nextInt();
					spelare[t]= new Person(n, p);
					t++;
				}
			}
			catch(Exception e){
				System.out.println("hej");
			}

			//Sorterar faltet
			int k;
			int ptemp=0;
			String ntemp;
			for(k = 0; k < antal; k++){
				int m =k;
			  for(int i = k+1; i < antal; i++)
				if(spelare[i].getPoang() < spelare[m].getPoang())
				  m=i;
				  ptemp = spelare[k].getPoang();
				  ntemp = spelare[k].getNamn();
				  spelare[k].setPoang(spelare[m].getPoang());
				  spelare[k].setNamn(spelare[m].getNamn());
				  spelare[m].setPoang(ptemp);
				  spelare[m].setNamn(ntemp);
			}


				int antarr = antal-1;
				//Skriver ut de tio hogsta poangen med namn
				JOptionPane.showMessageDialog(null,
				spelare[antarr].getNamn() + "  " + spelare[antarr].getPoang() +"\n" +
				spelare[antarr-1].getNamn() + "  " + spelare[antarr-1].getPoang() +"\n" +
				spelare[antarr-2].getNamn() + "  " + spelare[antarr-2].getPoang() +"\n" +
				spelare[antarr-3].getNamn() + "  " + spelare[antarr-3].getPoang() +"\n" +
				spelare[antarr-4].getNamn() + "  " + spelare[antarr-4].getPoang() +"\n" +
				spelare[antarr-5].getNamn() + "  " + spelare[antarr-5].getPoang() +"\n" +
				spelare[antarr-6].getNamn() + "  " + spelare[antarr-6].getPoang() +"\n" +
				spelare[antarr-7].getNamn() + "  " + spelare[antarr-7].getPoang() +"\n" +
				spelare[antarr-8].getNamn() + "  " + spelare[antarr-8].getPoang() +"\n" +
				spelare[antarr-9].getNamn() + "  " + spelare[antarr-9].getPoang() +"\n");
		}
	}





