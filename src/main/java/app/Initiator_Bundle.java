package app;
/**
 * This class object holds all of the values
 * needed for the initiator.
 */
public class Initiator_Bundle {
    private byte[] secret;
    private byte[] hash;
    private String contract_tx;
    private String initiator_address;

    Initiator_Bundle(byte[] secret, byte[] hash, String contract_tx, String initiator_address) {
        this.secret = secret;
        this.hash = hash;
        this.contract_tx = contract_tx;
        this.initiator_address= initiator_address;
    }

    public String getInitiator_address() {
        return initiator_address;
    }

    public void setInitiator_address(String initiator_address) {
        this.initiator_address = initiator_address;
    }

    public byte[] getSecret() {
        return secret;
    }

    public void setSecret(byte[] secret) {
        this.secret = secret;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public String getContract_tx() {
        return contract_tx;
    }

    public void setContract_tx(String contract_tx) {
        this.contract_tx = contract_tx;
    }
}
