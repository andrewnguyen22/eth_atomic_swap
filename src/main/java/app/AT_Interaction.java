package app;

import contracts.AtomicSwap;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class AT_Interaction {
    private final static BigInteger refundTime48 = BigInteger.valueOf(172800L); //48 Hours in seconds
    private final static BigInteger refundTime24 = BigInteger.valueOf(86400L);  //24 Hours In seconds

    /**
     * Bob Creates Initiate Contract C1
     *
     * @param participant_addr  The Address Of Alice
     * @param amount            The amount agreed upon
     * @param hash              The hash of the secret (byte array)
     * @param secret            The secret (byte array)
     * @return ibundle          A Class With The Needed Values
     * @throws Exception
     */
    public static Initiator_Bundle initiate(String participant_addr,
                                            BigInteger amount,
                                            byte[] hash,
                                            byte[] secret) throws Exception {

        //Load Contract Wrapper
        AtomicSwap contract = Web3_Tools.loadContractWrapper();

        //Call Contract Function 'Initiate'
        TransactionReceipt t = contract.initiate(
                refundTime48,
                hash,
                participant_addr,
                amount
        ).send();

        //Creates The Initiator Bundle and Returns it
        Initiator_Bundle bundle = new Initiator_Bundle(
                secret,
                hash,
                t.getTransactionHash(),
                Global.getCredentials().getAddress()
        );
        System.err.println("Transaction Hash Of C1:\n" +
                t.getTransactionHash());
        return bundle;

    }

    /**
     * Alice Creates Participate Contract C2
     *
     * @param hash              The Hash Of The Secret -> Emailed From Bob
     * @param initiator_address The Bobs Address
     * @param amount            The Agreed Upon Amount
     * @return pbundle          A Class Obj With All Of The Values Needed By Alice
     * @throws Exception
     */
    public static Participator_Bundle participate(byte[] hash,
                                                  String initiator_address,
                                                  BigInteger amount) throws Exception {
        //Load Wrapper Contract
        AtomicSwap contract = Web3_Tools.loadContractWrapper();

        //Call Contract Function 'Participate'
        TransactionReceipt t = contract.participate(
                refundTime24,
                hash,
                initiator_address,
                amount
        ).send();

        //Print TX Hash
        System.err.println("Transaction Hash Of C2:\n" +
                t.getTransactionHash());

        //Create New pbundle w/ tx hash and contract addr
        Participator_Bundle participator_bundle = new Participator_Bundle();
        participator_bundle.setContract_tx(t.getTransactionHash());
        return participator_bundle;
    }

    /**
     * Alice Audits C1 Before Posting C2
     *
     * @param contract_tx           Contract Tx of C1 -> Emailed From Bob
     * @param agreed_upon_amount    Agreed Upon Amount To Be Double Checked
     * @return boolean              Whether Or Not C1 Was Approved
     * @throws IOException
     */
    public static boolean iAudit(String contract_tx, BigInteger agreed_upon_amount) throws IOException {

        //Load Contract Wrapper
        AtomicSwap contract = Web3_Tools.loadContractWrapper();

        //Get The Receipt Of The C1 TX
        TransactionReceipt transactionReceipt =
                Global.getWeb3j().ethGetTransactionReceipt(contract_tx).send().getResult();

        //Get The Funds Variable From C1
        List<AtomicSwap.InitiatedEventResponse> l = contract.getInitiatedEvents(transactionReceipt);

        //Return True if Contract Matches Wrapper and Funds = Agreed Upon Amount
        return (contract.isValid() && l.get(0)._funds.equals(agreed_upon_amount));
    }

    /**
     * Bob Audits C2 Before Redeeming
     *
     * @param contract_tx           C2 Tx Hash -> Emailed From Alice
     * @param agreed_upon_amount    The Agreed Upon Amount
     * @return boolean              Whether Or Not C2 Was Approved
     * @throws IOException
     */
    public static boolean pAudit(String contract_tx, BigInteger agreed_upon_amount) throws IOException {

        //Load Wrapper
        AtomicSwap contract = Web3_Tools.loadContractWrapper();

        //Get TX From Contract TX
        TransactionReceipt transactionReceipt =
                Global.getWeb3j().ethGetTransactionReceipt(contract_tx).send().getResult();

        //Gets The Value Variable From C2
        List<AtomicSwap.ParticipatedEventResponse> l = contract.getParticipatedEvents(transactionReceipt);

        //Returns True If Contract Matches The Wrapper And Value = Agreed Upon Amount
        return (contract.isValid() && l.get(0)._value.equals(agreed_upon_amount));
    }

    /**
     * Can Be Called By Alice And/Or Bob to Retrieve Funds
     *
     * @param secret Secret Retrieved From Contract After Made Public
     * @param hash   Hash Retrieved From Email
     * @throws Exception
     */
    public static void redeem(byte[] secret, byte[] hash) throws Exception {

        //Create Wrapper Contract
        AtomicSwap contract = Web3_Tools.loadContractWrapper();

        //Calls Contract 'Redeem' Function
        TransactionReceipt t = contract.redeem(
                secret,
                hash
        ).send();

        //Prints Transaction Hash of Redeem
        System.err.println("Redeem TX Hash:\n" +
                t.getTransactionHash());
    }

    /**
     * Retrieve Secret From Contract After Counterpart Redeems
     *
     * @param contract_tx           Tx Of Contract
     * @return byte[]               Shared Secret For Redeem
     * @throws IOException
     */
    public static byte[] getSecretFromContract(String contract_tx) throws IOException {

        //Create Contract Wrapper
        AtomicSwap contract = Web3_Tools.loadContractWrapper();
        //Get The Values From The Contract TX Recipet
        TransactionReceipt t = Global.getWeb3j().ethGetTransactionReceipt(contract_tx).send().getResult();

        //Get Participated Event And Pull Secret From it 
        List<AtomicSwap.ParticipatedEventResponse> l = contract.getParticipatedEvents(t);

        //Return Secret
        return l.get(0)._hashedSecret; //should be secret after redeemed
    }

    /**
     * Unused Conversion Functions Below Hex->Byte Array | Vise Versa
     */

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
