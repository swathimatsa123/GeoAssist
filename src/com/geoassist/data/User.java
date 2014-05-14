package com.geoassist.data;
public class User {
	
	private static User mInstance = null;
	public String userName;
	public String emailTo;
	public String emailCC;
	
	public static User getInstance(){
		if(mInstance == null) {
            mInstance = new User();
        }
        return mInstance;
   }
	public void setDetails(String usrName,String emailTo, String emailCC) {
		this.userName = usrName;
		this.emailTo = emailTo;
		this.emailCC = emailCC;
	}
}
