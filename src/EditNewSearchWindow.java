import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/*Uniwersalna klasa umo¿liwiaj¹ca dodawanie i edytowanie pieœni oraz zaawansowane wyszukiwanie  */
public class EditNewSearchWindow extends JDialog implements ActionListener,WindowListener{
	private static final long serialVersionUID = 1L;
	private int option;
	/*Elementy GUI*/
	private JTextField textfield; //Mo¿e zawieraæ nazwe pliku lub wyszukiwana frazê
	private JTextArea textarea; //Mo¿e zawieraæ tekst piosenki lub listê plików
	private JButton bOK,bCancel,bSetname;//bOk mo¿e s³u¿yc zarówno jako przycisk szukania jak i zapisu
	private JPopupMenu popupmenu;
	private JMenuItem mCopy,mPaste,mCut;
	/*Pola pomocnicze*/
	private File file;
	private String Path;
	/*Konstruktory*/
	/*Konstruktor do edycji*/
	public EditNewSearchWindow(JFrame owner, File pfile) 
	{
		super(owner,"Edytuj tekst",true);
		setLocation(300,200);
		addWindowListener(this);
		if(pfile.isFile())
		{
			option=1;
			file=pfile;
		}
		else if(pfile.isDirectory())
		{
			Path=pfile.getAbsolutePath();
			option=2;
		}
		configureView();
	}
	/*Metody universalne*/
	private void configurePopupMenu()
	{
		mCopy = new JMenuItem("Kopiuj");
		mCopy.addActionListener(this);
		mPaste = new JMenuItem("Wklej");
		mPaste.addActionListener(this);
		mCut = new JMenuItem("Wytnij");
		mCut.addActionListener(this);
		
		popupmenu= new JPopupMenu();
		popupmenu.add(mCopy);
		popupmenu.add(mPaste);
		popupmenu.add(mCut);
		
		textarea.setComponentPopupMenu(popupmenu);
	}
	private void configureTextfields()
	{
		JLabel filename= new JLabel("Œcie¿ka do pliku:",JLabel.CENTER);
		filename.setBounds(50,10,200,20);
		add(filename);
		textfield = new JTextField();
		//textfield.setBounds(50,50,200,30);
		textfield.setEditable(false);
		JScrollPane tfsp = new JScrollPane(textfield);
		tfsp.setBounds(50,30,200,40);
		///add(textfield);
		add(tfsp);
		
		textarea = new JTextArea();
		JScrollPane sp = new JScrollPane(textarea);
		sp.setBounds(50,90,500,600);
		add(sp);
	}
	private void configureButtons()
	{
		bOK = new JButton("Zapisz");
		bOK.setBounds(50,700,150,40);
		bOK.addActionListener(this);
		add(bOK);
		
		bCancel= new JButton("Anuluj");
		bCancel.setBounds(220,700,150,40);
		bCancel.addActionListener(this);
		add(bCancel);
		if(option==2) 
		{
			bSetname =new JButton("Wybierz nazwê pliku");
			bSetname.addActionListener(this); 
			bSetname.setBounds(260,30,150,40);
			add(bSetname);
		} 
	}
	public void openAnother(File pfile)
	{
		textfield.setText("");
		textarea.setText("");
		if(pfile.isFile())//edycja
		{
			file=pfile;
			textfield.setText(file.getAbsolutePath());
			ImportText();
		}
		else if(pfile.isDirectory())
			Path=pfile.getAbsolutePath();
		setVisible(true);
	}
	private void configureView()
	{
		setSize(600,800);
		setResizable(false);
		setLayout(null);
		configureTextfields();
		configurePopupMenu();
		configureButtons();
		if(option==1)
		{
			textfield.setText(file.getAbsolutePath());
			ImportText();
		}			
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	/*Metody do edycji*/
	private void ImportText()
	{	
		textarea.setText("");
		if(file.exists())
		{
			try {
				Scanner fileReader = new Scanner(file);
				while(fileReader.hasNext())
				{
					textarea.append(fileReader.nextLine()+'\n');
				}
				fileReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Ten plik nie istnieje!","Wyst¹pi³ b³¹d",JOptionPane.ERROR_MESSAGE);
		}
	}
	private void saveFile()
	{
		if(textfield.getText().isEmpty()==false)
		{
			if(file.exists())
			{
				if(option==2)
				{
					int odp=JOptionPane.showConfirmDialog(this, "Plik ju¿ istnieje! \n Czy chcesz go nadpisaæ?", "Plik istnieje",JOptionPane.YES_NO_OPTION);
					if(odp!=JOptionPane.YES_OPTION)
						return;
				}
				file.delete();
			}
				try {
					PrintWriter filewriter = new PrintWriter(file);
					String tavalue=textarea.getText();
					Scanner taReader = new Scanner(tavalue);
					while(taReader.hasNext())
					{
						filewriter.println(taReader.nextLine());
					}
					taReader.close();
					filewriter.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Nazwa pliku nie mo¿e byæ pusta!", "Wyst¹pi³ b³¹d", JOptionPane.INFORMATION_MESSAGE);
		}
		setVisible(false);
	}
	private void setFileName()
	{
		JFileChooser fc= new JFileChooser();
		fc.setCurrentDirectory(new File(Path));
		fc.setDialogTitle("Wybierz nazwê pliku");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
		if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)
		{
			if(!fc.getSelectedFile().getAbsolutePath().endsWith(".txt")){
				file = new File(fc.getSelectedFile() + ".txt");
			}
			else 
			file=fc.getSelectedFile();
			textfield.setText(file.getAbsolutePath());
		}
	}
	/*System clipboard*/
	private void insertStringToClipboard(String s)
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		 StringSelection selection = new StringSelection(s);
		clipboard.setContents(selection, selection);
	}
	private String getTextFromClipboard()
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		try {
			return (String)clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String("");
	}
	private void insertFromClipboard()
	{
		textarea.insert(getTextFromClipboard(), textarea.getCaretPosition());
	}
	/*Action Listener*/
	@Override
	public void actionPerformed(ActionEvent e) {
		Object z =e.getSource();
		if(z==bOK)
		{
			switch(option)
			{
				case 1:
					saveFile();
				break;
				case 2:
					saveFile();
				break;
			}
		}
		else if(z==bCancel)
		{
			textarea.setText("");
			textfield.setText("");
			setVisible(false);
		}
		else if(z==mPaste)
			insertFromClipboard();
		else if(z==mCopy)
			insertStringToClipboard(textarea.getSelectedText());
		else if(z==mCut)
		{
			insertStringToClipboard(textarea.getSelectedText());
			textarea.replaceSelection("");
		}
		else if(z==bSetname)
			setFileName();
	}
	/*Windows listenree*/
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		textarea.setText("");
		textfield.setText("");
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
				
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
