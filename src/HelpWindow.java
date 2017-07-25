import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpWindow extends JDialog{
	private static final long serialVersionUID = -7257135450358148960L;
	private JTextArea textfield;
	public HelpWindow(JFrame owner)
	{
		super(owner,"Pomoc",true);
		setBounds(50, 50, 700, 600);
		textfield = new JTextArea();
		textfield.setMargin(new Insets(5,20,0,20));
		textfield.setEditable(false);
		textfield.setLineWrap(true);
		textfield.setFont(new Font("SansSerif",Font.PLAIN,15) );
		JScrollPane sp = new JScrollPane(textfield);
		add(sp);
		importText();
		setVisible(true);
	}
	private void importText()
	{
		File file= new File("help.txt");
		if(file.exists())
		{
			try 
			{
				Scanner fileReader= new Scanner(file);
				while(fileReader.hasNext())
				{
					textfield.append(fileReader.nextLine()+"\n");
				}
				fileReader.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}	
	}
}