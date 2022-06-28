package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;

    public List<Credential> getAllCredentials(int userid) throws Exception {
        List<Credential> credentials = credentialMapper.findByUserId(userid);
        if (credentials == null) {
            throw new Exception();
        }
        return credentials;
    }

    public void addCredential(Credential credential, int userid) {
        String key = randomKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        credentialMapper.insert(credential, userid);
    }

    private String randomKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;
    }

    public void updateCredential(Credential credential) {
        String key = randomKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        credentialMapper.update(credential);
    }


    public Credential decryptedCredential(Credential credential) {
        if(credential != null && credential.getKey() == null) return credential;
        Credential cl = new Credential(credential.getCredentialid(), credential.getUrl(),credential.getUsername(), credential.getKey(), credential.getPassword());
        cl.setPassword(encryptionService.decryptValue(cl.getPassword(), cl.getKey()));
        return cl;
    }

    public void deleteCredential(int credentialid) {
        credentialMapper.delete(credentialid);
    }

}
