package com.auisy.AccountManagement.SHA256Conversion;

import java.security.MessageDigest;
import java.util.Base64;

public class ApproveRejectMandateSHAConversion {

	public static String sha256(String input) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(input.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(hash);
	}

	public static void main(String[] args) {

		try {
			String tpapId = "TPSM000091";
			String seqNo = "SEQ1206202607";
			String accountNo = "123443211234";
			String requestId = "REQ1206202607";

			// Checksum Formula : tpapId + seqNo + ifsc + requestId;
			String checksumString = tpapId + "|" + seqNo + "|" + accountNo + "|" + requestId;
			System.out.println("Checksum Printing : " + checksumString + "\n");

			String ChecksumConversion = sha256(checksumString);

			System.out.println("SHA-256 Checksum : " + ChecksumConversion);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

