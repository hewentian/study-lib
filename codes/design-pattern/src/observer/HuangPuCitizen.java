package observer;

public class HuangPuCitizen extends Citizen {
    public HuangPuCitizen(PoliceMan policeMan) {
        setPoliceMan();
        register(policeMan);
    }

    @Override
    void sendMessenger(String help) {
        setHelp(help);
        for (PoliceMan policeMan : policeManList) {
            // 通知警察行动
            policeMan.action(this);
        }
    }
}
