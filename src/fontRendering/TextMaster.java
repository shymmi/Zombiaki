package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

public class TextMaster {
	
	private static Loader LOADER;
	private static Map<FontType, List<GUIText>> TEXTS = new HashMap<>();
	private static FontRenderer FONT_RENDERER;
	
	public static void init(Loader theLoader){
		FONT_RENDERER = new FontRenderer();
		LOADER = theLoader;
	}
	
	public static void renderText(){
		FONT_RENDERER.render(TEXTS);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = LOADER.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = TEXTS.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			TEXTS.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text){
		List<GUIText> textBatch = TEXTS.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			TEXTS.remove(TEXTS.get(text.getFont()));
		}
	}
	
	public static void cleanUp(){
		FONT_RENDERER.cleanUp();
	}

}
