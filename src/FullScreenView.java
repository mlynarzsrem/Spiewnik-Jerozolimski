import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class FullScreenView extends JDialog implements KeyListener,MouseWheelListener ,ActionListener{
	private static final long serialVersionUID = -5381313303663242848L;
	private JTextArea textfield;
	private JPopupMenu popupmenu;
	private JMenuItem mIncFontSize,mDecFontSize,mSetFontSize,mBegining;
	private String filename;
	private Settings settings;
	public FullScreenView(JFrame owner,String f,Settings fs)
	{
		super(owner,"Pe³ny ekran",true);
		addKeyListener(this);
		addMouseWheelListener(this);
		Dimension d =Toolkit.getDefaultToolkit().getScreenSize();
		setSize(d.width,d.height);
		setResizable(false);
		configureTextfield();
		configurePopupMenu();
		settings=fs;
		filename=f;
		ChangeText(filename);	
	}
	private void configureTextfield()
	{
		textfield = new JTextArea();
		textfield.addKeyListener(this);
		textfield.addMouseWheelListener(this);
		textfield.setMargin(new Insets(5,20,0,20));
		textfield.setEditable(false);
		textfield.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)textfield.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane sp = new JScrollPane(textfield);
		sp.addKeyListener(this);
		add(sp);
	}
	private void configurePopupMenu()
	{
		mIncFontSize = new JMenuItem("Zwiêksz czcionkê");
		mIncFontSize.addActionListener(this);
		mDecFontSize = new JMenuItem("Zmniejsz czcionkê");
		mDecFontSize.addActionListener(this);
		mSetFontSize = new JMenuItem("Ustaw rozmiar czcionki");
		mSetFontSize.addActionListener(this);
		mBegining= new JMenuItem("Wróæ na pocz¹tek");
		mBegining.addActionListener(this);
		
		popupmenu = new JPopupMenu();
		popupmenu.add(mIncFontSize);
		popupmenu.add(mDecFontSize);
		popupmenu.add(mSetFontSize);
		popupmenu.add(mBegining);
		
		textfield.setComponentPopupMenu(popupmenu);
	}
	private void IncrementFontSize(boolean up)
	{
		Font actFont=textfield.getFont();
		int fsize=actFont.getSize();
		int style=actFont.getStyle();
		String fontname=actFont.getFontName();
		if(up==true)
			fsize++;
		else
		{
			if(fsize>2)
				fsize--;
		}
		settings.setFontSize(fsize);
		textfield.setFont(new Font(fontname,style,fsize));
	}
	private void setNewFontSize()
	{
		Font actFont=textfield.getFont();
		int fsize=actFont.getSize();
		int style=actFont.getStyle();
		String fontname=actFont.getFontName();
		String newFontsize=JOptionPane.showInputDialog(this, "Podaj rozmiar czcionki",Integer.toString(fsize));
		if(newFontsize!=null)
		{
			fsize=Others.IsInteger(newFontsize);
			if(fsize!=-1)
			{
				textfield.setFont(new Font(fontname,style,fsize));
				settings.setFontSize(fsize);
			}
			else
				JOptionPane.showMessageDialog(this, "Nieprawid³owy format czcionki","Wyst¹pi³ b³¹d!",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void ChangeText(String f)
	{
		filename=f;	
		importText();
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);	
	}
	private void importText()
	{
		textfield.setFont(settings.getBoldFont());
		textfield.setForeground(settings.getFontColor());
		textfield.setText("");
		File file= new File(this.filename);
		if(file.exists())
		{
			try 
			{
				Scanner fileReader= new Scanner(file);
				while(fileReader.hasNext())
				{
					textfield.append(fileReader.nextLine().trim()+"\n");
				}
				fileReader.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		textfield.setCaretPosition(0);
		textfield.setRows(textfield.getLineCount()*3);
	}
	/*Listenrr*/
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
			setVisible(false);
		if(e.getKeyCode()==KeyEvent.VK_Q)
			IncrementFontSize(true);
		if(e.getKeyCode()==KeyEvent.VK_W)
			IncrementFontSize(false);
		if(e.getKeyCode()==KeyEvent.VK_SPACE||e.getKeyCode()==KeyEvent.VK_SHIFT)
			textfield.setCaretPosition(0);
		
	}
	@Override
	public void keyReleased(KeyEvent e) {

		
	}
	@Override
	public void keyTyped(KeyEvent e) {

			
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
	       int notches = e.getWheelRotation();
	       if (notches < 0) { //up
	    	   IncrementFontSize(true);
	       } else { //down
	    	   IncrementFontSize(false);

	       }
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object z=e.getSource();
		if(z==mIncFontSize)
			IncrementFontSize(true);
		else if(z==mDecFontSize)
			IncrementFontSize(false);
		else if(z==mSetFontSize)
			setNewFontSize();
		else if(z==mBegining)
			textfield.setCaretPosition(0);
	}

}
