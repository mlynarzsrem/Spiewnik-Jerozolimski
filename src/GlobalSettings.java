import java.io.File;
import java.io.Serializable;


public class GlobalSettings implements Serializable {
	private static final long serialVersionUID = -1588202610455900646L;
	private String Path;
	GlobalSettings()
	{
		setDefault();
	}
	public void setDefault()
	{
		File folder= new File("Songs");
		if(folder.isDirectory()&&folder.exists())
		this.Path=folder.getAbsolutePath();
		else
		{
				folder.mkdir();
				this.Path=folder.getAbsolutePath();
		}
	}
	public void setPath(String p)
	{
		this.Path=p;
	}
	public String getPath()
	{
		return new String(Path);
	}

    public GlobalSettings clone()  
	{
    	GlobalSettings klon = new GlobalSettings();
    	klon.setPath(getPath());
		return klon;
	}
	@Override
	public String toString() {
		return new StringBuffer(" Path : ")
				.append(this.Path).toString();
	}
	public void copyValuesOf(GlobalSettings s)
	{
    	this.Path=s.getPath();
	}


}
