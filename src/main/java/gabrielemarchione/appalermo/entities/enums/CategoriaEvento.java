package gabrielemarchione.appalermo.entities.enums;


public enum CategoriaEvento {
    Concerto("concert"),
    Teatro("theater"),
    Conferenza("conference"),
    Workshop("workshop"),
    Webinar("webinar"),
    FestivalMusica("music festival"),
    FestivalCibo("food festival"),
    MostraArte("art exhibition"),
    MostraFotografia("photography exhibition"),
    Corso("course"),
    EventoBenefico("charity"),
    FestaPrivata("private party"),
    FestaCompleanno("birthday party"),
    EventoAziendale("corporate event"),
    SportCalcio("soccer"),
    SportBasket("basketball"),
    SportTennis("tennis"),
    EventoComunitario("community event"),
    Hackathon("hackathon"),
    PresentazioneLibro("book presentation");

    private String keyword;

    CategoriaEvento(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public static CategoriaEvento fromString(String categoria) {
        for (CategoriaEvento ce : CategoriaEvento.values()) {
            if (ce.name().equalsIgnoreCase(categoria)) {
                return ce;
            }
        }
        throw new IllegalArgumentException("Categoria evento non valida: " + categoria);
    }
}

