/*
package hsrmod.patches;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NewTextureAtlas extends TextureAtlas {
    static final String[] tuple = new String[4];

    public NewTextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip) {
        super(new NewTextureAtlasData(packFile, imagesDir, flip));
    }

    static int readTuple(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int colon = line.indexOf(58);
        if (colon == -1) {
            throw new GdxRuntimeException("Invalid line: " + line);
        } else {
            int i = 0;
            int lastMatch = colon + 1;

            for(i = 0; i < 3; ++i) {
                int comma = line.indexOf(44, lastMatch);
                if (comma == -1) {
                    break;
                }

                tuple[i] = line.substring(lastMatch, comma).trim();
                lastMatch = comma + 1;
            }

            tuple[i] = line.substring(lastMatch).trim();
            return i + 1;
        }
    }
    
    public static class NewTextureAtlasData extends TextureAtlas.TextureAtlasData {
        public NewTextureAtlasData(FileHandle packFile, FileHandle imagesDir, boolean flip) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);

            try {
                Page pageImage = null;

                while(true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }

                    if (line.trim().length() == 0) {
                        pageImage = null;
                    } else if (pageImage == null) {
                        FileHandle file = imagesDir.child(line);
                        float width = 0.0F;
                        float height = 0.0F;
                        if (TextureAtlas.readTuple(reader) == 2) {
                            width = (float)Integer.parseInt(TextureAtlas.tuple[0]);
                            height = (float)Integer.parseInt(TextureAtlas.tuple[1]);
                            TextureAtlas.readTuple(reader);
                        }

                        Pixmap.Format format = Pixmap.Format.valueOf(TextureAtlas.tuple[0]);
                        TextureAtlas.readTuple(reader);
                        Texture.TextureFilter min = Texture.TextureFilter.valueOf(TextureAtlas.tuple[0]);
                        Texture.TextureFilter max = Texture.TextureFilter.valueOf(TextureAtlas.tuple[1]);
                        String direction = TextureAtlas.readValue(reader);
                        Texture.TextureWrap repeatX = Texture.TextureWrap.ClampToEdge;
                        Texture.TextureWrap repeatY = Texture.TextureWrap.ClampToEdge;
                        if (direction.equals("x")) {
                            repeatX = Texture.TextureWrap.Repeat;
                        } else if (direction.equals("y")) {
                            repeatY = Texture.TextureWrap.Repeat;
                        } else if (direction.equals("xy")) {
                            repeatX = Texture.TextureWrap.Repeat;
                            repeatY = Texture.TextureWrap.Repeat;
                        }

                        pageImage = new Page(file, width, height, min.isMipMap(), format, min, max, repeatX, repeatY);
                        this.pages.add(pageImage);
                    } else {
                        boolean rotate = Boolean.valueOf(TextureAtlas.readValue(reader));
                        TextureAtlas.readTuple(reader);
                        int left = Integer.parseInt(TextureAtlas.tuple[0]);
                        int top = Integer.parseInt(TextureAtlas.tuple[1]);
                        TextureAtlas.readTuple(reader);
                        int width = Integer.parseInt(TextureAtlas.tuple[0]);
                        int height = Integer.parseInt(TextureAtlas.tuple[1]);
                        Region region = new Region();
                        region.page = pageImage;
                        region.left = left;
                        region.top = top;
                        region.width = width;
                        region.height = height;
                        region.name = line;
                        region.rotate = rotate;
                        if (TextureAtlas.readTuple(reader) == 4) {
                            region.splits = new int[]{Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3])};
                            if (TextureAtlas.readTuple(reader) == 4) {
                                region.pads = new int[]{Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3])};
                                TextureAtlas.readTuple(reader);
                            }
                        }

                        region.originalWidth = Integer.parseInt(TextureAtlas.tuple[0]);
                        region.originalHeight = Integer.parseInt(TextureAtlas.tuple[1]);
                        TextureAtlas.readTuple(reader);
                        region.offsetX = (float)Integer.parseInt(TextureAtlas.tuple[0]);
                        region.offsetY = (float)Integer.parseInt(TextureAtlas.tuple[1]);
                        region.index = Integer.parseInt(TextureAtlas.readValue(reader));
                        if (flip) {
                            region.flip = true;
                        }

                        this.regions.add(region);
                    }
                }
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
            } finally {
                StreamUtils.closeQuietly(reader);
            }

            this.regions.sort(TextureAtlas.indexComparator);
        }
    }
}
*/
