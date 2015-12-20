package xyz.sangcomz.sangcomz_n_study.core.Security;


public class Security {
	public static String MD5(String src, String def){
		if(src != null && src.length()>0){
			java.security.MessageDigest md5 = null;
			try{
				md5 = java.security.MessageDigest.getInstance("MD5");
			}catch(Exception e){
				return def;
			}
			
			String eip;
			byte[] bip;
			String temp = "";
			String tst = src;
			
			bip = md5.digest(tst.getBytes());
			for(int i=0;i<bip.length;i++){
				eip = "" + Integer.toHexString((int) bip[i] & 0x000000ff);
				if(eip.length()<2) eip = "0" + eip;
				temp = temp + eip;
			}
			return temp;
		}else{
			return def;
		}
	}
}
