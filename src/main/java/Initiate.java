import app.*;
import crypto.Secrets;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;

/**
 * **The Atomic Swap Test**
 * BOB is Initiator (wants to transfer x ETH for another coin <in this case more ETH>)
 * Alice is the Participant (Facilitates exchange for BOB)
 * NOTES:
 * Runs off of RPC (localhost:8545) so can test on ganache-cli as well
 * A normal Atomic Swap Does Not Need TWO secrets..
 * But An On Chain one that uses one contract does (Simulating different chains is hard :<)
 */

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
            System.out.println("TIP: Be patient and watch rinkeby block explorer txs");

            Global.setWeb3j(Web3j.build(new HttpService()));
            Global.setCredentials(WalletUtils.loadCredentials("", BOBWALLET));
            Global.setParent_contract(PARENT_CONTRACT);

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
     * Atomic Swap Test
     * Bob Initiates AS
     * Alice Audits
     * Alice Participates
     * Bob Audits
     * Bob redeems <ETH>
     * Alice redeems <ETH>
     *
     * @throws Exception
     */

    private static void atomic_swap_test() throws Exception {

        //Bob initiates Contract C1
        Initiator_Bundle ibundle = AT_Commands.initiate(
                ALICEADDR,
                AGREED_UPON_AMOUNT,
                hash,
                secret
        );

        System.out.println("Bob Posted Contract C1 'Initiate Contract'");
        System.out.println("Bob Balance After Posting C1 = " + Web3_Tools.getBalance(BOBADDR));

        //Switch To Alice
        Global.setCredentials(WalletUtils.loadCredentials("", ALICEWALLET));

        //Alice Audits C1
        boolean success = AT_Commands.iAudit(
                ibundle.getContract_tx(),
                AGREED_UPON_AMOUNT
        );

        //If Audit Was Successful
        if (success) {
            System.out.println("C1 Audited By Alice And She Approved");

            //Alice Creates Participant Contract C2
            Participator_Bundle pbundle = AT_Commands.participate(
                    hash2, //Needed New Hash Because On Same Chain
                    BOBADDR,
                    AGREED_UPON_AMOUNT);

            System.out.println("Alice Posted Contract C2 'Participate Contract' ");
            System.out.println("Alice Balance After Posting C2 = " + Web3_Tools.getBalance(ALICEADDR));

            //Switch Back To Bob
            Global.setCredentials(WalletUtils.loadCredentials("", BOBWALLET));

            //Bob Audits C2
            boolean success2 = AT_Commands.pAudit(
                    pbundle.getContract_tx(),
                    AGREED_UPON_AMOUNT
            );

            //If Audit Was Successful
            if (success2) {
                System.out.println("Bob Audited C2 And He Approved");

                //Bob Redeem ETH
                AT_Commands.redeem(
                        secret2,
                        hash2
                );

                System.out.println("Bob Redeemed His Money From C2");
                System.out.println("Bob Balance After Redeeming C2 = " + Web3_Tools.getBalance(BOBADDR));

                //Switch Back To Alice
                Global.setCredentials(WalletUtils.loadCredentials("", ALICEWALLET));

                //Alice Redeem Eth
                AT_Commands.redeem(
                        secret,
                        hash
                );

                System.out.println("Alice Redeemed Her Money From C1");
                System.out.println("Alice Balance After Redeeming C1 = " + Web3_Tools.getBalance(ALICEADDR));

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
