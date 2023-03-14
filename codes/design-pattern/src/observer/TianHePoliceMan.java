package observer;

public class TianHePoliceMan implements PoliceMan {
    @Override
    public void action(Citizen citizen) {
        String help = citizen.getHelp();
        if ("normal".equals(help)) {
            System.out.println("一切正常，不必外出");
        } else if ("unnormal".equals(help)) {
            System.out.println("有犯罪行为，天河警察出动");
        }
    }
}
