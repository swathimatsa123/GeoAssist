package com.geoassist;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

public class MBTileAdapter  implements TileProvider {
    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE = 16 * 1024;

    private AssetManager mAssets;
	private MBTileReader mapRdr  = null;
    public MBTileAdapter(String  fileName ) {
        mapRdr = new MBTileReader(fileName);
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
    	Log.e("GetTile" , "Called " + String.valueOf(mapRdr));
    	if ( mapRdr != null) {
    		return  mapRdr.getTile(x, y, zoom);
    	}
    	else{
    		return NO_TILE;
    	}
    }
//
//        byte[] image = readTileImage(x, y, zoom);
//        return image == null ? null : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
//    }
//
//    private byte[] readTileImage(int x, int y, int zoom) {
//        InputStream in = null;
//        ByteArrayOutputStream buffer = null;
//
//        try {
//            in = mAssets.open(getTileFilename(x, y, zoom));
//            buffer = new ByteArrayOutputStream();
//
//            int nRead;
//            byte[] data = new byte[BUFFER_SIZE];
//
//            while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
//                buffer.write(data, 0, nRead);
//            }
//            buffer.flush();
//
//            return buffer.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } catch (OutOfMemoryError e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (in != null) try { in.close(); } catch (Exception ignored) {}
//            if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
//        }
//    }
//
//    private String getTileFilename(int x, int y, int zoom) {
//        return "map/" + zoom + '/' + x + '/' + y + ".png";
//    }
}
