package com.stormpath.sdk.android;

import com.stormpath.sdk.PreferenceStore;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class SystemKeyStore implements PreferenceStore {

    public static String KEY_ACCESS_TOKEN = "";

    public static String KEY_REFRESH_TOKEN = "";

    static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";
    static final String CIPHER_PROVIDER = "AndroidKeyStoreBCWorkaround";

    public static final String TAG = "SystemKeyStore";

    private KeyStore keyStore;
    List<String> keyAliases;

    public SystemKeyStore(Context context) {
        try {
            KEY_ACCESS_TOKEN = context.getPackageName() + ".stormpath-access-token"; //must be unique keys at the system level
            KEY_REFRESH_TOKEN = context.getPackageName() + ".stormpath-refresh-token";
            keyStore = keyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        }
        catch(Exception e) {}
    }

    private void refreshKeys() {
        keyAliases = new ArrayList<>();
        try {
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement());
            }
        }
        catch(Exception e) {}
    }

    private void encryptKey(String key, String value) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(key, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(value.getBytes("UTF-8"));
            cipherOutputStream.close();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @TargetApi(8) //this whole class will only be run on api level greater than like 16
    private String decryptKey(String key, String encryptedText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(key, null);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            byte[] value = output.doFinal(privateKeyEntry.getPrivateKey().getEncoded());

            return new String(value, 0, value.length, "UTF-8");

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    private void removeKey(String key) {
        try {
            keyStore.deleteEntry(key);
            refreshKeys();
        } catch (KeyStoreException e) {

            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void setAccessToken(String accessToken) {
        encryptKey(KEY_ACCESS_TOKEN, accessToken);
    }

    @Override
    public String getAccessToken() {
        return decryptKey(KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void clearAccessToken() {
        removeKey(KEY_ACCESS_TOKEN);
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        encryptKey(KEY_REFRESH_TOKEN, refreshToken);
    }

    @Override
    public String getRefreshToken() {
        return decryptKey(KEY_REFRESH_TOKEN, null);
    }

    @Override
    public void clearRefreshToken() {
        removeKey(KEY_REFRESH_TOKEN);
    }
}
