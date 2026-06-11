package com.auisy.TransactionAPI.Decryption;

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

			String EncryptResponseAfterPaymentDone = "20dwaMkjrz/aSxG3kSEMqWsJt57P2zke5oljMPaQKWQtxf0sG7/SMpFBEhhxNcf5E69cpk6347YkMpoLC2Nnv06lASXrsbS7ZzTJAUvmoUzzx6ME9CSzwyk8c0XRH39hCYI/VeCatKcBSCJxT2eyRULD5S4ZFk4Zml5OZAeO7sUhmH7Z3kiHFx8DcRo93QH9TGd8EW6nsOgxirRV/B+Lg2XNd4fYzxvRHSI1/NcuNRBX8IWEUlSB21UXUlddKyFD/DHzKQ9AU70vqf4dsp9lzDB76bY1FJ+N9UzTFj8rXKe1a/bZtvAsCPbtMFDUeyQPtyFPnkxGp+3TSwu/oIqyCCWfNdKm41YsqiNZsEduTjVf";

			// For Decryption, We Need Request And Tpap Transaction Key
			String Decrypt = decrypt(EncryptResponseAfterPaymentDone, TpapTransactionKey);
			System.out.println("After Decryption JSON API Response :" + "\n" + Decrypt + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
