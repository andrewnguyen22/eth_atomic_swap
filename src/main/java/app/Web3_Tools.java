package app;

import contracts.AtomicSwap;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.IOException;
import java.math.BigInteger;

import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

public class Web3_Tools {
    private static BigInteger getGasLimit(Web3j web3) {
        EthBlock ethBlock = null;
        try {
            ethBlock = web3.ethGetBlockByNumber(DefaultBlockParameterName.PENDING, true).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert ethBlock != null;
        assert ethBlock.getBlock().getGasLimit() != null;
        return ethBlock.getBlock().getGasLimit().divide(BigInteger.valueOf(10L));
    }
    public static BigInteger getBalance(String addr) throws IOException {
        EthGetBalance ethGetBalance = Global.getWeb3j().ethGetBalance(addr, DefaultBlockParameterName.LATEST).send();
        return ethGetBalance.getBalance();
    }
    public static AtomicSwap loadContractWrapper(){
        return AtomicSwap.load(
                Global.getParent_contract(),
                Global.getWeb3j(),
                Global.getCredentials(),
                GAS_PRICE,
                Web3_Tools.getGasLimit(Global.getWeb3j())
        );
    }
}
