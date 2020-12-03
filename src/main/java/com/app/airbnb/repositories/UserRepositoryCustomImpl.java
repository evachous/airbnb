package com.app.airbnb.repositories;

import com.app.airbnb.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.net.URL;

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

    public List<User> findAllHosts() {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.isHost = true");
        return query.getResultList();
    }

    public List<User> findAllGuests() {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.isGuest = true");
        return query.getResultList();
    }

    public String uploadImage(MultipartFile image, String type) {
        try {
            String UPLOADED_FOLDER = "C://temp//" + type + "//";
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

    public String uploadImageByURL(String imageURL, String filename, String type) {
        try {
            String UPLOADED_FOLDER = "C://temp//" + type + "//";
            File folder = new File(UPLOADED_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }

            URL url = new URL(imageURL);
            BufferedImage image = ImageIO.read(url);
            Path path = Paths.get(UPLOADED_FOLDER
                    + filename
                    + new Random().nextInt(1 << 20)
                    + ".jpg"
            );
            File file = new File(path.toString());
            ImageIO.write(image, "jpg", file);
            byte[] bytes = Files.readAllBytes(path);

            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
