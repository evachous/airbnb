package com.app.airbnb.repositories;

import com.app.airbnb.model.Image;
import com.app.airbnb.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public User findByUsername(String username) {
        User user = null;
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = ?1");
        query.setParameter(1, username);
        List<User> users = query.getResultList();
        if (users != null && users.size() > 0)
            user = users.get(0);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = ?1");
        query.setParameter(1, email);
        List<User> users = query.getResultList();
        if (users != null && users.size() > 0)
            user = users.get(0);
        return user;
    }

    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ioe) {
            System.out.println("Error compressing");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {
            System.out.println("Error decompressing");
        }
        return outputStream.toByteArray();
    }

    public String uploadImage(MultipartFile image) {
        try {
            String UPLOADED_FOLDER = "C://temp//";
            File folder = new File(UPLOADED_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] bytes = image.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER
                    + image.getOriginalFilename()
                    .substring(0, image.getOriginalFilename().lastIndexOf('.'))
                    + new Random().nextInt(1 << 20)
                    + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."))
            );
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
