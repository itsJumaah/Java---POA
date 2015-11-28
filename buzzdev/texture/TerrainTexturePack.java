package buzzdev.texture;

public class TerrainTexturePack {
	
	private TerrainTexture finalTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	public TerrainTexturePack(TerrainTexture finalTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		super();
		this.finalTexture = finalTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	
	public TerrainTexture getFinalTexture() {
		return finalTexture;
	}
	public TerrainTexture getrTexture() {
		return rTexture;
	}
	public TerrainTexture getgTexture() {
		return gTexture;
	}
	public TerrainTexture getbTexture() {
		return bTexture;
	}

}
