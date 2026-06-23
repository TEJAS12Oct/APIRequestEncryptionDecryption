package com.auisy.AccountManagement.SHA256Conversion;

import java.security.MessageDigest;
import java.util.Base64;

public class InitiateMandatePaySHAConversion {

	public static String sha256(String input) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(input.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(hash);
	}

	public static void main(String[] args) {

		try {
			String requestId = "REQ1506202602";
			String seqNo = "SEQ1506202602";
			String accountNo = "987667899876";
			String tpapId = "TPSM000091";

			// Checksum Formula : requestId + seqNo + accountNo + tpapId;
			String checksumString = requestId + "|" + seqNo + "|" + accountNo + "|" + tpapId;
			System.out.println("Checksum Printing : " + checksumString + "\n");

			String ChecksumConversion = sha256(checksumString);

			System.out.println("SHA-256 Checksum : " + ChecksumConversion);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
