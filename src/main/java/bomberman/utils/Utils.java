package bomberman.utils;

import bomberman.drawing.MovingSprite;
import bomberman.drawing.Sprite;
import com.sun.javafx.fxml.PropertyNotFoundException;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

public class Utils {
    private static Utils instance = null;
    private Map<String, BufferedImage> images;
    private Color tranColor;
    private Properties properties;
    // Contains the lists of Resource items mapped by their directory name.
    private Map<String, List<String>> listMap;

    private Utils() {
        listMap = new HashMap<>();
        images = new HashMap<>();
        properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("bomberman.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file is missing! (bomberman.properties)");
            }
            properties.load(inputStream);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        String[] transRelics = getProperty(Utils.class, "transColor").split("\\W+");
        this.tranColor = new Color(Integer.parseInt(transRelics[0]), Integer.parseInt(transRelics[1]), Integer.parseInt(transRelics[2]));
    }

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public Image getImage(String imageName) {
        return this.images.get(imageName);
    }

    public synchronized void initAllImages() {
        loadResourceDir(getProperty(Utils.class, "miscDir"), this::loadImage);
        loadResourceDir(getProperty(Utils.class, "charactersDir"), this::loadImage);
        loadResourceDir(getProperty(Utils.class, "worldsDir"), this::loadImage);
        loadResourceDir(getProperty(Utils.class, "mapsDir"), this::loadMap);
    }


    private void loadResourceDir(String dirPath, BiConsumer<String, String> resourceHandler) {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File dir = new File(classLoader.getResource(dirPath).getFile());
        File[] files = dir.listFiles();
        List<String> resourceNameList = new ArrayList<>(files.length);
        for (File nextFile : files) {
            String resourceName = FilenameUtils.removeExtension(nextFile.getName());
            resourceHandler.accept(dirPath + nextFile.getName(), resourceName);
            resourceNameList.add(resourceName);
        }
        listMap.put(dirPath, resourceNameList);
    }

    public List<String> getListMap(String dirPath) {
        return listMap.get(dirPath);
    }

    public void loadMap(String path, String name) {

    }

    public void loadImage(String path, String name) {
        BufferedImage image;
        try {
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
            if (image == null) {
                throw new MissingResourceException("The image: " + path + " is missing.", getClass().getName(), path);
            }
            image = transImage(image);
            this.images.put(name, image);
        } catch (IOException | MissingResourceException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage transImage(BufferedImage image) {
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        BufferedImage transImage = new BufferedImage(width, height, 2);
        Graphics nG = transImage.getGraphics();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) != this.tranColor.getRGB()) {
                    nG.setColor(new Color(image.getRGB(x, y)));
                    nG.drawLine(x, y, x, y);
                }
            }
        }
        return transImage;
    }

//    public BufferedImage fixImage(BufferedImage image, Color c) {
//        int height = image.getHeight(null);
//        int width = image.getWidth(null);
//        BufferedImage nBI = new BufferedImage(width, height, 2);
//        Graphics nG = nBI.getGraphics();
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (image.getRGB(x, y) != c.getRGB()) {
//                    nG.setColor(new Color(image.getRGB(x, y)));
//                    nG.drawLine(x, y, x, y);
//                } else {
//                    nG.setColor(this.tranColor);
//                    nG.drawLine(x, y, x, y);
//                }
//            }
//        }
//        return nBI;
//    }

//    public void fixImage(String path) {
//        try {
//            BufferedImage image = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
//            Color c = new Color(0, 136, 255);
//            image = fixImage(image, c);
//            try {
//                ImageIO.write(image, "png",
//                        getInstance().getClass().getResource(path).openConnection().getOutputStream());
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

//    public String getPath() {
//        return getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//    }

    public Object loadObject(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(ClassLoader.getSystemResourceAsStream(fileName));
        return ois.readObject();
    }

    public void saveObject(Object obj, String fileName) throws IOException {
        URL url = getInstance().getClass().getResource(fileName);
        File f = new File(url.getPath());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(obj);
    }

    public char[][] loadMap(String fileName) throws IOException {
        URL u = getInstance().getClass().getResource(fileName);
        //File file = new File(u.getPath());
        InputStreamReader isr = new InputStreamReader(u.openStream());
        BufferedReader br = new BufferedReader(isr);
        char[][] wordSurface = new char[11][13];
        int tmpVal;
        for (int i = 0; (tmpVal = br.read()) != -1; i++) {
            if (((char) tmpVal >= 'A') && ((char) tmpVal <= 'Z')) {
                wordSurface[(i / 13)][(i % 13)] = ((char) tmpVal);
            } else {
                i--;
            }
        }
        br.close();
        return wordSurface;
    }

    public boolean hasImage(String imageName) {
        return this.images.containsKey(imageName);
    }

    public String getProperty(Class<?> clazz,String name) {
        String property = properties.getProperty(clazz.getName() + "." + name);
        if (property == null) {
            throw new PropertyNotFoundException("The property:" + name + " of class:" +
                    clazz.getSimpleName() + " was not found in the properties file.");
        }
        return property;
    }

    public Image getMainSpriteImage(String imageName) {
        BufferedImage image = (BufferedImage) Utils.getInstance().getImage(imageName);
        int width = image.getWidth(null) / Sprite.ANIMATED_PHASES;
        int height = image.getHeight(null) / MovingSprite.Directions.count();
        return image.getSubimage(width, height * MovingSprite.Directions.Down.getValue(), width, height);
    }
}