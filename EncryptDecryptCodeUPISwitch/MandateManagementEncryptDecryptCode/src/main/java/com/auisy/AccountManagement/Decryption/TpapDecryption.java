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
			// Every Auth-ID has Separate Merchant Transaction Key
			// Merchant Transaction Key
			// String TpapTransactionKey = "jP4vB2nk2IX1Xq8Wf6bz7Gn7vO7Su8ln";
			// Take it from Backend or After Tpap On Boarding Mail will be sent and Details
			// available in Mail

			String TpapTransactionKey = "jP4vB2nk2IX1Xq8Wf6bz7Gn7vO7Su8ln"; // TPSM000091

			String EncryptResponseAfterPaymentDone = "FrohBgb4780EPpCMmXn6kLAPDndUs4QbIKG8ZA9lyph1vGzi9pbAkMr6Z2whmnUe3gTug2i3Ph48K3mSiR0XOypSTUyeBF2GKOPklWDyRkbYOTMCURpDuNyE9rSP2awaoIUAm9a1xHAyxvt99zHVuyUlqnIs7GEQ1/Tu3YyDS87U3veReqIkULpZ2SuGEM1ZKouNgtdLbrsXNUZ2AKAotqL7OXvtS1RqvL8wiDK3/1ZaCqGfoLFuIcMBn6+sXIP/mgAB/0NJehEfkoDJlgMZfycuvDjpf24lSSiPjMZGejNIMIaFkm55T5VNcLiwylbBmA9u8odKyjqzJcb0l3thm4uupMywRoinsLCpsAsWc9XMsNBU6y23Qv2gcfx52qLrUUGHo0BhW8meI4RZdiAOnoXLBsEaWQ==";

			// For Decryption, We Need Request And Tpap Transaction Key
			String Decrypt = decrypt(EncryptResponseAfterPaymentDone, TpapTransactionKey);
			System.out.println("After Decryption JSON API Response :" + "\n" + Decrypt + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
