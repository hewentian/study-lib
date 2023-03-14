package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Citizen {
    protected List<PoliceMan> policeManList;
    private String help = "normal";

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    abstract void sendMessenger(String help);

    public void setPoliceMan() {
        this.policeManList = new ArrayList<>();
    }

    public void register(PoliceMan policeMan) {
        this.policeManList.add(policeMan);
    }

    public void unRegister(PoliceMan policeMan) {
        this.policeManList.remove(policeMan);
    }
}
