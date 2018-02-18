package app;

import contracts.AtomicSwap;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

public class AT_Interaction {
    private final static BigInteger refundTime48 = BigInteger.valueOf(172800L);//48 Hours in seconds
    private final static BigInteger refundTime24 = BigInteger.valueOf(86400L);

    public static Initiator_Bundle initiate(String participant_addr,
                                            BigInteger amount,
                                            String contract_addr, byte[] hash, byte[] secret) throws Exception {

        //Load Contract Wrapper
        AtomicSwap contract = AtomicSwap.load(
                contract_addr,
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
        //Call Contract Function 'Initiate'
        TransactionReceipt t = contract.initiate(
                refundTime48,
                hash,
                participant_addr,
                amount
        ).send();

        //currently static might need to change
        Initiator_Bundle bundle = new Initiator_Bundle(
                secret,
                hash,
                contract_addr,
                t.getTransactionHash(),
                Global.getCredentials().getAddress()
        );
        System.err.println(t.getTransactionHash());
        return bundle;

    }

    public static Participator_Bundle participate(byte[] hash,
                                                  String contract_addr,
                                                  String initiator_address,
                                                  BigInteger amount) throws Exception {
        AtomicSwap contract = AtomicSwap.load(
                contract_addr,
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
        TransactionReceipt t = contract.participate(
                refundTime24,
                hash,
                initiator_address,
                amount
        ).send();

        Participator_Bundle participator_bundle = new Participator_Bundle();
        participator_bundle.setContract(t.getContractAddress());
        participator_bundle.setContract_tx(t.getTransactionHash());
        return participator_bundle;
    }

    public static boolean iAudit(String contract_addr, String contract_tx, BigInteger agreed_upon_amount) throws IOException {
        AtomicSwap contract = AtomicSwap.load(
                contract_addr,
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
        TransactionReceipt transactionReceipt =
                Global.getWeb3j().ethGetTransactionReceipt(contract_tx).send().getResult();
        List<AtomicSwap.InitiatedEventResponse> l = contract.getInitiatedEvents(transactionReceipt);
        return (contract.isValid() && l.get(0)._funds.equals(agreed_upon_amount));
    }

    public static boolean pAudit(String contract_addr,
                                 String contract_tx,
                                 BigInteger agreed_upon_amount) throws IOException {
        //Load Wrapper
        AtomicSwap contract = AtomicSwap.load(
                contract_addr,
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
        //Get TX From Contract TX
        TransactionReceipt transactionReceipt =
                Global.getWeb3j().ethGetTransactionReceipt(contract_tx).send().getResult();
        List<AtomicSwap.ParticipatedEventResponse> l = contract.getParticipatedEvents(transactionReceipt);
        return (contract.isValid() && l.get(0)._value.equals(agreed_upon_amount));
    }

    public static void redeem(byte[] secret, String contract_address, byte[] hash) throws Exception {
        AtomicSwap contract = AtomicSwap.load(
                contract_address,
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
        TransactionReceipt t = contract.redeem(
                secret,
                hash
        ).send();
        System.err.println(t.getTransactionHash());
    }

    public static byte[] getSecretFromContract(String contract_addr, String contract_tx) throws IOException {
        AtomicSwap contract = AtomicSwap.load(
                contract_addr,
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
        TransactionReceipt t = Global.getWeb3j().ethGetTransactionReceipt(contract_tx).send().getResult();
        List<AtomicSwap.RedeemedEventResponse> l = contract.getRedeemedEvents(t);
        List<AtomicSwap.ParticipatedEventResponse> l2 = contract.getParticipatedEvents(t);
        return l2.get(0)._hashedSecret; //should be secret after redeemed
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
