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

    Image image;

    @Override
    public void initialize(MapView map) {
        try {
            URL url = new URL("https://i.imgur.com/CNGYK7Q.png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            drawn = true; // prevents rendering a null image
            throw new RuntimeException(e);
        }
    }

    boolean drawn = false;

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (drawn) return;
        canvas.drawImage(0, 0, MapPalette.resizeImage(image));
        drawn = true;
    }
}
