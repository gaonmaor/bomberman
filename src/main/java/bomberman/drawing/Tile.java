package bomberman.drawing;

import bomberman.entities.GameBoard;

import java.awt.*;
import java.io.Serializable;

public class Tile implements Serializable {
    public static final int TILE_LENGTH = 50;
    private Sprite sprite;
    private boolean hasSprite;
    // The actual location of the object center gravity on the board.
    private Point location;

    public void setX(int x) {
        location.x = x;
        setCol(x / TILE_LENGTH);
    }

    public int getX() {
        return location.x;
    }

    public  void setY(int y) {
        location.y = y;
        setRow(y / TILE_LENGTH);
    }

    public int getY() {
        return  location.y;
    }

    public boolean hasSprite() {
        return hasSprite;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        hasSprite = this.sprite != null;
    }

    public int getRow() {
        return locationToGrid(location.y);
    }

    public void setRow(int row) {
        this.location.y = gridToLocation(row);
    }

    public int getCol() {
        return locationToGrid(location.x);
    }

    public void setCol(int col) {
        this.location.x = gridToLocation(col);
    }

    private static int locationToGrid(int location) {
        return location / TILE_LENGTH;
    }

    private static int gridToLocation(int grid) {
        return (grid * TILE_LENGTH) + (TILE_LENGTH / 2);
    }

//    public int getTileX() {
//        return (this.row + TILE_LENGTH / 2) / TILE_LENGTH;
//    }
//
//    public int getTileY() {
//        return (this.col + TILE_LENGTH / 2) / TILE_LENGTH;
//    }

    public Tile(Sprite sprite, int col, int row) {
        setSprite(sprite);
        this.location = new Point(gridToLocation(col), gridToLocation(row));
    }

    void paint(Graphics g) {
        int x = location.x - (TILE_LENGTH / 2);
        int y = location.y - (TILE_LENGTH / 2);

        if (hasSprite) {
            g.drawImage(sprite.getImage(), x, y, x + TILE_LENGTH, y + TILE_LENGTH,
                    sprite.x, sprite.y, sprite.x + sprite.width,
                    sprite.y + sprite.height, GameBoard.getInstance());
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(x, y, x + TILE_LENGTH, y + TILE_LENGTH);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, x + TILE_LENGTH, y + TILE_LENGTH);
        }
    }

//    public boolean colide(Tile tile) {
//        boolean flag;
//        int tlx1 = tile.getRow() / TILE_LENGTH;
//        int tly1 = tile.getCol() / TILE_LENGTH;
//        int trx1 = (tile.getRow() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int try1 = tile.getCol() / TILE_LENGTH;
//        int brx1 = (tile.getRow() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int bry1 = (tile.getCol() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int blx1 = tile.getRow() / TILE_LENGTH;
//        int bly1 = (tile.getCol() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int tlx2 = getRow() / TILE_LENGTH;
//        int tly2 = getCol() / TILE_LENGTH;
//        int trx2 = (getRow() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int try2 = getCol() / TILE_LENGTH;
//        int brx2 = (getRow() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int bry2 = (getCol() + TILE_LENGTH - 1) / TILE_LENGTH;
//        int blx2 = getRow() / TILE_LENGTH;
//        int bly2 = (getCol() + TILE_LENGTH - 1) / TILE_LENGTH;
//        flag = ((tlx1 == tlx2) && (tly1 == tly2)) || ((trx1 == tlx2) && (try1 == tly2)) || ((brx1 == tlx2) && (bry1 == tly2)) || ((blx1 == tlx2) && (bly1 == tly2));
//        flag = (flag) || ((tlx1 == trx2) && (tly1 == try2)) || ((trx1 == trx2) && (try1 == try2)) || ((brx1 == trx2) && (bry1 == try2)) || ((blx1 == trx2) && (bly1 == try2));
//        flag = (flag) || ((tlx1 == brx2) && (tly1 == bry2)) || ((trx1 == brx2) && (try1 == bry2)) || ((brx1 == brx2) && (bry1 == bry2)) || ((blx1 == brx2) && (bly1 == bry2));
//        flag = (flag) || ((tlx1 == blx2) && (tly1 == bly2)) || ((trx1 == blx2) && (try1 == bly2)) || ((brx1 == blx2) && (bry1 == bly2)) || ((blx1 == blx2) && (bly1 == bly2));
//        return flag;
//    }
}