package com.carlesramos.utilities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Lib {
	public static String obtenerSHA1(String nickName, String password) {
		String sha1 = null;
		StringBuilder sb = new StringBuilder();
		sb.append(nickName);
		sb.append(password);
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(sb.toString().getBytes("UTF-8"));
			sha1 = String.format("%040x", new BigInteger(1,digest.digest()));
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return sha1;
	}
}
