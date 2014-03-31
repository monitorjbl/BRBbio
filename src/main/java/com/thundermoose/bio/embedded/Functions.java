package com.thundermoose.bio.embedded;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Thundermoose on 3/30/2014.
 */
public class Functions {
  public static String MD5(String input) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hash = md.digest(input.getBytes());
    StringBuilder sb = new StringBuilder(2 * hash.length);
    for (byte b : hash) {
      sb.append(String.format("%02x", b & 0xff));
    }

    return sb.toString();
  }

}
