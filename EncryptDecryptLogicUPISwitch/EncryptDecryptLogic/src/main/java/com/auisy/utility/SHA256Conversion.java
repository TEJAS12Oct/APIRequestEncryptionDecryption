package com.auisy.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256Conversion {

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
        	//Checksum Formula : Logic: auth_id + merchantTxnId + amount + pay_key
            String Checksum = "M0000932026546994443400888600.00gatla_dplnQZ@paytm";

            String ChecksumConversion = sha256(Checksum);

            System.out.println("SHA-256 Checksum:");
            System.out.println(ChecksumConversion);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}