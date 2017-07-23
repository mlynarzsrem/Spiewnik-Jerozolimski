import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsManager {

	public static void SaveToFile(GlobalSettings gs,Settings fss,Settings vbs)  throws IOException, ClassNotFoundException
	{
		  File f=new File("settings.dat");
		  f.delete();
	      FileOutputStream fos = new FileOutputStream("settings.dat");
	      ObjectOutputStream oos = new ObjectOutputStream(fos);
	      oos.writeObject(gs);
	      oos.writeObject(fss);
	      oos.writeObject(vbs);
	      oos.close();
	      fos.close();
	}
	public static void ReadFromFile(GlobalSettings gs,Settings fss,Settings vbs)  throws IOException, ClassNotFoundException
	{
		FileInputStream fos = new FileInputStream("settings.dat");
		ObjectInputStream oos = new ObjectInputStream(fos);
		
		gs.copyValuesOf((GlobalSettings) oos.readObject());
		fss.copyValuesOf((Settings) oos.readObject());
		vbs.copyValuesOf((Settings) oos.readObject());
		
		oos.close();
		fos.close();
	}
}
