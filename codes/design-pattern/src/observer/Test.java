package observer;

public class Test {
    public static void main(String[] args) {
        // 天河警察和市民
        PoliceMan tianHePoliceMan = new TianHePoliceMan();
        Citizen tianHeCitizen = new TianHeCitizen(tianHePoliceMan);
        tianHeCitizen.sendMessenger("normal");
        tianHeCitizen.sendMessenger("unnormal");

        // 黄埔警察和市民
        PoliceMan huangPuPoliceMan = new HuangPuPoliceMan();
        Citizen huangPuCitizen = new HuangPuCitizen(huangPuPoliceMan);
        huangPuCitizen.sendMessenger("normal");
        huangPuCitizen.sendMessenger("unnormal");
    }
}
