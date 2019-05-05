// Klassen TennisBana
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.lang.Math.*;
import java.util.Random;
import java.io.*;


class HoppSpel extends JPanel implements ActionListener{
	//klockan utfor metoderna en gang per millisekund
	private Timer klocka=new Timer(10,this);
	private JLabel visaPoang; 	// for att visa poang
	private int xMax;			// hogsta xvarde
	private int yMax;			// hogsta yvarde
	private int radie;		// figurens radie
	private int x;				// lage, x
	private int y;				// lage, y
	private int h=0;			// figurens nya hojd over yMax
	private double rectbredd= xMax*0.23;	//plattformarnas bredd, dynamisk, beror pa fonstrets storlek
	private int recthojd = 8;	//plattformarnas hojd
	private int rect1xpos = 100;	//Plattformarnas startpositioner, x-led
	private int rect2xpos = 200;
	private int rect3xpos = 230;
	private int rect4xpos = 0;
	private int rect5xpos = 150;
	private int rect6xpos = 50;
	private double rectyref=(yMax/6);	//Variabel for att bestamma plattformarnas avstand mellan varandra
	private double rect1ypos;		//Plattformarnas startpositioner, y-led
	private double rect2ypos;
	private double rect3ypos;
	private double rect4ypos;
	private double rect5ypos;
	private double rect6ypos;
	double rect1temp=0;
	double rect2temp=0;
	double rect3temp=0;
	double rect4temp=0;
	double rect5temp=0;
	double rect6temp=0;
	boolean paused;					//div. kontrollvariabler
	boolean hoppa=true;
	boolean ner;
	boolean upp;
	boolean hoger;
	boolean vanster;
	boolean hit=false;
	boolean rectner = false;
	boolean spelar;
	private double t=0;				//Variabel som vilken figurens bana i y-led beror pa.
	private double p=0;
	private double d;				//Variabel som beskriver bollens hojd "over" yMax
	Random aRandom = new Random();	//randomobjekt for slumpvis utplacering av plattformar
	Highscore hs = new Highscore();	//For att kunna anropa metoderna i klassen "Highscore
	private int poang;				//poang
	private double k;



	//satter storlek och bakgrundsfarg
	public HoppSpel(){
		setPreferredSize(new Dimension(300,400));
		setBackground(Color.red);
	}
	//kor spelet
	public void start(JLabel p){


		visaPoang=p;
		// hamtar storlek pa "spelplanen"
		xMax=getSize().width;
		yMax=getSize().height;
		radie=15;
		nollstall();

		//Lyssnare som styr figuren vid knapptryck.
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					vanster = true;
				}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT){
					hoger = true;
				}

			}
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					vanster = false;
				}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT){
					hoger = false;
				}
			}
		});

		//Funktion som forhindrar spelaren att andra fonstrets storlek under spel och som andrar attribut
		//i programmet nar fonstrets storlek val andras
		addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				if(spelar==true){
					pausaSpel();
					JOptionPane.showMessageDialog(null, "Du kan inte andra fonstrets storlek medan du spelar!\n\n Klicka 'ok' och sedan 'forstatt' for att fortsatta spela");
				}
				else{
				xMax=e.getComponent().getSize().width;
				yMax=e.getComponent().getSize().height;
				e.getComponent().requestFocus();
				x=xMax/2;
				y=(int)yMax-radie;
				repaint();
			}
			}
		});
	}
	//Nar nytt spel startas nollstalls allt
	private void nollstall(){
		rectyref=(yMax/6);
		rect1ypos=rectyref;
		rect2ypos=rectyref*2;
		rect3ypos=rectyref*3;
		rect4ypos=rectyref*4;
		rect5ypos=0;
		rect6ypos= rectyref*5;
		visaPoang.setText(" 0");;
		x=xMax/2;
		y=((int)yMax-radie);
		t=0;
		h=0;
		poang=0;
		hit=false;
		hoger=false;
		vanster=false;
	}

	//Startar timern som styr bollens rorelse
	public void startaSpel(){
		klocka.start();
		hit=false;
	}

	//anropas nar spelet tagit slut (spelaren forlorat). ett highscoreobjekt skapas och den klassens metoder utfors
	public void stoppaSpel(){
		klocka.stop();
		hs.Add(poang);
		hs.Read();
		spelar=false;
	}
	//Om spelaren pausar stannar spelet
	public void pausaSpel(){
		klocka.stop();
		paused=true;

	}

	//Spelaren kan bara fortsatta spela om spelet ar pausat. Dvs. inte starta spelet med fortsatt-knappen.
	public void fortsatt(){
		if(paused==true){
			klocka.start();
			paused=false;
			spelar=true;
		}
	}
	public void nyttSpel(){
		nollstall();
		startaSpel();
		spelar=true;

	}
	//anropas
	public void actionPerformed(ActionEvent e){


			//bollens rorelse i y-led

			//Funktionen d(t) beskriver en andragradsfunktion vars maxvarde beror pa yMax (fonstrets hojd).
			//Tiden det tar for bollen att genomfora ett hopp pa marken ar 400 ms. "I luften" tar det
			//Langre tid eftersom att hela funktionens varde adderas med hojden h som fas vid traff av plattform.
			d =radie + h + -(((t-60)*t)/(4000/yMax));	//formeln typ: (-x^2+100x)/(2000/1024)
			if(hoppa==true){
				//Satser som bestammer om bollen ar pa vag upp eller ner. Ett hopp tar 400 ms. maximipunkten i d ar oberoende av h.
				if(t>30)
					ner=true;
				if(t<30){
					upp=true;
				}
				//For sakerhets skull har jag satt bade upp och ner till false da bollen nar sin hogsta punkt i banan
				if(t==30){
					ner=false;
					upp=false;
				}

				//Bollens y-kordinat ges av diffensen mellan yMax och d
				t++;

				//Da bollen befinner sig i nagon av de tva ovre tredjedelarna av skarmen flyttar sig plattformarna nedat.
				y= yMax-(int)d;
				if(y>(yMax/2) && ner==true){
					rectner=false;
				}

				else{
					rectner=true;

				}
				//Man kan hoppa pa marken hur manga ganger som helst tills man nuddar den forsta plattformen
				//Nuddar man marken da forlorar man.
				if(ner==true && hit==false && y+radie>yMax)
					t=0;
				//Spelet stoppas om man forlorar
				if(y-(radie)>yMax)
					stoppaSpel();

				//Bollens rorelse i x-led
				if(vanster == true)
					x-=4;
				else if (hoger == true)
					x+=4;

				if(x>xMax){
					x=0;
				}
				if(x<0){
					x=xMax;
				}

				//Plattformarnas rorelse och detektion av traff.

				// Flyttar ner om bollen ar over halva
				if(rectner==true){
					rect1ypos+=3;
					rect2ypos+=3;
					rect3ypos+=3;
					rect4ypos+=3;
					rect5ypos+=3;
					rect6ypos+=3;
					poang++;
				}
				//Kontrollerar traff. Om traff borjar hoppsekvensen om med en ny hojd.
				if((rect1xpos)<=x && x<=(rect1xpos+(int)rectbredd) &&  (y+radie)>=rect1ypos && (y+radie)<=rect1ypos+15 && ner==true) {
					t=0;
					h=yMax-(y);
					hit=true;
				}
				if((rect2xpos)<=x && x<=(rect2xpos+(int)rectbredd) &&  (y+radie)>=rect2ypos && (y+radie)<=rect2ypos+15 && ner==true) {
					t=0;
					h=yMax-(y);
					hit=true;
				}
				if((rect3xpos)<=x && x<=(rect3xpos+(int)rectbredd) &&  (y+radie)>=rect3ypos && (y+radie)<=rect3ypos+15 && ner==true) {
					t=0;
					h=yMax-(y);
					hit=true;
				}
				if((rect4xpos)<=x && x<=(rect4xpos+(int)rectbredd) &&  (y+radie)>=rect4ypos && (y+radie)<=rect4ypos+15 && ner==true) {
					t=0;
					h=yMax-(y);
					hit=true;
				}
				if((rect5xpos)<=x && x<=(rect5xpos+(int)rectbredd) &&  (y+radie)>=rect5ypos && (y+radie)<=rect5ypos+15 && ner==true) {
					t=0;
					h=yMax-(y);
					hit=true;
				}
				if((rect6xpos)<=x && x<=(rect6xpos+(int)rectbredd) &&  (y+radie)>=rect6ypos && (y+radie)<=rect6ypos+15 && ner==true) {
					t=0;
					h=yMax-(y);
					hit=true;
				}

				//Flyttar tillbaka om de nar langst ner med ny slumpmassig x-kordinat innanfor fonstret
				if(rect1ypos>=yMax){
					rect1ypos=1;
					rect1xpos=aRandom.nextInt(xMax-(int)rectbredd);
				}
				if(rect2ypos>=yMax){
					rect2ypos=1;
					rect2xpos=aRandom.nextInt(xMax-(int)rectbredd);;
				}
				if(rect3ypos>=yMax){
					rect3ypos=1;
					rect3xpos=aRandom.nextInt(xMax-(int)rectbredd);
				}
				if(rect4ypos>=yMax){
					rect4ypos=1;
					rect4xpos=aRandom.nextInt(xMax-(int)rectbredd);
				}
				if(rect5ypos>=yMax){
					rect5ypos=1;
					rect5xpos=aRandom.nextInt(xMax-(int)rectbredd);
				}
				if(rect6ypos>=yMax){
					rect6ypos=1;
					rect6xpos=aRandom.nextInt(xMax-(int)rectbredd);
				}
				//Plattformarnas bredd beror pa en funktion som gor sa att plattornas ursprungliga storlek
				//halverats vid 2000 poang oavsett av fonstrets storlek.
				double ref=xMax*0.23;
				k = -(((ref)/2)/2000);
				rectbredd=k*poang+(ref);
				visaPoang.setText(" " + String.valueOf(poang));
				repaint();
	}
}
	// Ritar bakgrundsfarg samt boll och racket
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.black);
		g.fillOval(x-radie,y-radie,2*radie,2*radie);
		g.fillRect(rect1xpos, (int)rect1ypos, (int)rectbredd, recthojd);
		g.fillRect(rect2xpos, (int)rect2ypos, (int)rectbredd, recthojd);
		g.fillRect(rect3xpos, (int)rect3ypos, (int)rectbredd, recthojd);
		g.fillRect(rect4xpos, (int)rect4ypos, (int)rectbredd, recthojd);
		g.fillRect(rect5xpos, (int)rect5ypos, (int)rectbredd, recthojd);
		g.fillRect(rect6xpos, (int)rect6ypos, (int)rectbredd, recthojd);
		//En liten punkt som visar var bollen befinner sig i x-led om den ar utanfor fonstret i y-led
		if(y<0)
		g.fillOval(x,5, 5,5);
	}

}




