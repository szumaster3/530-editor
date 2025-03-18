package com.alex.tools;

import com.alex.filestore.Index;
import com.alex.filestore.Store;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpritesDumper {
    public static void main2(String[] args) throws IOException {
        BufferedImage background = ImageIO.read(new File("bg/matrix.jpg"));
        int id = 3769;
        int sx = background.getWidth() / 2;
        int sy = background.getHeight() / 2;

        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                System.out.println("id " + id);
                BufferedImage part = background.getSubimage(x * sx, y * sy, sx, sy);
                ImageIO.write(part, "gif", new File("bg/" + id++ + ".gif"));
            }
        }

    }

    public static void main3(String[] args) throws IOException {
        Store cache = new Store("cache667_2/", false);
        UpdateCache.packLogo(cache);
        System.out.println("Adding donator icon...");
        UpdateCache.packDonatorIcon(cache);
        System.out.println("Adding Matrix icon...");
        UpdateCache.packMatrixIcon(cache);
    }

    public static byte[] getImage(File file) throws IOException {
        ImageOutputStream stream = ImageIO.createImageOutputStream(file);
        byte[] data = new byte[(int) stream.length()];
        stream.read(data);
        return data;
    }

    public static void main(String[] args) throws IOException {
        Store cache = new Store("");
        Index sprites = cache.getIndexes()[32];
        int[] arr$ = sprites.getTable().getValidArchiveIds();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            int archiveId = arr$[i$];
            int[] arr$1 = sprites.getTable().getArchives()[archiveId].validFileIds;
            int len$1 = arr$1.length;

            for (int i$1 = 0; i$1 < len$1; ++i$1) {
                int fileId = arr$1[i$1];
                byte[] data = sprites.getFile(archiveId, fileId);
                Image image = Toolkit.getDefaultToolkit().createImage(data);
                String name = "sprites32/" + archiveId + "_" + fileId;
                BufferedImage bi = toBufferedImage(image);
                if (bi == null) {
                    System.out.println("failed " + name);
                } else {
                    ImageIO.write(bi, "png", new File(name + ".png"));
                }
            }
        }

    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        } else {
            image = (new ImageIcon(image)).getImage();
            boolean hasAlpha = true;
            BufferedImage bimage = null;
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            byte g;
            try {
                g = 1;
                if (hasAlpha) {
                    g = 2;
                }

                GraphicsDevice g1 = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = g1.getDefaultConfiguration();
                if (image.getWidth(null) < 0 || image.getHeight(null) < 0) {
                    return null;
                }

                bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), g);
            } catch (HeadlessException var7) {
            }

            if (bimage == null) {
                g = 1;
                if (hasAlpha) {
                    g = 2;
                }

                bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), g);
            }

            Graphics2D g11 = bimage.createGraphics();
            g11.drawImage(image, 0, 0, null);
            g11.dispose();
            return bimage;
        }
    }
}
