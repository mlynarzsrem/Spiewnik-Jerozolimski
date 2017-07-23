
public class Others {
	public static int IsInteger(String s)
	{
		s=s.trim();
		if(s.indexOf(" ")!=-1)
		return -1;		
		if(s.charAt(0)=='0'&&s.length()!=1)
		return -1;
		for(int i=0;i<s.length();i++)
		{
			if(Character.isDigit(s.charAt(i))==false)
				return -1;
		}
		return Integer.parseInt(s);
		
	}

}
