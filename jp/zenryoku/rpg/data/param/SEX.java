package jp.zenryoku.rpg.data.param;


/**
 * Enumeration class SEX - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum SEX
{
    MAN(0,"男")
    , WOMAN(1, "女")
    , MAIL(2, "雄")
    , FEMAIL(3, "雌")
    , ANDROGYNOS(4, "両性具有");
    
    private int intVal;
    private String name;
    
    private SEX(int i, String name) {
        this.intVal = i;
        this.name = name;
    }
}
