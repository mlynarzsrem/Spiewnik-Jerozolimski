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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*Uniwersalna klasa umo¿liwiaj¹ca dodawanie i edytowanie pieœni oraz zaawansowane wyszukiwanie  */
public class EditNewSearchWindow extends JDialog implements ActionListener,WindowListener{
	private static final long serialVersionUID = 1L;
	private int option;
	/*Elementy GUI*/
	private JTextField textfield; //Mo¿e zawieraæ nazwe pliku lub wyszukiwana frazê
	private JTextArea textarea; //Mo¿e zawieraæ tekst piosenki lub listê plików
	private JButton bOK,bCancel;//bOk mo¿e s³u¿yc zarówno jako przycisk szukania jak i zapisu
	private JPopupMenu popupmenu;
	private JMenuItem mCopy,mPaste,mCut;
	/*Pola pomocnicze*/
	private File file;
	private String Path;
	private GlobalSettings Settings;
	/*Konstruktory*/
	/*Konstruktor do edycji*/
	public EditNewSearchWindow(JFrame owner, File pfile) 
	{
		super(owner,"Edytuj tekst",true);
		setLocation(300,200);
		option=1;
		file=pfile;
		addWindowListener(this);
		configureEditView();
	}
	/*Konstruktor do dodawnaie nowych*/
	public EditNewSearchWindow(JFrame owner, String path) 
	{
		super(owner,"Nowy tekst",true);
		setLocation(300,200);
		addWindowListener(this);
		Path=path;
		option=2;
		configureNewView();
	}
	/*Konstruktor do wyszukiwania*/
	public EditNewSearchWindow(JFrame owner,GlobalSettings gs) 
	{
		super(owner,"Szukanie zaawansowane",true);
		setLocation(300,200);
		addWindowListener(this);
		Settings=gs;
		option=3;
		configureSearchingView();
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
	private void configureTextfields()//nie dzia³a do wyszukiwania
	{
		JLabel filename= new JLabel("Nazwa pliku:",JLabel.CENTER);
		filename.setBounds(50,20,200,30);
		add(filename);
		textfield = new JTextField();
		textfield.setBounds(50,50,200,30);
		add(textfield);
		
		textarea = new JTextArea();
		JScrollPane sp = new JScrollPane(textarea);
		sp.setBounds(50,90,500,600);
		add(sp);
	}
	private void configureButtons()//komentarz jak wy¿ej
	{
		bOK = new JButton("Zapisz");
		bOK.setBounds(50,700,150,40);
		bOK.addActionListener(this);
		add(bOK);
		
		bCancel= new JButton("Anuluj");
		bCancel.setBounds(220,700,150,40);
		bCancel.addActionListener(this);
		add(bCancel);
	}
	/*Metody do edycji*/
	private void configureEditView()
	{
		setSize(600,800);
		setResizable(false);
		setLayout(null);
		configureTextfields();
		textfield.setEditable(false);
		textfield.setText(file.getName());
		configurePopupMenu();
		configureButtons();
		ImportText();
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
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
	private void SaveChanges()
	{
		if(file.exists())
		{
			String filePath=file.getAbsolutePath();
			if(file.delete()==true)
			{
				file = new File(filePath);
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
				JOptionPane.showMessageDialog(this, "Nie mo¿na zapisaæ! \n Prawdopodobnie ten plik jest ju¿ otwarty w innym programie ","Wyst¹pi³ b³¹d",JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Ten plik nie istnieje!","Wyst¹pi³ b³¹d",JOptionPane.ERROR_MESSAGE);
		}
		setVisible(false);
	}
	public void NextEdit(File pfile)
	{
		file=pfile;
		ImportText();
		setVisible(true);
	}
	/*metody do tworzenia nowego teksty*/
	private void configureNewView()
	{
		setSize(600,800);
		setResizable(false);
		setLayout(null);
		configureTextfields();
		textfield.setToolTipText("Tu wpisz nazwê pliku.");
		configurePopupMenu();
		configureButtons();
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	private void AddNewText()
	{
		if(textfield.getText().isEmpty()==false)
		{
			String format=".txt";
			String newPath= this.Path+"\\"+ textfield.getText() +format;
			file = new File(newPath);
			if(file.exists())
			{
				int odp=JOptionPane.showConfirmDialog(this, "Plik ju¿ istnieje! \n Czy chcesz go nadpisaæ?", "Plik istnieje",JOptionPane.YES_NO_OPTION);
				if(odp==JOptionPane.YES_OPTION)
					SaveChanges();
					return;
			}
			else
			{
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
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Nazwa pliku nie mo¿e byæ pusta!", "Wyst¹pi³ b³¹d", JOptionPane.INFORMATION_MESSAGE);
		}
		setVisible(false);
	}
	public void NextNewText(String path)
	{
		Path=path;
		textfield.setText("");
		textarea.setText("");
		setVisible(true);
	}
	/*metody do wyszukiwania zaawansowanego*/
	private void configureSearchingView()
	{
		setSize(250,450);
		setResizable(false);
		setLayout(null);
		configureSearchingTextfields();
		configureSearchingButtons();
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	private void configureSearchingTextfields()
	{
		textfield = new JTextField();
		textfield.setBounds(50,10,150,30);
		textfield.setToolTipText("Tu wpisz wyszukiwan¹ frazê.");
		add(textfield);
		
		textarea = new JTextArea();
		textarea.setEditable(false);
		JScrollPane sp = new JScrollPane(textarea);
		sp.setBounds(50,50,150,300);
		add(sp);
	}
	private void configureSearchingButtons()
	{
		bOK = new JButton("Szukaj");
		bOK.setBounds(10,360,100,40);
		bOK.addActionListener(this);
		add(bOK);
		
		bCancel= new JButton("Anuluj");
		bCancel.setBounds(130,360,100,40);
		bCancel.addActionListener(this);
		add(bCancel);
	}
	private void Search()
	{
		String msg="To opcja przeszukuje wszystkie pliki w folderze linia po lini.\n"
				+ "Przy folderach z du¿¹ liczb¹ d³ugich plików tekstowych mo¿e trwaæ to bardzo d³ugo.\n"
				+"Operacja nie mo¿e zostaæ przerwana przed zakoñczeniem wyszukiwania!\n"
				+"Czy nadal chcesz kontynuowaæ?";
		if(JOptionPane.showConfirmDialog(this, msg, "Ostrze¿enie", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
		{
			textarea.setText("");
			if(textfield.getText().isEmpty()==false)
			{
				String searched=textfield.getText();
				File folder= new File(Settings.getPath());
				File listoffiles[] =folder.listFiles();
				for(int i=0;i<listoffiles.length;i++)
				{
					if(IsInFile(listoffiles[i],searched.toLowerCase()))
						textarea.append(listoffiles[i].getName()+'\n');
				}
			}
		}
	}
	private boolean IsInFile(File file,String s)
	{
		String AllText="";
		if(file.exists())
		{
			try {
				Scanner fileReader = new Scanner(file);
				while(fileReader.hasNext())
				{
					AllText+=fileReader.nextLine().toLowerCase() + " ";
					if(AllText.indexOf(s)!=-1)
					{
						fileReader.close();
						return true;
					}
				}
				fileReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
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
					SaveChanges();
				break;
				case 2:
					AddNewText();
				break;
				case 3:
					Search();
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
