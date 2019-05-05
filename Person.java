public class Person{
	private int p;
	private String n;
	//konstruktor som anvands vid skapande av ny spelare
	public Person(String nom, int pinne){
		n=nom;
		p=pinne;
	}
	//Till	ter andra klasser att andra namn och poang
	public void setPoang(int pinne){
		p=pinne;
	}
	public void setNamn(String nom){
		n=nom;
	}
	//Metoder som returnerar namn och poang
	public String getNamn(){
		return(n);
	}

	public int getPoang(){
		return(p);
	 }
 }