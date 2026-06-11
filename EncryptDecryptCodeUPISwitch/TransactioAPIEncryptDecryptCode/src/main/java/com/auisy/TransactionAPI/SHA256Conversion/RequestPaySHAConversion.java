package com.auisy.TransactionAPI.SHA256Conversion;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class RequestPaySHAConversion {
	public static String sha256(String input) throws Exception {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

		StringBuilder hexString = new StringBuilder();

		for (byte b : hashBytes) {
			hexString.append(String.format("%02x", b));
		}

		return hexString.toString();
	}

	public static void main(String[] args) {

		try {
			String merchantTxnId = "1006202601";
			String auth_id = "TPSM000091";
			double amount = 301.00; // Always keep amount in 2 decimal format
			String payee_addr = "tejas_vpa_jawaletejaszSFK@okicici"; // who Receives Money
			String payer_addr = "Tejas08062026@upi"; //Customer who pays Money

			// Checksum Formula : auth_id + merchantTxnId + amount + pay_key
			String checksumString = merchantTxnId + auth_id + String.format("%.2f", amount) + payer_addr + payee_addr;
			System.out.println("Checksum Printing : " + checksumString + "\n");

			String ChecksumConversion = sha256(checksumString);

			System.out.println("SHA-256 Checksum : " + ChecksumConversion);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}