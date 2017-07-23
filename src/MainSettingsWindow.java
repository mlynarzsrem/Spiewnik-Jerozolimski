import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MainSettingsWindow extends JDialog implements ActionListener,WindowListener {
	private static final long serialVersionUID = -6459770130486247593L;
	private GlobalSettings globalSettings;
	private Settings fullscreenSettings, viewboxSettings;
	/*JLabel*/
	JLabel lgsTitle,lfssTitle,lvbsTitle,lPath,lfssFontSize,
	lfssFontColor,lfssFont,lvbsFontSize,lvbsFontColor,lvbsFont;
	/*JTextField*/
	JTextField tPath,tfssFontSize,tvbsFontSize;
	/*JButton*/
	JButton bOK,bCancel,bsDefault,bChoosePath,bfssColor,bvbsColor;
	/*JComboBox*/
	JComboBox<String> cbFSSFont,cbVBSFont;
	/*Temp vairables*/
	Color tempColFSS,tempColVBS; 
	/*Wykonwyane tylko raz*/
	MainSettingsWindow(JFrame owner,GlobalSettings gs,Settings fss,Settings vbs)
	{
		super(owner,"Ustawienia",true);
		addWindowListener(this);
		/*Import ustawieñ*/
		globalSettings=gs;
		fullscreenSettings=fss;
		viewboxSettings=vbs;
		/*Ustawienia okna*/
		setSize(600,500);
		setLocation(300,200);
		this.setLayout(null);
		setResizable(false);
		configureLabels();
		configureTextFields();
		configureComboBoxes();
		configureButtons();
		setCurrentValues();
		setVisible(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	private void configureLabels()
	{
		/*Ustawienia ogólne*/
		lgsTitle = new JLabel("Ustawienia ogólne",JLabel.CENTER);
		lgsTitle.setBounds(0,10,500,30);
		add(lgsTitle);
		
		lPath = new JLabel("Folder z pieœniami:",JLabel.RIGHT);
		lPath.setBounds(0,50,150,20);
		add(lPath);
		/*Ustawienia pelnoekranowe*/
		lfssTitle = new JLabel("Ustawienia pe³noekranowe",JLabel.CENTER);
		lfssTitle.setBounds(0,100,500,30);
		add(lfssTitle);
		
		lfssFontSize = new JLabel("Rozmiar czcionki:",JLabel.RIGHT);
		lfssFontSize.setBounds(0,140,150,20);
		add(lfssFontSize);
		
		lfssFontColor = new JLabel("Kolor czcionki:",JLabel.RIGHT);
		lfssFontColor.setBounds(0,170,150,20);
		add(lfssFontColor);
		
		lfssFont = new JLabel("Czcionka:",JLabel.RIGHT);
		lfssFont.setBounds(0,200,150,20);
		add(lfssFont);
		/*Ustawienia podgl¹du*/
		lvbsTitle = new JLabel("Ustawienia podgl¹du",JLabel.CENTER);
		lvbsTitle.setBounds(0,240,500,30);
		add(lvbsTitle);
		
		lvbsFontSize = new JLabel("Rozmiar czcionki:",JLabel.RIGHT);
		lvbsFontSize.setBounds(0,280,150,20);
		add(lvbsFontSize);
		
		lvbsFontColor = new JLabel("Kolor czcionki:",JLabel.RIGHT);
		lvbsFontColor.setBounds(0,310,150,20);
		add(lvbsFontColor);
		
		lvbsFont = new JLabel("Czcionka:",JLabel.RIGHT);
		lvbsFont.setBounds(0,340,150,20);
		add(lvbsFont);
	}
	private void configureTextFields()
	{
		tPath = new JTextField();
		tPath.setBounds(160, 50, 250, 20);
		tPath.setEditable(false);
		add(tPath);
		
		tfssFontSize = new JTextField();
		tfssFontSize.setBounds(160, 140, 50, 20);
		add(tfssFontSize);
		
		tvbsFontSize = new JTextField();
		tvbsFontSize.setBounds(160, 280, 50, 20);
		add(tvbsFontSize);
	}
	private void configureComboBoxes()
	{
		cbFSSFont = new JComboBox<>();
		cbFSSFont.setBounds(160,200,200,20);
		cbFSSFont.addItem("SansSerif");
		cbFSSFont.addItem("Helvetica");
		add(cbFSSFont);
		
		cbVBSFont= new JComboBox<>();
		cbVBSFont.setBounds(160,340,200,20);
		cbVBSFont.addItem("SansSerif");
		cbVBSFont.addItem("Helvetica");
		add(cbVBSFont);
	}
	private void configureButtons()
	{
		/*Color chosery*/
		bfssColor = new JButton("");
		bfssColor.setBounds(160, 170, 50, 20);
		bfssColor.addActionListener(this);
		add(bfssColor);
		
		bvbsColor = new JButton("");
		bvbsColor.setBounds(160, 310, 50, 20);
		bvbsColor.addActionListener(this);
		add(bvbsColor);
		/*path*/
		bChoosePath = new JButton("wybierz");
		bChoosePath.setBounds(420, 50, 100, 20);
		bChoosePath.addActionListener(this);
		add(bChoosePath);
		/*rest*/
		bOK= new JButton("Zapisz");
		bOK.setBounds(30, 380, 100, 40);
		bOK.addActionListener(this);
		add(bOK);
		bCancel= new JButton("Anuluj");
		bCancel.setBounds(150, 380, 100, 40);
		bCancel.addActionListener(this);
		add(bCancel);
		
		bsDefault= new JButton("Domyœlne");
		bsDefault.setBounds(270, 380, 100, 40);
		bsDefault.addActionListener(this);
		add(bsDefault);
	}
	/*Wykonywane wiecj ni¿ raz*/
	private void setCurrentValues()
	{
		/*Color*/
		tempColFSS=fullscreenSettings.getFontColor();		
		bfssColor.setBackground(tempColFSS);
		
		tempColVBS=viewboxSettings.getFontColor();
		bvbsColor.setBackground(tempColVBS);
		/*textfields*/
		tPath.setText(new String(globalSettings.getPath()));
		tfssFontSize.setText(Integer.toString(fullscreenSettings.getFontSize()));
		tvbsFontSize.setText(Integer.toString(viewboxSettings.getFontSize()));
		/*comboboxy*/
		cbFSSFont.setSelectedItem(fullscreenSettings.getFontName());
		cbVBSFont.setSelectedItem(viewboxSettings.getFontName());
	}
	private void choosePath()
	{
		JFileChooser chooser;
		chooser=new JFileChooser();
		chooser.setCurrentDirectory(new File(globalSettings.getPath()));
		chooser.setDialogTitle("Wybierz folder");
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
		{ 
			tPath.setText(chooser.getSelectedFile().toString());
		}	
	}
	private void chooseFontColor(boolean fs)
	{
		if(fs==true)
		{	
			tempColFSS=JColorChooser.showDialog(null, "Wybierz kolor", tempColFSS);	
			bfssColor.setBackground(tempColFSS);
		}
		else
		{
			tempColVBS=JColorChooser.showDialog(null, "Wybierz kolor", tempColVBS);
			bvbsColor.setBackground(tempColVBS);
		}
	}
	private void saveChanges()
	{		
		int value=Others.IsInteger(tfssFontSize.getText());
		if(value==-1)
		{
			JOptionPane.showMessageDialog(null, "Niepoprawny rozmaiar czcionki", "B³ad", JOptionPane.ERROR_MESSAGE); 
			return;
		}		
		value=Others.IsInteger(tvbsFontSize.getText());
		if(value==-1)
		{
			JOptionPane.showMessageDialog(null, "Niepoprawny rozmaiar czcionki", "B³ad", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		globalSettings.setPath(tPath.getText());
		
		fullscreenSettings.setFontName(cbFSSFont.getSelectedItem().toString());
		fullscreenSettings.setFontColor(tempColFSS);
		fullscreenSettings.setFontSize(Others.IsInteger(tfssFontSize.getText()));

		viewboxSettings.setFontName(cbVBSFont.getSelectedItem().toString());
		viewboxSettings.setFontColor(tempColVBS);
		viewboxSettings.setFontSize(Others.IsInteger(tvbsFontSize.getText()));

		setCurrentValues();
		//zapis do pliku
		try {
			SettingsManager.SaveToFile(globalSettings, fullscreenSettings, viewboxSettings);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setVisible(false);
	}
	private void setDefault()
	{
		/*Tworznie klona plus default*/
		GlobalSettings gs;
		Settings fss, vbs;
		gs=globalSettings.clone();
		gs.setDefault();
		fss=fullscreenSettings.clone();
		fss.setDefault();
		vbs=viewboxSettings.clone();
		vbs.setDefault();
		/*TWyœwietlanie infrmacji*/
		/*Color*/
		tempColFSS=fss.getFontColor();
		bfssColor.setBackground(tempColFSS);
		
		tempColVBS=vbs.getFontColor();
		bvbsColor.setBackground(tempColVBS);
		/*textfields*/
		tPath.setText(gs.getPath());
		tfssFontSize.setText(Integer.toString(fss.getFontSize()));
		tvbsFontSize.setText(Integer.toString(vbs.getFontSize()));
		/*comboboxy*/
		cbFSSFont.setSelectedItem(fss.getFontName());
		cbVBSFont.setSelectedItem(vbs.getFontName());
	}
	/*aCTION LISTENER*/
	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		if(z ==bCancel)
		{
			setCurrentValues();
			setVisible(false);
		}
		else if(z==bChoosePath)
			choosePath();
		else if(z==bfssColor)
			chooseFontColor(true);
		else if(z==bvbsColor)
			chooseFontColor(false);
		else if(z==bOK)
		{
			saveChanges();
			setCurrentValues();
		}
		else if(z==bsDefault)
			setDefault();
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		setCurrentValues();
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
