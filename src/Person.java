import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class Person implements Serializable
{
        static final long longserialVersionUID = 1L;
    
	String ID;
        String cardNumber;
	String year;
	String name=null;
	String classes[][]=new String[5][13];
	
	public static int number=0;
        
        public static void setNumber(int n)
        {
            number=n;
        }
	
	Person(String ID,String year) throws MalformedURLException, UnknownHostException, Exception
	{
		number++;
		this.ID=ID;
		this.year=year;
		for (int i=0;i<5;i++)
			for (int j=0;j<13;j++) classes[i][j]=null;
                if (ID.length()<8)
                {
                    this.cardNumber="220"+ID;
                    return;
                }
		try
		{
			String strURL="http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action?queryAcademicYear="
					+this.year+"&queryStudentId="+this.ID;
			URL coseURL=new URL(strURL);
			URLConnection connection=coseURL.openConnection();
			BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String html=in.readLine();
			while (html!=null)
			{
				if (html.indexOf("姓名:")!=-1)
				{
					String str="姓名:";
					int beginInx=html.indexOf("姓名:")+str.length();
					name=html.substring(beginInx,html.indexOf("</td>",beginInx));
				}
                                if (html.indexOf("一卡通号:")!=-1)
				{
					String str="一卡通号:";
					int beginInx=html.indexOf("一卡通号:")+str.length();
					cardNumber=html.substring(beginInx,html.indexOf("</td>",beginInx));
				}
				if (html.indexOf("上午")!=-1 || html.indexOf("下午")!=-1 || html.indexOf("晚上")!=-1)
					{
						html=in.readLine();
						for (int i=0;i<5;i++)
						{
							html=in.readLine();
							int beginInx=html.indexOf("align=\"center\">")+15;
							int brInx=html.indexOf("<br>",beginInx);
							while (brInx!=-1)
							{
								String name=null;
								
								//课程名字
								if (name==null) name=html.substring(beginInx,brInx);
								else name+=html.substring(beginInx,brInx);
								beginInx=brInx+4;
								brInx=html.indexOf("<br>",beginInx);
								
								//课程时间
								int inx=html.indexOf("]",beginInx)+1;
								name+=html.substring(beginInx,inx);
								int begin=0,end=0,x=0;
								for (int j=inx;j<brInx;j++)
								{
									char ch=html.charAt(j);
									if (ch>='0' && ch<='9') x=x*10+ch-'0';
									else
										if (ch=='-') 
										{
											begin=x;
											x=0;
										}
										else 
										{
											end=x;
											break;
										}
								}
								for (int j=begin-1;j<end;j++)
									if (classes[i][j]==null) classes[i][j]=name;
									else classes[i][j]+=name;
								beginInx=brInx+4;
								brInx=html.indexOf("<br>",beginInx);
								
								//课程地点（暂未处理）
								beginInx=brInx+4;
								brInx=html.indexOf("<br>",beginInx);
							}
						}
					}
					html=in.readLine();
				}
                        if (name==null) throw new Exception("连接出错");
		}
                catch(UnknownHostException e)
                {
                    throw e;
                }
		catch(IOException e)
		{
                    e.printStackTrace();
                    throw e;
		}
	}
	
	public void print()
	{
		System.out.println(name);
		for (int i=0;i<5;i++)
		{
			for (int j=0;j<13;j++)
				System.out.print(classes[i][j]+'\t');
			System.out.println();
		}
	}
        
        public void setBusy(int i,int j)
        {
            classes[i][j]="busy";
        }
        
        public String getID() {
                return ID;
        }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[][] getClasses() {
		return classes;
	}

	public void setClasses(String[][] classes) {
		this.classes = classes;
	}

	public String getCardNumber() {
                return cardNumber;
        }
        
//	public static void main(String[] args)
//	{
//		Person p=new Person("71115134","16-17-3");
//		p.print();
//	}
}
