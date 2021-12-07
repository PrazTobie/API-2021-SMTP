package PrankSMTP;

import java.util.ArrayList;
import java.util.Random;

public class Group {
    public String sender;
    public ArrayList<String> victims = new ArrayList<>();
    public String message;

    public static ArrayList<Group> parseGroups(Config config) {
        Random r = new Random();
        ArrayList<Group> groups = new ArrayList<>();
        int groupSize = config.victims.size() / config.numGroups;

        Group current = new Group();
        for (String victim : config.victims) {
            if(current.sender == null) {
                current.sender = victim;
            } else {
                current.victims.add(victim);
            }

            if(current.victims.size() + 1 == groupSize) {
                current.message = config.messages.get(r.nextInt(config.messages.size()));
                groups.add(current);
                current = new Group();
            }
        }

        if(current.sender != null && current.victims.size() + 1 < groupSize) {
            current.victims.add(current.sender);
            while (current.victims.size() != 0) {
                groups.get(Math.max(0,groups.size() - current.victims.size())).victims.add(current.victims.remove(0));
            }
        }

        return groups;
    }
}
