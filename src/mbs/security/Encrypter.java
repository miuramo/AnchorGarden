package mbs.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * �n�b�V���l�����@�\���
 * @auther Mahny
 */
public class Encrypter{
    
    /**
     * ���b�Z�[�W�_�C�W�F�X�g�FMD5
     */
    public static final String ALG_MD5= "MD5";
        
    /**
     * ���b�Z�[�W�_�C�W�F�X�g�FSHA-1
     */
    public static final String ALG_SHA1= "SHA-1";
        
    /**
     * ���b�Z�[�W�_�C�W�F�X�g�FSHA-256
     */
    public static final String ALG_SHA256= "SHA-256";
        
    /**
     * ���b�Z�[�W�_�C�W�F�X�g�FSHA-384
     */
    public static final String ALG_SHA384= "SHA-384";
        
    /**
     * ���b�Z�[�W�_�C�W�F�X�g�FSHA-512
     */
    public static final String ALG_SHA512= "SHA-512";
    
    /**
     * �n�b�V���l��Ԃ�
     * @param org �v�Z��������
     * @param algorithm �n�b�V���A���S���Y����(Encrypter.ALG_xxx�Ŏ擾�ł���)
     * @return �n�b�V���l
     */
    public static String getHash(String org, String algorithm){
        // �����E�A���S���Y���w�肪�����ꍇ�͌v�Z���Ȃ�
        if ((org== null)||(algorithm== null)){
            return null;
        }
        
        // ������
        MessageDigest md= null;
        try{
            md= MessageDigest.getInstance(algorithm);
        }
        catch(NoSuchAlgorithmException e){
            return null;
        }
        
        md.reset();
        md.update(org.getBytes());
        byte[] hash= md.digest();
        
        // �n�b�V����16�i��������ɕϊ�
        StringBuffer sb= new StringBuffer();
        int cnt= hash.length;
        for(int i= 0; i< cnt; i++){
            sb.append(Integer.toHexString( (hash[i]>> 4) & 0x0F ) );
            sb.append(Integer.toHexString( hash[i] & 0x0F ) );
        }
        return sb.toString();
    }
    public static void main(String[] args){
        
        String value= "aaa";
        String hash= Encrypter.getHash(value, Encrypter.ALG_SHA1);
        
        System.out.println("�o�͌��ʁF" + hash);
    }
}
