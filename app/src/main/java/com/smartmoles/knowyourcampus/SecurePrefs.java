package com.smartmoles.knowyourcampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.security.MessageDigest;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class SecurePrefs {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
    private static final String CHARSET = "UTF-8";

    private Cipher writer;
    private Cipher reader;
    private Cipher keyWriter;
    private SharedPreferences preferences;

    SecurePrefs(Context act) {
        try {
            String secureKey = getUniqueId();
            writer = Cipher.getInstance(TRANSFORMATION);
            reader = Cipher.getInstance(TRANSFORMATION);
            keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);

            byte[] iv = new byte[writer.getBlockSize()];
            System.arraycopy("fldsjfodasjifudslfjdsaofshaufihadsf".getBytes(), 0, iv, 0, writer.getBlockSize());
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            MessageDigest md = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
            md.reset();
            SecretKeySpec secretKey = new SecretKeySpec(md.digest(secureKey.getBytes(CHARSET)), TRANSFORMATION);

            writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            keyWriter.init(Cipher.ENCRYPT_MODE, secretKey);

            preferences = PreferenceManager.getDefaultSharedPreferences(act);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUniqueId() {
        UUID uniqueKey = UUID.nameUUIDFromBytes(getDeviceName().getBytes());
        return uniqueKey.toString();
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    boolean containsKey(String key) {
        return preferences.contains(toKey(key));
    }

    void remove(String key){
        if (preferences.contains(toKey(key))) {
            preferences.edit().remove(toKey(key)).apply();
        }
    }

    String getString(String key) {
        if (preferences.contains(toKey(key))) {
            String securedEncodedValue = preferences.getString(toKey(key), "");
            return decrypt(securedEncodedValue);
        }
        return null;
    }

    void putString(String key, String value) {
        preferences.edit().putString(toKey(key), encrypt(value, writer)).apply();
    }

    private String toKey(String key) {
        return encrypt(key, keyWriter);
    }

    private String encrypt(String value, Cipher writer) {
        try {
            return Base64.encodeToString(writer.doFinal(value.getBytes(CHARSET)), Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }

    private String decrypt(String securedEncodedValue) {
        try {
            return new String(reader.doFinal(Base64.decode(securedEncodedValue, Base64.NO_WRAP)), CHARSET);
        } catch (Exception e) {
            return "";
        }
    }
}
