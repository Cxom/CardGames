package net.punchtree.cardgames.prototyping;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CustomMapRenderer extends MapRenderer {

    private final Image image;

    public CustomMapRenderer(Image image) {
        this.image = image;
    }

    public static CustomMapRenderer fromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            BufferedImage image = ImageIO.read(url);
            return new CustomMapRenderer(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(MapView map) {

    }

    boolean drawn = false;

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (drawn) return;
        canvas.drawImage(0, 0, MapPalette.resizeImage(image));
        drawn = true;
    }
}
