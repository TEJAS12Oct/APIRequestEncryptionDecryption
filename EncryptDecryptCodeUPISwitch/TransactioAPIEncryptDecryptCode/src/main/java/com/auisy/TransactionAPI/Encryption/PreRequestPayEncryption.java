package com.auisy.TransactionAPI.Encryption;

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
public class PreRequestPayEncryption {
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
					  "seqnum": "AABBCCDDEEFF998877",
					  "merchantTxnId": "2026546994403062602",
					  "mpin": "NA",
					  "checkSum": "0d19637828353773da0b5f7efc6657719418082339ca99717cbaea4632716faf",
					  "payer": {
					    "mobile": "+917420857935",
					    "accountNumber": "987667899876",
					    "ifscCode": "HDFC0021196",
					    "accountType": "SAVINGS",
					    "payerAddr": "Tejas12@upi",
					    "payerName": "Tejas Jawale",
					    "payerMCC": "6789",
					    "payerType": "PERSON"
					  },
					  "payee": {
					    "payeeAddr": "Ram@okicici",
					    "payeeName": "Ram",
					    "payeeMCC": "9876",
					    "payeeType": "ENTITY"
					  },
					  "transaction": {
					    "amount": 302.00,
					    "currencyCode": "INR",
					    "txnType": "P2M",
					    "remark": "pay forshopping",
					    "approvedFlag": "A"
					  },
					   "qrDetails": {
					    "referenceId": "",
					    "globalVpa": "",
					    "qrAmount": "305.00",
					    "referenceUrl": "",
					    "merchantId": "TPSM000091",
					    "merchantTxnId": "",
					    "subMerchantId": "",
					    "terminalId": "",
					    "ver": "",
					    "qrMode": "",
					    "purpose": "",
					    "category": "",
					    "qrThrough": "",
					    "qrExpireDate": ""
					  },
					  "gstDetails": {
					    "gst": "svalue",
					    "billNo": "323223",
					    "billDate": "01-06-2026",
					    "billName": "mel"
					  },
					   "device": {
					    "mobile": "+917420844456",
					    "geocode": "19.084,72.8777",
					    "location": "Mumbai, IND",
					    "ip": "192.168.1.10",
					    "type": "MOBILE",
					    "id": "DEVICETEJAA1299",
					    "os": "Android",
					    "app": "PayApp 2.0",
					    "capability": "GPS",
					    "telecom": "Airtel"
					  },
					 	"additionalInfo": {
					    "auth_id": "TPSM000091",
					    "reseller_auth_id": "NA",
					    "customerID": "C00056",
					    "adf1": "NA",
					    "adf2": "NA",
					    "adf3": "NA",
					    "adf4": "NA",
					    "adf5": "NA"
					  },
					  "callbackUrl": "https://merchant.com/callback"
					}
					""";

			// Every Auth-ID has Separate Merchant Transaction Key
			// Merchant Transaction Key
			// String TpapTransactionKey = "jP4vB2nk2IX1Xq8Wf6bz7Gn7vO7Su8ln";
			// Take it from Backend or After Tpap On Boarding Mail will be sent and Details
			// available in Mail

			String TpapTransactionKey = "jP4vB2nk2IX1Xq8Wf6bz7Gn7vO7Su8ln"; // TPSM000091

			// For Encryption, We Need Request And TPAP Transaction Key
			String Encrypt = encrypt(Request, TpapTransactionKey);

			System.out.println("Encryption Key :" + "\n" + Encrypt + "\n");

			String Descryption = decrypt(Encrypt, TpapTransactionKey);

			System.out.println("Request validation :" + "\n" + Descryption + "\n");

		} catch (

		Exception e) {
			e.printStackTrace();
		}	
	}

}
