import app.*;
import com.sun.java.swing.action.AlignCenterAction;
import contracts.AtomicSwap;
import crypto.Secrets;
import javafx.scene.control.Alert;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;

public class Initiate {
    private static final BigInteger AGREED_UPON_AMOUNT = BigInteger.valueOf(200000000000000000L);
    private static final String PARENT_CONTRACT = "0x6D5ae9dd8F1a2582Deb1b096915313459f11ba70";
    private static byte[] secret;
    private static byte[] hash;
    private static byte[] hash2;
    private static byte[] secret2;
    private static final String BOBADDR = "0xe02ac7846b02503715328ecb0bc32e82e68b971f";
    private static final String ALICEADDR = "0x73e664cc0f698e3ae2aabdc702c87fe70c8c63e1";
    private static File BOBWALLET = new File(System.getProperty("user.home") + "/Library/Ethereum/rinkeby/keystore/" + "UTC--2018-02-16T18-22-07.683948588Z--e02ac7846b02503715328ecb0bc32e82e68b971f");
    private static File ALICEWALLET = new File(System.getProperty("user.home") + "/Library/Ethereum/rinkeby/keystore/" + "UTC--2018-02-16T23-37-01.313436261Z--73e664cc0f698e3ae2aabdc702c87fe70c8c63e1");

    public static void main(String[] args) {
        try {
            System.out.println("Starting App");
            Global.setWeb3j(Web3j.build(new HttpService()));
            Global.setCredentials(WalletUtils.loadCredentials("", BOBWALLET));
            System.out.println("TIP: Be patient and watch block explorer txs");

            secret = Secrets.create_secret();
            hash = Secrets.hash_ripemd160(secret);
            secret2 = Secrets.create_secret();
            hash2 = Secrets.hash_ripemd160(secret2);
            System.out.println("Bob Starting Balance = " + Web3_Tools.getBalance(BOBADDR));
            System.out.println("Alice Starting Balance = " + Web3_Tools.getBalance(ALICEADDR));
            atomic_swap_test();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */

    private static void atomic_swap_test() throws Exception {
        //Bob initiates Contract C1
        Initiator_Bundle ibundle = AT_Interaction.initiate(
                ALICEADDR,
                AGREED_UPON_AMOUNT,
                PARENT_CONTRACT,
                hash,
                secret
        );
        System.out.println("Bob Posted Contract C1 'Initiate Contract'");
        System.out.println("Bob Balance After Posting C1 = " + Web3_Tools.getBalance(BOBADDR));
        //Switch To Alice
        Global.setCredentials(WalletUtils.loadCredentials("", ALICEWALLET));

        //Alice Audits C1
        boolean success = AT_Interaction.iAudit(
                ibundle.getContract(),
                ibundle.getContract_tx(),
                AGREED_UPON_AMOUNT
        );

        //If Audit Was Successful
        if (success) {
            System.out.println("C1 Audited By Alice And She Approved");
            //Alice Creates Participant Contract C2
            Participator_Bundle pbundle = AT_Interaction.participate(
                    hash2,
                    PARENT_CONTRACT,
                    BOBADDR,
                    AGREED_UPON_AMOUNT);
            System.out.println("Alice Posted Contract C2 'Participate Contract' ");
            System.out.println("Alice Balance After Posting C2 = " + Web3_Tools.getBalance(ALICEADDR));
            //Switch Back To Bob
            Global.setCredentials(WalletUtils.loadCredentials("", BOBWALLET));
            //Bob Audits C2
            boolean success2 = AT_Interaction.pAudit(
                    PARENT_CONTRACT,
                    pbundle.getContract_tx(),
                    AGREED_UPON_AMOUNT
            );

            //If Audit Was Successful
            if (success2) {
                System.out.println("Bob Audited C2 And He Approved");
                //Bob Redeem ETH
                AT_Interaction.redeem(
                        secret2,
                        PARENT_CONTRACT,
                        hash2
                );
                System.out.println("Bob Redeemed His Money From C2");
                System.out.println("Bob Balance After Redeeming C2 = " + Web3_Tools.getBalance(BOBADDR));
                //Switch Back To Alice
                Global.setCredentials(WalletUtils.loadCredentials("", ALICEWALLET));

                //Alice Redeem Eth
                AT_Interaction.redeem(
                        secret,
                        PARENT_CONTRACT,
                        hash
                );
                System.out.println("Alice Redeemed Her Money From C1");
                System.out.println("Alice Balance After Redeeming C1 = " + Web3_Tools.getBalance(ALICEADDR));
                Thread.sleep(1000);
                System.err.println("Final Balances:");
                System.err.println("BOB: " + Web3_Tools.getBalance(BOBADDR));
                System.err.println("Alice: " + Web3_Tools.getBalance(ALICEADDR));
            } else {
                System.err.println("Bobs Audit of C2 Failed");
            }

        } else {
            System.err.println("Alice Audit of C1 Failed");
        }
    }
}
