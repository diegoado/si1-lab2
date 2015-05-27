package models.constant;

public enum StyleType {
	
	AXE("Áxe"), BLACK_MUSIC("Black Music"), BLUES("Blues"), BOSSA_NOVA("Bossa Nova"),
	CHILLOUT("Chillout"), CLASSICO("Clássico"), COUNTRY("Country"), DANCE("Dance"),
	DISCO("Disco"), ELECTRONICA("Electronica"), EMOCORE("Emocore"), FADO("Fado"),
	FOLK("Folk"), FORRO("Forró"), FUNK("Funk"), GOLPEL("Gospel"), GOTICO("Gótico"),
	GRUNGE("Grunge"), HARD_ROCK("Hard Rock"), HARDCORE("Hardcore"), HEAVY_METAL("Heavy Metal"),
	HIP_HOP("Hip Hop"), HOUSE("House"), INDIE("Indie"), INDUSTRIAL("Industrial"), INFANTIL("Infatil"),
	INSTRUMENTAL("Instrumental"), J_POP("J-Pop"), J_ROCK("J-Rock"), JAZZ("Jazz"), 
	JOVEM_GUARDA("Jovem Guarda"), K_POP("K-Pop"), K_ROCK("K-Rock"), MPB("MPB"), NEW_AGE("New Age"),
	NEW_WAVE("New Wave"), PAGODE("Pagode"), PIANO_ROCK("Piano Rock"), POP("Pop"), PUNK("Punk"),
	POP_ROCK("Pop Rock"), POS_PUNK("Pós Punk"), POST_ROCK("Post Rock"), POWER_POP("Power Pop"),
	PROGRESSIVO("Progressivo"), PSICODELIA("Psicodelia"), PUNK_ROCK("Punk Rock"), R_AND_B("R&B"),
	RAP("Rap"), REGGAE("Reggae"), REGIONAL("Regional"), ROCK("Rock"), 
	ROCK_ALTERNATIVO("Rock Alternativo"), ROMATIVO("Romântico"), SAMBA("SAMBA"), SERTANEJO("Sertanejo"),
	SKA("Ska"), SOFT_ROCK("Soft Rock"), SOUL("Soul"), SURF_MUSIC("Surf Music"), TECNOPOP("Tecnopop"),
	TRACE("Trace"), TRIP_HOP("Trip Hop"), VELHA_GUARDA("Velha Guarda"), WORLD_MUSIC("World Music");
	
	private String description;
	
	private StyleType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
