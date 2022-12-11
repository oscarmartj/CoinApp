package es.upm.etsiinf.dam.coinapp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Security {
    private static final String HASH_ALGORITHM = "SHA-256";

    public String encryptPassword (String password) throws NoSuchAlgorithmException {
        return getHash(password);
    }

    public boolean checkPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        return storedHash.equals(getHash(password));
    }

    private String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, hash).toString(16); //Convierte el hash a formato hexadecimal usando la clase BigInteger
    }
}

