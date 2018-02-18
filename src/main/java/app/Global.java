package app;

import contracts.AtomicSwap;
import org.web3j.protocol.Web3j;

public class Global {
    private static Web3j web3j;
    private static org.web3j.crypto.Credentials credentials;

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
