package MapEditor;

import Level.Map;
import Maps.TestMap;
import Maps.TestMap2;
import Maps.TestMap3;
import Maps.TestMap4;
import Maps.TestMap5;
import Maps.TitleScreenMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("TestMap2");
            add("TestMap3");
            add("TestMap4");
            add("TestMap5");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "TitleScreen":
                return new TitleScreenMap();
            case "TestMap2":
            	return new TestMap2();
            case "TestMap3":
            	return new TestMap3();
            case "TestMap4":
            	return new TestMap4();
            case "TestMap5":
            	return new TestMap5();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
