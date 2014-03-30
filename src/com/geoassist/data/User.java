package com.geoassist.data;
public class User {
	
	private static User mInstance = null;
	
	public static User getInstance(){
		if(mInstance == null) {
            mInstance = new User();
        }
        return mInstance;
   }
}
