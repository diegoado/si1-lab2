package models.constant;

public enum InstrumentType {
	
	ACCORDION("Sanfona"), ACOUSTIC_BASS("Baixo Acústico"), BABY_GRAND("Piano de Meia-Cauda"),
	BAGPIDES("Gaita de Fole"), BANJO("Banjo"), BASS_DRUM("Bumbo"), BASS_GUITAR("Baixo Elétrico"), 
	BONGO_DRUMS("Bonjô"), CARILLON("Carrilhão"), CASTANETS("Castanholas"), CAVAQUINHO("Cavaquinho"), 
	VIOLONCELLO("Violoncelo"), CLARINET("Clarinete"), CONGA("Conga"), CLAVICHORD("Clavicórdio"), 
	DOUBLE_BASSOON("Contrafagote"), CYMBAL("Prato"), DOUBLE_BASS("Contra Baixo"), DRUMS("Bateria"), 
	FIFE("Pífano"), FLUTE("Flauta"), GRAND_PIANO("Piano de Cauda"), GUIRO("Reco-reco"), GUITAR1("Violão"),
	GUITAR2("Guitarra"), HARMONICA("Gaita"), HARP("Harpa"), HI_HAT("Chimbau"), HORN("Trompa"),
	TIMPANI("Tímpano"), KEYBOARD("Teclado"), LAP_STEEL_GUITAR("Guitarra Havaiana"), MANDOLIN("Mandolin"),
	ORGAN("Orgão"), PIANO("Piano"), PORTUGUESE_GUITAR("Guitarra Portuguesa"), RECORDER("Flauta Doce"),
	SAXOPHONE("Saxofone"), SEVEN_STRING_GUITAR("Violão de 7 cordas"), SHAKER("Chocalho"), SITAR("Cítara"),
	SOUSAPHONE("Sousafone"), TABLA("Tabla"), TALKING_DRUM("Tambor Falante"), TAMBOURINE("Pandeiro"),
	TENOR_GUITAR("Violão Tenor"), TOM_TOM("Tambor"), TRIANGLE("Triângulo"), TROMBONE("Trombone"),
	TRUMPET("Trompete"), TUBA("Tuba"), VIBRAPHONE("Vibrafone"), VIOLA("Viola"), VIOLIN("Violino"),
	VOCAL("Vocal"), XYLOPHONE("Xilofone"), ZABUMBA("Zabumba");
	
	private String description;
	
	private InstrumentType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

}
