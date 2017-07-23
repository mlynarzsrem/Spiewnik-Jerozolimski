import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MainWindow extends JFrame implements ActionListener , MouseListener,KeyListener,WindowListener
{
	private static final long serialVersionUID = 4939884085632438880L;
	/*JDialog*/
	BlackScreen blackscreen;
	MainSettingsWindow settingsWindow;
	FullScreenView fullscreenview;
	EditNewSearchWindow newfilewindow;
	EditNewSearchWindow editwindow;
	EditNewSearchWindow searchwindow;
	/*Table*/
	private MyModel TabModel; 
	private JTable FileList; 
	/*Textfileds*/
	private JTextField tfSearch;
	private JTextArea  taView;
	/*Menu*/
	private JMenuBar menubar;
	private JMenu mFile,mTools,mInfo;
	private JMenuItem mfOpenFile,mfNewFile,mfEdit,mfExit,mtSettings,mtSearch,mtBlackScreen,mtFullScreen,miAboutProgram,miHelp;
	/*JPopupMemu*/
	private JPopupMenu filelistpopupmenu,viewpopupmenu;
	private JMenuItem pmNewFile,pmEditFile,pmOpenFile,pvIncFontSize,pvDecFontSize,pvSetFontSize;
	/*Othes*/
	private String AboutProgram="To jest œpiewnik Wspólnoty Jerozolima.\n Aplikacja zosta³a stworzona przez Jakuba M³ynarza.\n Wszelkie prawa zastrze¿one \n Wersja 1.0.0 ";
	/*Setings*/
	private Settings ViewBoxSettings,fsSettings; 
	private GlobalSettings globalSettings;
	/*Functions*/
	/*Metody konfiguracyjne wykonywane tylko raz*/
	public MainWindow()
	{
		/*Konfiguracja podstawowa*/
		setTitle("Œpiewnik Jerozolimski");
		setSize(800,600);
		setLocation(300,200);
		setResizable(false);
		addWindowListener(this);
		/*Konfiguracja ustawiên*/
		ViewBoxSettings = new Settings();
		fsSettings = new Settings();
		globalSettings = new GlobalSettings();
		ImportSettings();
		File folder= new File(globalSettings.getPath());
		if(!folder.exists()) //w przypadku nie poprawnych ustawien
			globalSettings.setDefault();
		/*Konfiguracja wygl¹du*/
		setLayout(null);
		configureMenus(); //rozmieszczanie obiektów menu
		configureTextFields(); //rzmieszczanie pól tekstowych i tabeli z list¹ plików
		configureFileListPopupMenu();
		confgureViewPopupMenu();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ShowAllFiles();
	}
	public static void main(String[] args) {
		MainWindow mainwindow= new MainWindow();
		mainwindow.setVisible(true);
		
	}
	private void confgureViewPopupMenu()
	{
		pvIncFontSize = new JMenuItem("Zwiêksz czcionkê");
		pvIncFontSize.addActionListener(this);
		pvDecFontSize = new JMenuItem("Zmniejsz czcionkê");
		pvDecFontSize.addActionListener(this);
		pvSetFontSize = new JMenuItem("Ustaw rozmiar czcionki");
		pvSetFontSize.addActionListener(this);

		viewpopupmenu = new JPopupMenu();
		viewpopupmenu.add(pvIncFontSize);
		viewpopupmenu.add(pvDecFontSize);
		viewpopupmenu.add(pvSetFontSize);
		
		taView.setComponentPopupMenu(viewpopupmenu);
	}
	private void configureFileListPopupMenu()
	{
		pmNewFile = new JMenuItem("Nowy plik");
		pmNewFile.addActionListener(this);
		pmEditFile = new JMenuItem("Edytuj plik");
		pmEditFile.addActionListener(this);
		pmOpenFile = new JMenuItem("Pe³en ekran");
		pmOpenFile.addActionListener(this);
		
		filelistpopupmenu = new JPopupMenu();
		//popupmenu.add(pmNewFile);
		filelistpopupmenu.add(pmEditFile);
		filelistpopupmenu.add(pmOpenFile);
		
		//FileList.setComponentPopupMenu(popupmenu);
	}
	private void configureMenus()
	{
		/*Menus*/
		mFile= new JMenu("Plik");
		mTools= new JMenu("Narzêdzia");
		mInfo= new JMenu("Informacje");
		/*MenuBar*/
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		menubar.add(mFile);
		menubar.add(mTools);
		menubar.add(mInfo);
		/*MenuItems*/
		/*File menu*/
		mfOpenFile = new JMenuItem("Otwórz");
		mfOpenFile.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		mfOpenFile.addActionListener(this);
		mfNewFile = new JMenuItem("Nowy");
		mfNewFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		mfNewFile.addActionListener(this);
		mfEdit = new JMenuItem("Edytuj");
		mfEdit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		mfEdit.addActionListener(this);
		mfExit = new JMenuItem("WyjdŸ");
		mfExit.addActionListener(this);
		mFile.add(mfOpenFile);
		mFile.add(mfNewFile);
		mFile.add(mfEdit);
		mFile.addSeparator();
		mFile.add(mfExit);
		/*ToolsMenu*/
		mtSettings=new JMenuItem("Ustawienia");
		mtSettings.addActionListener(this);
		mtBlackScreen=new JMenuItem("Czarny ekran");
		mtBlackScreen.addActionListener(this);
		mtBlackScreen.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
		mtFullScreen=new JMenuItem("Pe³ny ekran");
		mtFullScreen.addActionListener(this);
		mtFullScreen.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
		mtSearch=new JMenuItem("Szuaknie zaawansowane");
		mtSearch.addActionListener(this);
		mTools.add(mtSettings);
		mTools.add(mtFullScreen);
		mTools.add(mtSearch);
		mTools.add(mtBlackScreen);
		/*Info Menu*/
		miAboutProgram=new JMenuItem("O programie");
		miAboutProgram.addActionListener(this);
		miHelp=new JMenuItem("Pomoc");
		miHelp.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
		miHelp.addActionListener(this);
		mInfo.add(miAboutProgram);
		mInfo.add(miHelp);	
	}
	private void configureTextFields()
	{
		/*Searching input field*/
		tfSearch = new JTextField();
		tfSearch.setBounds(20, 10, 150, 30);
		tfSearch.addActionListener(this);
		tfSearch.addKeyListener(this);
		add(tfSearch);
		/*Table with file list*/
		TabModel = new MyModel(); 
		TabModel.addColumn("Nazwa pliku");
		FileList = new JTable(TabModel);
		FileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FileList.setCursor(new Cursor(Cursor.HAND_CURSOR));
		FileList.addMouseListener(this);
		/*View of songs*/
		taView = new JTextArea();
		taView.setEditable(false);
		taView.setLineWrap(true);
		configureViewBox();
		/*Scrollpane*/
		JScrollPane spFileList = new JScrollPane(FileList);
		spFileList.setBounds(20,50,150,460);
		add(spFileList);
		JScrollPane spView = new JScrollPane(taView);
		spView.setBounds(180, 10,600, 500);
		add(spView);
	}
	/*Wykonywane wiêcej razy*/
	private void UserWantExit()
	{
		int r=JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjœæ?", "Wyjœcie", JOptionPane.YES_NO_OPTION);
		if(r==JOptionPane.YES_OPTION)
		{
			saveSettings();
			dispose();
		}
	}
	/*Dotycz¹ce listy plików*/
	private void UserSearch()
	{
		ClearTable();
		String searched=tfSearch.getText().toLowerCase();
		if(searched.isEmpty())//Je¿eli u¿ytkownik niczego nie szuka wyœwietl wszystkie			
			ShowAllFiles();
		else
		{
			File folder= new File(globalSettings.getPath());
			if(folder.exists()&&folder.isDirectory())
			{
				File [] ListOfFiles= folder.listFiles();
				for(int i=0;i<ListOfFiles.length;i++)
				{
					if(ListOfFiles[i].isFile()&&ListOfFiles[i].getName().indexOf(".txt")!=-1)
					{
						if(ListOfFiles[i].getName().toLowerCase().indexOf(searched)!=-1)
							TabModel.addRow(new Object[] {ListOfFiles[i].getName()});
					}
				}
			}
			else
				globalSettings.setDefault();
		}
	}
	private void ShowAllFiles()
	{
		ClearTable();
		File folder= new File(globalSettings.getPath());
		if(folder.exists()&&folder.isDirectory())
		{
			File [] ListOfFiles= folder.listFiles();
			for(int i=0;i<ListOfFiles.length;i++)
			{
				if(ListOfFiles[i].isFile()&&ListOfFiles[i].getName().indexOf(".txt")!=-1)
				TabModel.addRow(new Object[] {ListOfFiles[i].getName()});
			}
		}
		else
			globalSettings.setDefault();
		
	}
	private void ClearTable() //czysczenie listy plików
	{
		if (TabModel.getRowCount() > 0) {
		    for (int i = TabModel.getRowCount() - 1; i > -1; i--) {
		    	TabModel.removeRow(i);
		    }
		}
	}
	/*Dotycz¹ce podgl¹du*/
	private void ShowContentInViewBox() //wyœwitlanie piosenki w podgl¹dzie
	{
		taView.setText("");
		int row_id=FileList.getSelectedRow();
		String FileName=FileList.getModel().getValueAt(row_id, 0).toString();
		String FileAbsolutePath=globalSettings.getPath()+"\\"+FileName;
		//System.out.println(FileAbsolutePath);
		File file= new File(FileAbsolutePath);
		if(file.exists())
		{
			try {
				Scanner fileReader= new Scanner(file);
				while(fileReader.hasNext())
				{
					taView.append(fileReader.nextLine()+"\n");
				}
				fileReader.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	private void configureViewBox() //ustawienia podgl¹du piosenek
	{
		taView.setMargin(new Insets(20,20,20,20));
		taView.setForeground(ViewBoxSettings.getFontColor());
		taView.setFont(ViewBoxSettings.getPlainFont());
	}
	/*Otwieranie okien Dialogowych*/
	private void openAdvancedSearching()
	{
		if(searchwindow==null)
			searchwindow = new EditNewSearchWindow(this,globalSettings);
		else searchwindow.setVisible(true);
		
	}
	private void openBlackScreenWindow()
	{
		if(blackscreen==null)
		{
			blackscreen = new BlackScreen(this);	
		}
		else
			blackscreen.setVisible(true);
	}
	private void openFullScreenView(File file)
	{
		if(file.exists()==true&&file.isFile()==true)
		{
			if(fullscreenview==null)
			fullscreenview = new FullScreenView(this,file.getAbsolutePath(),fsSettings);
			else fullscreenview.ChangeText(file.getAbsolutePath());
		}
	}
	private void openNewFileWindow()
	{
		if(newfilewindow==null)
		newfilewindow= new EditNewSearchWindow(this,globalSettings.getPath());
		else
		{
			newfilewindow.NextNewText(globalSettings.getPath());
		}
		UserSearch();
	}
	private void openEditingWindow(File editFile)
	{
		if(editFile.exists()==true&&editFile.isFile()==true)
		{
			if(editwindow==null)
				editwindow= new EditNewSearchWindow (this,editFile);
			else
				editwindow.NextEdit(editFile);
			UserSearch();
		}
	}
	private void openSettingsWindow()
	{
		if(settingsWindow==null)
		settingsWindow = new MainSettingsWindow(this,this.globalSettings,this.fsSettings,this.ViewBoxSettings);
		else settingsWindow.setVisible(true);
		configureViewBox();
		UserSearch();
	}
	/*Pomocnicze*/
	private void saveSettings()
	{
		try {
			SettingsManager.SaveToFile(globalSettings, fsSettings, ViewBoxSettings);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private File getSelectedFile()
	{
		File newFile;
		int row_id=FileList.getSelectedRow();
		if(row_id!=-1)
		{
			String FileName=FileList.getModel().getValueAt(row_id, 0).toString();
			String FileAbsolutePath=globalSettings.getPath()+"\\"+FileName;
			newFile = new File(FileAbsolutePath);
		}
		else
		{
			newFile = new File("");
		}
		return newFile;
	}
	private File OpenFile()
	{
		JFileChooser fc= new JFileChooser();
		fc.setDialogTitle("Otwórz plik");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(new File(globalSettings.getPath()));
		fc.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
		int odp=fc.showOpenDialog(this);
		if(odp==JFileChooser.APPROVE_OPTION)
		{
			File editFile=fc.getSelectedFile();
			return editFile;
		}
		return new File("");
	}
	private void ImportSettings()
	{
		File file = new File("settings.dat");
		if(file.exists())
		{
			try {
				SettingsManager.ReadFromFile(globalSettings, fsSettings, ViewBoxSettings);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			saveSettings();
		}
	}
	private void setNewFontSize()
	{
		Font actFont=taView.getFont();
		int fsize=actFont.getSize();
		int style=actFont.getStyle();
		String fontname=actFont.getFontName();
		String newFontsize=JOptionPane.showInputDialog(this, "Podaj rozmiar czcionki",Integer.toString(fsize));
		if(newFontsize!=null)
		{
			fsize=Others.IsInteger(newFontsize);
			if(fsize!=-1)
			{
				taView.setFont(new Font(fontname,style,fsize));
				ViewBoxSettings.setFontSize(fsize);
			}
			else
				JOptionPane.showMessageDialog(this, "Nieprawid³owy format czcionki","Wyst¹pi³ b³¹d!",JOptionPane.ERROR_MESSAGE);
			configureViewBox();
		}
	}
	private void IncrementFontSize(boolean up)
	{
		Font actFont=taView.getFont();
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
		ViewBoxSettings.setFontSize(fsize);
		taView.setFont(new Font(fontname,style,fsize));
		configureViewBox();
	}
	/*Action Listenery*/
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src =e.getSource();
		/*File menu*/
		if(src==mfExit)
			UserWantExit();
		if(src==mfNewFile)
			openNewFileWindow();
		if(src==mfEdit)
			openEditingWindow(OpenFile());
		if(src==mfOpenFile)
			openFullScreenView(OpenFile());
		/*Inforamation Menu*/
		else if(src==miAboutProgram)
			JOptionPane.showMessageDialog(null, AboutProgram, "O programie", JOptionPane.INFORMATION_MESSAGE);
		else if(src==miHelp)
			JOptionPane.showMessageDialog(null, "Ta opcja jest niedostêpna w tej wersji programu!", "Pomoc", JOptionPane.INFORMATION_MESSAGE);
		/*Tools Menu*/
		else if(src==mtFullScreen)
			openFullScreenView(getSelectedFile());
		else if(src==mtBlackScreen)
			openBlackScreenWindow();
		else if(src==mtSearch)
			openAdvancedSearching();
		else if(src==mtSettings)
			openSettingsWindow();
		/*Textfields*/
		else if(src==tfSearch)
			UserSearch();
		/*Popupmenu*/
		else if(src==pmNewFile)
			openNewFileWindow();
		else if(src==pmOpenFile)
			openFullScreenView(getSelectedFile());
		else if(src==pmEditFile)
			openEditingWindow(getSelectedFile());
		else if(src==pvIncFontSize)
			IncrementFontSize(true);
		else if(src==pvDecFontSize)
			IncrementFontSize(false);
		else if(src==pvSetFontSize)
			setNewFontSize();
	}
	/*KeyListener*/
	@Override
	public void keyPressed(KeyEvent arg0) {
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		UserSearch();
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {

	}	
	/*Mouse Listener*/
	@Override
	public void mouseClicked(MouseEvent e) {
		Object z = e.getSource();
		if(z==FileList)
		{
			if(e.getButton()==MouseEvent.BUTTON1)
			{
				if(e.getClickCount() == 2)
					openFullScreenView(getSelectedFile());
				else
					ShowContentInViewBox();
			}
			else if(e.getButton()==MouseEvent.BUTTON3)
				openPopupMaybe(e);

		}

	}
	@Override
	public void mouseEntered(MouseEvent e) {
	
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {

		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		saveSettings();
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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
	private void openPopupMaybe(MouseEvent e)
	{
		int row_focused = FileList.rowAtPoint(e.getPoint());
		int row_selected=FileList.getSelectedRow();
		if(row_focused==row_selected)
		{
			filelistpopupmenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
}
