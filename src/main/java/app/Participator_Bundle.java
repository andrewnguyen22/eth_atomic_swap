package app;
/**
 * This class object holds all of the values
 * needed for the participant
 */
public class Participator_Bundle {
    private String contract_tx;
    private byte[] secret;
    private byte[] hash;

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
