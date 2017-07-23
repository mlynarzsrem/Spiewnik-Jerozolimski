import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;


public class BlackScreen extends JDialog implements KeyListener{

	private static final long serialVersionUID = 1L;

	public BlackScreen(JFrame owner)
	{
		super(owner,"Black Screen",true);
		Dimension d =Toolkit.getDefaultToolkit().getScreenSize();
		setSize(d.width,d.height);
		addKeyListener(this);
		setUndecorated(true);
		setBlankCursor(); 
		getContentPane().setBackground(Color.BLACK);
		setVisible(true);
		setResizable(false);
		
	}
	private void setBlankCursor() 
	{
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");
		this.setCursor(blankCursor);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
		{
			setVisible(false);
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
