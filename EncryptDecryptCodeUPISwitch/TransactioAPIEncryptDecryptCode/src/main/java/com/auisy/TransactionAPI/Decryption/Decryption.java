package com.auisy.TransactionAPI.Decryption;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decryption {

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
			// Every Auth-ID has Separate Merchant Transaction Key
			// Merchant Transaction Key
			// String MerchantTransactionKey = "jx2Au8Rt8gR8qA4zG4zh1HT6Lp7rT2MH";
			// Path : Merchant Login => My Account => Transaction Key

			String MerchantTransactionKey = "jx2Au8Rt8gR8qA4zG4zh1HT6Lp7rT2MH";
			String EncryptResponseAfterPaymentDone = "J3QBTg2fJUo1EuHwLCTFgqz43JLDpiBTxZw3aE0Og/EYVPi33SUFzIyQncCsGVsfOc+ypVPTqNF8zQbQniVhXh8I2pcJjGDXMGusqXtjFHlUDXzx9COeQ45lq7WJbM6NnEUGPFL2f2OepUWIGjxak6FacOJ68n7XlYHOqJRb7wJJkkISlZYFMxxvaRbzZUjL9srNxu5qs8wRgN+lkWehkXZB44yuR/zogpsQxrdGvmzsGpwEpS5iA2hqTdycCgHPGsge2/fdhl+KCAFYZDbYkeJOBktx0lPkqB3quWAbGUU5Dvi/TZHkn/8bUnJ2O+jo+qNx7C8Lii8sMSzJ8vvBOdzW8+rssHDQq23TqsdKkCLsN6lqAS7A5qLJ";

			// For Decryption, We Need Request And Merchant Transaction Key
			String Decrypt = decrypt(EncryptResponseAfterPaymentDone, MerchantTransactionKey);
			System.out.println("After Decryption JSON API Response :" + "\n" + Decrypt + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
