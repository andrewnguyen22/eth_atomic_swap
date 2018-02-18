package app;

import org.web3j.protocol.Web3j;
/**
 * This class holds all the global values needed (credentials / web3 obj)
 */
public class Global {
    private static Web3j web3j;
    private static org.web3j.crypto.Credentials credentials;
    private static String parent_contract;

    public static String getParent_contract() {
        return parent_contract;
    }

    public static void setParent_contract(String parent_contract) {
        Global.parent_contract = parent_contract;
    }

    public static org.web3j.crypto.Credentials getCredentials() {
        return credentials;
    }

    public static void setCredentials(org.web3j.crypto.Credentials credentials) {
        Global.credentials = credentials;
    }

    public static Web3j getWeb3j() {
        return web3j;
    }

    public static void setWeb3j(Web3j web3j) {
        Global.web3j = web3j;
    }
}
