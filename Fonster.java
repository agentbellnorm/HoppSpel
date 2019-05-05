import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Fonster extends JFrame{


	private HoppSpel bana=new HoppSpel();
	private JLabel poang;
	private JPanel p;
	private JButton[] knapp=new JButton[4];
	private String[] knappText={"Nytt spel","Paus","Fortsatt","Highscore"};

	public Fonster(){
		super("Hoppa lite tack");
		poang=new JLabel(" 0",JLabel.CENTER);
		poang.setFont(new Font("Arial", Font.BOLD, 30));
		p=new JPanel();

		Lyssnare minLyssnare=new Lyssnare();

		//knapparna skapas och far de namn som finns i string-faltet
		for(int i=0; i<knapp.length; i++){
			knapp[i]=new JButton();
			knapp[i].setText(knappText[i]);
			knapp[i].addActionListener(minLyssnare);
			p.add(knapp[i]);
		}

		// Komponenter och paneler placeras i fonstret
		Container c=getContentPane();
		c.add(poang,"North");
		c.add(bana,"Center");
		c.add(p,"South");
		//avslutar om fonstret stangs
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});

		pack();
		bana.start(poang);
	}

	class Lyssnare implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			//fokus satts pa spelplanen
			// utfor vald aktivitet
			bana.requestFocus();
			if(ae.getSource()==knapp[0]){
				bana.nyttSpel();
			}
			else if(ae.getSource()==knapp[1]){
				bana.pausaSpel();
			}
			else if(ae.getSource()==knapp[2]){
				bana.fortsatt();
			}
			else{
				Highscore hs = new Highscore();
				hs.Read();
			}
		}
	}
}


