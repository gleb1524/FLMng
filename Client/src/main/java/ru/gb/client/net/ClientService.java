package ru.gb.client.net;

import ru.gb.client.ClientController;
import ru.gb.client.WorkController;
import ru.gb.client.RegController;

public class ClientService {

   private static RegController regController;
   private static WorkController workController;
   private static ClientController clientController;
   private static String auth;

    public static String getAuth() {
        return auth;
    }

    public static void setAuth(String auth) {
        ClientService.auth = auth;
    }

    public static ClientController getClientController() {
        return clientController;
    }

    public static void setClientController(ClientController clientController) {
        ClientService.clientController = clientController;
    }

    public static WorkController getWorkController() {
        return workController;
    }

    public static void setWorkController(WorkController authorizationController) {
        ClientService.workController = authorizationController;
    }

    public static void setRegController(RegController regController) {
        ClientService.regController = regController;
    }

    public static RegController getRegController() {
        return regController;
    }
}
