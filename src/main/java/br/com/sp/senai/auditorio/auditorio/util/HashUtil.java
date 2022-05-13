package br.com.sp.senai.auditorio.auditorio.util;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class HashUtil {
	public static String hash(String aleat) {
		String sauce = "C0d1n1nG";
		aleat = sauce + aleat;
		
		String hash = Hashing.sha256().hashString(aleat, StandardCharsets.UTF_8).toString();
		return hash;
	}
}
