package com.auisy.AccountManagement.Decryption;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TpapDecryption {

	public static String decrypt(String cipherText, String keyString) throws Exception {
		byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length != 32) {
			throw new IllegalArgumentException("AES-256 key must be 32 bytes");
		}

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		byte[] decoded = Base64.getDecoder().decode(cipherText);

		byte[] iv = new byte[12];
		byte[] encrypted = new byte[decoded.length - 12];
		System.arraycopy(decoded, 0, iv, 0, 12);
		System.arraycopy(decoded, 12, encrypted, 0, encrypted.length);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(128, iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

		byte[] decrypted = cipher.doFinal(encrypted);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

	public static void main(String[] args) {

		try {
			// Every Auth-ID has Separate Tpap Transaction Key
			// T PAP Transaction Key
			// String TpapTransactionKey = "jP4vB2nk2IX1Xq8Wf6bz7Gn7vO7Su8ln";
			// Take it from Backend or After Tpap On Boarding Mail will be sent and Details
			// available in Mail

			String TpapTransactionKey = "jP4vB2nk2IX1Xq8Wf6bz7Gn7vO7Su8ln"; // TPSM000091

			String EncryptResponseAfterPaymentDone = "5OeoUxd9O0occrtdDjvTGWliSxAzDQbVMh7O+wqCeYdoiec+DzS067o1tvNF/BLdDA4vi4+6b5CAxQOzAY0R1K2XLdinoSUNmhGkpTN2YWgscABNWuHFytlrMD9Ke2b0muKID85crIMFm9Z+1wQ4EWzg0K7yRrYg5+XWpYYUGeMzICzALj4GzZd9bdTNGmQD47ILzVgbK3zN5l+W8SkVx/UD5F9vewXyj8srFugvdb3wd7p/n9S08NNxw+QCnQ8zz5to0d/Rj3rBGA==";

			// For Decryption, We Need Request And Tpap Transaction Key
			String Decrypt = decrypt(EncryptResponseAfterPaymentDone, TpapTransactionKey);
			System.out.println("After Decryption JSON API Response :" + "\n" + Decrypt + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
