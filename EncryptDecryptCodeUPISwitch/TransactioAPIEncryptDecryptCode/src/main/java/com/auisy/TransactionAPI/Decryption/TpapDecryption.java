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

			String EncryptResponseAfterPaymentDone = "/hH9zp6bIqdvfzy3l3a++MCMl1425WsXDTrsUFfcdAwmUHUdc5EJIv9l65K/2tW7bXjjwumC+4u6A172+NjWYzy6hbICRCVtJDgyVXYniBeZyAYnkCYicUjIbTlVFq5hurfKLN7VvVzvQzX6tjPO8kVAXat/daa0bRFjfmRrW6ag33IeQ88TUP32MkfDCXW3kA49B/8uz7Pa48coq7PCRXxqySVIzhnPoJ36aSjro+gDdTZTamPsByk9OX3Tg8cqW7gCS24G0ZNg0BZs7hRanjDso8BGFMtsFH7gtHrOM52cg9p/NVj5wfOgfRSNBpZPV9mORqedl4LKN13GV3tVlKVTs6lgqlE1ZcLZ7XiO6Hc0zAYJig==";

			// For Decryption, We Need Request And Tpap Transaction Key
			String Decrypt = decrypt(EncryptResponseAfterPaymentDone, TpapTransactionKey);
			System.out.println("After Decryption JSON API Response :" + "\n" + Decrypt + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
