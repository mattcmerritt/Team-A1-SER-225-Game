package Maps;


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
import Tilesets.CommonTileset;
import Utils.Direction;
import Utils.Point;

// Represents a test map to be used in a level
public class TestMap6 extends Map{

	public TestMap6(String mapFileName) {
        super(mapFileName, new CommonTileset(), new Point(1, 11));
    }
    public TestMap6() {
    	super("test_map_6.txt", new CommonTileset(), new Point(1, 11));
    }

}
