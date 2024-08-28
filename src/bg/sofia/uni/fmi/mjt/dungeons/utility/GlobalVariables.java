package bg.sofia.uni.fmi.mjt.dungeons.utility;

import bg.sofia.uni.fmi.mjt.dungeons.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalVariables {
    public static List<User> allRegisteredUsers;
    public static Map<String, String> usersCredentials = new HashMap<>();
    public static List<User> activeUsers;
}
