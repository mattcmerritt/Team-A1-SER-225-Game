package Maps;

import Tilesets.CommonTileset;
import Utils.Point;
import java.util.ArrayList;

import Enemies.BugEnemy;
import Enemies.DinosaurEnemy;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
import Level.Enemy;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.TileType;
import NPCs.Walrus;
import Utils.Direction;

public class TestMap7 extends Map {
	public TestMap7(String mapFileName) {
        super(mapFileName, new CommonTileset(), new Point(1, 11));
    }
    public TestMap7() {
    	super("test_map_7.txt", new CommonTileset(), new Point(1, 11));
    }

}
