package Backend;

import Backend.controllers.MessagesController;
import Backend.controllers.UsersController;

public class Main {
    public static void main(String[] args) {
        UsersController uc = new UsersController();
        MessagesController mc = new MessagesController();
    }
}
