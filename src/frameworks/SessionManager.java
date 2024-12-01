package frameworks;

import entities.User;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    // Menyimpan pengguna yang sedang login
    private static Map<Integer, User> loggedInUsers = new HashMap<>();

    // Menambahkan pengguna yang login
    public static void addLoggedInUser(User user) {
        loggedInUsers.put(user.getId(), user);
    }

    // Menghapus pengguna yang logout
    public static void removeLoggedInUser(User user) {
        loggedInUsers.remove(user.getId());
    }

    // Mendapatkan daftar pengguna yang sedang login
    public static Map<Integer, User> getLoggedInUsers() {
        return loggedInUsers;
    }

    // Mendapatkan pengguna berdasarkan ID
    public static User getUserById(int userId) {
        return loggedInUsers.get(userId);
    }
}
