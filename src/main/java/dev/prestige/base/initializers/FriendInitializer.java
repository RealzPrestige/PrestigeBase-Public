package dev.prestige.base.initializers;
import java.util.ArrayList;

public class FriendInitializer {
    public ArrayList<FriendPlayer> friendList = new ArrayList<>();

    public void addFriend(String name){
        friendList.add(new FriendPlayer(name));
    }
    public ArrayList<FriendPlayer> getFriendList() {
        return friendList;
    }

    public boolean isFriend(String name){
        return friendList.contains(new FriendPlayer(name));
    }

    public static class FriendPlayer {
        String name;
        public FriendPlayer(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
