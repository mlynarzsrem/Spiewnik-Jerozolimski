import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

public class Settings implements Serializable{
	private static final long serialVersionUID = -7525818590524942263L;
	private int FontSize;
	private String FontName;
	private Color FontColor;
	public Settings()
	{
		setDefault();
	}
	public void setDefault()
	{
		this.FontSize=12;
		this.FontColor=Color.BLACK;
		this.FontName="SansSerif";
		
	}
	public void setFontSize(int fs)
	{
		this.FontSize=fs;
	}
	public void setFontName(String f)
	{
		this.FontName=f;
	}
	public void setFontColor(Color c)
	{
		this.FontColor=c;
	}
	public int getFontSize()
	{
		return this.FontSize;
	}
	public String getFontName()
	{
		return new String(FontName);
	}
	public Color getFontColor()
	{
		int r,g,b,a;
		r=FontColor.getRed();
		g=FontColor.getGreen();
		b=FontColor.getBlue();
		a=FontColor.getAlpha();
		
		return new Color(r,g,b,a);
	}
	public Font getBoldFont()
	{
		return new Font(this.FontName,Font.BOLD,this.FontSize);
	}
	public Font getPlainFont()
	{
		return new Font(this.FontName,Font.PLAIN,this.FontSize);
	}
    public Settings clone() 
    {
		Settings klon = new Settings();
		klon.setFontName(this.getFontName());
		klon.setFontSize(this.FontSize);
		klon.setFontColor(this.getFontColor());
		return klon;
	}
    public void copyValuesOf(Settings s)
    {
    	this.FontName=new String(s.getFontName());
    	this.FontSize=s.getFontSize();
    	this.FontColor=s.getFontColor();
    }


}
