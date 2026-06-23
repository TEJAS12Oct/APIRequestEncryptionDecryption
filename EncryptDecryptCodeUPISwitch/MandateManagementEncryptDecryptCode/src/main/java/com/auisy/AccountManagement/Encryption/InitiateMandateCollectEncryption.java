
package com.auisy.AccountManagement.Encryption;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class InitiateMandateCollectEncryption {

	private static final String AES = "AES";
	private static final String AES_GCM = "AES/GCM/NoPadding";
	private static final int KEY_SIZE = 256;
	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12;

	private static final SecureRandom secureRandom = new SecureRandom();

	public static String generateAESKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance(AES);
		keyGen.init(KEY_SIZE);
		SecretKey key = keyGen.generateKey();
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public static String encrypt(String plainText, String keyString) throws Exception {
		byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length != 32) {
			throw new IllegalArgumentException("AES-256 key must be 32 bytes");
		}

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);

		byte[] iv = new byte[IV_LENGTH_BYTE];
		secureRandom.nextBytes(iv);

		Cipher cipher = Cipher.getInstance(AES_GCM);
		GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

		byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

		byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
		System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

		return Base64.getEncoder().encodeToString(encryptedWithIv);
	}

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

			String Request = """
					{
						 "customerId": "NA",
						 "requestId": "REQ1206202603",
						 "tpapId": "M0000118",
						 "deviceInfo": {
						 "mobile": "7420111111",
						 "os": "NA",
						 "appVersion": "NA",
						 "type": "MOB",
						 "geoCode": "NA",
						 "location": "NA",
						 "ip": "192.168.1.1",
						 "id": "NA",
						 "app": "NA",
						 "capability": "NA",
						 "telecom": "NA"
						 },
						 "seqNo": "SEQ1206202603",
						 "payerVa": "Tejas08062026@upi",
						 "payeeVa": "tejas_vpa_jawaletejaszSFK@okicici",
						 "ifsc": "SBIN0004321",
						 "accountNumber": "123443211234",
						 "signature": "60xBJ/DXrIWWWCMquclC4Z1VfQbf1YWXVAjuKJRbzmc=",
						 "amount": "700.00",
						 "debitRule": "ON",
						 "startDate": "12062026",
						 "endDate": "12062028",
						 "frequency": "MONTHLY",
						 "adf1": "",
						 "adf2": "",
						 "adf3": "",
						 "adf4": "",
						 "adf5": ""
					}
					""";

			// Every Auth-ID has Separate Merchant Transaction Key
			// Merchant Transaction Key
			// String MerchantTransactionKey = "jx2Au8Rt8gR8qA4zG4zh1HT6Lp7rT2MH";
			// Path : Merchant Login => My Account => Transaction Key
			String MerchantTransactionKey = "jx2Au8Rt8gR8qA4zG4zh1HT6Lp7rT2MH"; // M0000118

			// For Encryption, We Need Request And Merchant Transaction Key
			String Encrypt = encrypt(Request, MerchantTransactionKey);

			System.out.println("Encryption Key :" + "\n" + Encrypt + "\n");

			String Descryption = decrypt(Encrypt, MerchantTransactionKey);

			System.out.println("Request validation :" + "\n" + Descryption + "\n");

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

}
