package spy.project.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.*;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.Base64;

/**
 *
 * 加解密工具类
 * 编码：hex， base64
 * 对称加密： AES, SM4
 * 非对称加密：RSA, SM2
 * 散列： MD5，SHA，HMAC, SM3
 *
 */
public class EncryptUtils {

    //hex, base64
    public static class Encoder {
        public static String base64Encode(String text, boolean isUrl) {
            if(isUrl) {
                return Base64.getUrlEncoder().encodeToString(text.getBytes());
            } else {
                return Base64.getEncoder().encodeToString(text.getBytes());
            }
        }

        public static String base64Decode(String text, boolean isUrl) {
            if(isUrl) {
                return new String(Base64.getUrlDecoder().decode(text));
            } else {
                return new String(Base64.getDecoder().decode(text));
            }
        }

        public static String hexEncode(String text) {
            return HexUtil.encodeHexStr(text.getBytes());
        }

        public static String hexDecode(String text) {
            return HexUtil.decodeHexStr(text);
        }

    }


    //MD5,SHA,HMAC,SM3
    public static class Hash {

        public static String md5(String text) {
            return MD5.create().digestHex16(text);
        }

        public static String sha256(String text) {
            return DigestUtil.sha256Hex(text);
        }

        public static String sha512(String text) {
            return DigestUtil.sha512Hex(text);
        }

        public static String hmacMd5(String text, String key) {
            return DigestUtil.hmac(HmacAlgorithm.HmacMD5, key.getBytes()).digestHex(text);
        }

        public static String hmacSha256(String text, String key) {
            return DigestUtil.hmac(HmacAlgorithm.HmacSHA256, key.getBytes()).digestHex(text);
        }

        public static String hmacSha512(String text, String key) {
            return DigestUtil.hmac(HmacAlgorithm.HmacSHA512, key.getBytes()).digestHex(text);
        }

        public static String sm3(String text) {
            return DigestUtil.digester("sm3").digestHex(text);
        }

    }

    //AES:
    //电子密码本模式 AES/ECB/PKCS5Padding
    //分组密码本模式 AES/CBC/PKCS7Padding
    public static class AES {
        public static String encodeECB(String text, String key) {
            SymmetricCrypto aes = new SymmetricCrypto("AES/ECB/PKCS5Padding", key.getBytes());
            return aes.encryptHex(text);
        }

        public static String decodeECB(String text, String key) {
            SymmetricCrypto aes = new SymmetricCrypto("AES/ECB/PKCS5Padding", key.getBytes());
            return aes.decryptStr(text);
        }

        public static String encodeCBC(String text, String key) {
            SymmetricCrypto aes = new SymmetricCrypto("AES/CBC/PKCS7Padding", key.getBytes());
            return aes.encryptHex(text);
        }

        public static String decodeCBC(String text, String key) {
            SymmetricCrypto aes = new SymmetricCrypto("AES/CBC/PKCS7Padding", key.getBytes());
            return aes.decryptStr(text);
        }
    }


    //SM4:
    // SM4/ECB/PKCS5Padding
    // SM4/CBC/PKCS7Padding
    public static class SM4 {
        public static String encodeECB(String text, String key) {
            SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());
            return sm4.encryptHex(text);
        }

        public static String decodeECB(String text, String key) {
            SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());
            return sm4.decryptStr(text, CharsetUtil.CHARSET_UTF_8);
        }

        public static String encodeCBC(String text, String key) {
            SymmetricCrypto sm4 = new SymmetricCrypto("SM4/CBC/PKCS7Padding", key.getBytes());
            return sm4.encryptHex(text);
        }

        public static String decodeCBC(String text, String key) {
            SymmetricCrypto sm4 = new SymmetricCrypto("SM4/CBC/PKCS7Padding", key.getBytes());
            return sm4.decryptStr(text, CharsetUtil.CHARSET_UTF_8);
        }
    }


    //RSA: RSA/ECB/PKCS1Padding
    public static class Rsa {
        public static KeyPair genRSAKey() {
            RSA rsa = new RSA(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue());
            KeyPair keypair = new KeyPair();
            keypair.setPrvkey(rsa.getPrivateKeyBase64());
            keypair.setPubkey(rsa.getPublicKeyBase64());
            return keypair;
        }

        //pubkey encode
        public static String encode(String text, String pubkey) {
            RSA rsa = new RSA(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(),null, pubkey);
            return rsa.encryptHex(text, KeyType.PublicKey);
        }

        //prvkey decode
        public static String decode(String text, String prvkey) {
            RSA rsa = new RSA(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(), prvkey, null);
            return rsa.decryptStr(text, KeyType.PrivateKey);
        }

    }

    //SM2: sm2p256v1
    public static class SM2 {
        public static KeyPair genSM2Key() {
            java.security.KeyPair pair = SecureUtil.generateKeyPair("SM2");
            byte[] privateKey = pair.getPrivate().getEncoded();
            byte[] publicKey = pair.getPublic().getEncoded();
            KeyPair keypair = new KeyPair();
            keypair.setPrvkey(Base64.getEncoder().encodeToString(privateKey));
            keypair.setPubkey(Base64.getEncoder().encodeToString(publicKey));
            return keypair;
        }

        public static String encode(String text, String pubKey) {
            cn.hutool.crypto.asymmetric.SM2 sm2 = SmUtil.sm2(null, pubKey);
            return sm2.encryptHex(text, KeyType.PublicKey);
        }

        public static String decode(String text, String prvKey) {
            cn.hutool.crypto.asymmetric.SM2 sm2 = SmUtil.sm2(prvKey, null);
            return sm2.decryptStr(text, KeyType.PrivateKey);
        }
    }

    public static class KeyPair {
        private String pubkey;
        private String prvkey;

        public String getPubkey() {
            return pubkey;
        }

        public void setPubkey(String pubkey) {
            this.pubkey = pubkey;
        }

        public String getPrvkey() {
            return prvkey;
        }

        public void setPrvkey(String prvkey) {
            this.prvkey = prvkey;
        }
    }

}
