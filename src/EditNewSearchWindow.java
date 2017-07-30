import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class EditNewSearchWindow extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private int option;
	/*Elementy GUI*/
	private JTextField textfield; 
	private JTextArea textarea;
	private JButton bOK,bCancel,bSetname;
	/*popmenu*/
	private JPopupMenu popupmenu;
	private JMenuItem mCopy,mPaste,mCut;
	/*Pola pomocnicze*/
	private File file;
	private String Path;
	/*Wykonywane tylko raz*/
	public EditNewSearchWindow(JFrame owner, File pfile) 
	{
		super(owner,"Edytuj tekst",true);
		setLocation(300,200);
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
	/*Konfiguracja widoku*/
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
		JLabel filename= new JLabel("�cie�ka do pliku:",JLabel.CENTER);
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
			bSetname =new JButton("Wybierz nazw� pliku");
			bSetname.addActionListener(this); 
			bSetname.setBounds(260,30,150,40);
			add(bSetname);
		} 
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
			importText();
		}			
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	/*Wykonywane przy otwieraniu nowego okna*/
	public void openAnother(File pfile)
	{
		textfield.setText("");
		textarea.setText("");
		if(pfile.isFile())//edycja
		{
			file=pfile;
			textfield.setText(file.getAbsolutePath());
			importText();
		}
		else if(pfile.isDirectory())
			Path=pfile.getAbsolutePath();
		setVisible(true);
	}
	private void importText()
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
			JOptionPane.showMessageDialog(this, "Ten plik nie istnieje!","Wyst�pi� b��d",JOptionPane.ERROR_MESSAGE);
		}
	}
	/*Wykonywane wi�cej razy*/
	/*Wykonywane po reakcji u�ytkownika*/
	private void saveFile()
	{
		if(textfield.getText().isEmpty()==false)
		{
			if(file.exists())
			{
				if(option==2)
				{
					int odp=JOptionPane.showConfirmDialog(this, "Plik ju� istnieje! \n Czy chcesz go nadpisa�?", "Plik istnieje",JOptionPane.YES_NO_OPTION);
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
			JOptionPane.showMessageDialog(this, "Nazwa pliku nie mo�e by� pusta!", "Wyst�pi� b��d", JOptionPane.INFORMATION_MESSAGE);
		}
		setVisible(false);
	}
	private void setFileName()
	{
		JFileChooser fc= new JFileChooser();
		fc.setCurrentDirectory(new File(Path));
		fc.setDialogTitle("Wybierz nazw� pliku");
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
			if(option==1||option==2)
					saveFile();
		}
		else if(z==bCancel)
			setVisible(false);
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
}
